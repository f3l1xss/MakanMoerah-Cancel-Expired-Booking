package com.felixstanley.makanmoerahcancelexpiredbookingjob.configuration;

import com.felixstanley.makanmoerahcancelexpiredbookingjob.constant.Constants;
import com.felixstanley.makanmoerahcancelexpiredbookingjob.dao.BookingDao;
import com.felixstanley.makanmoerahcancelexpiredbookingjob.dao.BookingWithCurrentDateTimeslotDao;
import com.felixstanley.makanmoerahcancelexpiredbookingjob.dao.UsersDao;
import com.felixstanley.makanmoerahcancelexpiredbookingjob.entity.Booking;
import com.felixstanley.makanmoerahcancelexpiredbookingjob.entity.enums.BookingStatus;
import com.felixstanley.makanmoerahcancelexpiredbookingjob.listener.LoggingJobListener;
import com.felixstanley.makanmoerahcancelexpiredbookingjob.partitioner.UsersIdPartitioner;
import com.felixstanley.makanmoerahcancelexpiredbookingjob.rowmapper.BookingRowMapper;
import com.felixstanley.makanmoerahcancelexpiredbookingjob.tasklet.DisableUsersTasklet;
import com.felixstanley.makanmoerahcancelexpiredbookingjob.writer.ExpireBookingWriter;
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author Felix
 */
@Configuration
// Needed because of spring.main.web-application-type=none properties
@EnableRedisHttpSession
@EnableBatchProcessing
public class BatchConfiguration {

  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Autowired
  private DataSource dataSource;

  @Autowired
  private BookingDao bookingDao;

  @Autowired
  private BookingWithCurrentDateTimeslotDao bookingWithCurrentDateTimeslotDao;

  @Autowired
  private UsersDao usersDao;

  @Autowired
  private FindByIndexNameSessionRepository findByIndexNameSessionRepository;

  @Bean
  public ConfigureRedisAction configureRedisAction() {
    // Disable Redis Keyspace events for Generic commands and Expired events
    // Because AWS ElastiCache forbids Config Modification
    return ConfigureRedisAction.NO_OP;
  }

  @Bean(name = "cancelExpiredBookingJob")
  public Job cancelExpiredBookingJob(Step cancelExpiredBookingManager) {
    return this.jobBuilderFactory.get("cancelExpiredBookingJob").listener(loggingJobListener())
        .start(cancelExpiredBookingManager).build();
  }

  @Bean
  public Step cancelExpiredBookingManager(Partitioner usersIdPartitioner,
      Step cancelExpiredBooking) {
    return this.stepBuilderFactory.get("cancelExpiredBooking.manager")
        .partitioner(cancelExpiredBooking.getName(), usersIdPartitioner).step(cancelExpiredBooking)
        .allowStartIfComplete(true).build();
  }

  @Bean
  public Partitioner usersIdPartitioner() {
    return new UsersIdPartitioner(bookingWithCurrentDateTimeslotDao, usersDao);
  }

  @Bean
  public Step cancelExpiredBooking(Step cancelBooking, Step disableUsers) {
    Flow cancelExpiredBookingFlow = new FlowBuilder<SimpleFlow>("cancelExpiredBookingFlow")
        .start(cancelBooking).next(disableUsers).build();
    return this.stepBuilderFactory.get("cancelExpiredBooking").flow(cancelExpiredBookingFlow)
        .allowStartIfComplete(true).build();
  }

  @Bean
  public Step cancelBooking(ItemReader bookingItemReader, ItemWriter expireBookingWriter) {
    return ((SimpleStepBuilder) this.stepBuilderFactory.get("cancelBooking")
        .chunk(Integer.MAX_VALUE)
        .reader(bookingItemReader).writer(expireBookingWriter).allowStartIfComplete(true)).build();
  }

  @Bean
  public Step disableUsers(Tasklet disableUsersTasklet) {
    return this.stepBuilderFactory.get("disableUsers").tasklet(disableUsersTasklet)
        .allowStartIfComplete(true).build();
  }

  public RowMapper<Booking> bookingRowMapper() {
    return new BookingRowMapper();
  }

  @Bean
  @StepScope
  public JdbcCursorItemReader<Booking> bookingItemReader(@Value(
      "#{stepExecutionContext[" + Constants.USERS_ID_EXECUTION_CONTEXT_KEY_NAME
          + "]}") Integer usersId) {
    // Querying from booking_with_current_date_and_timeslot view
    return new JdbcCursorItemReaderBuilder<Booking>().name("bookingItemReader")
        .dataSource(dataSource).rowMapper(bookingRowMapper())
        .sql("SELECT id, restaurant_id, users_id,"
            + " confirmed_date, timeslot, booking_code, cancelled_by_system FROM booking_with_current_date_and_timeslot"
            + " WHERE (confirmed_date < current_local_date OR (confirmed_date = current_local_date "
            + "AND timeslot < current_timeslot)) AND booking_status = ? AND users_id = ?")
        .queryArguments(BookingStatus.ONGOING.ordinal(), usersId).driverSupportsAbsolute(true)
        .build();
  }

  @Bean
  @StepScope
  public ExpireBookingWriter expireBookingWriter(
      @Value("#{stepExecutionContext[" + Constants.USERS_ID_EXECUTION_CONTEXT_KEY_NAME
          + "]}") Integer usersId) {
    return new ExpireBookingWriter(bookingDao, usersDao, usersId);
  }

  @Bean
  @StepScope
  public Tasklet disableUsersTasklet(
      @Value("#{stepExecutionContext[" + Constants.USERS_ID_EXECUTION_CONTEXT_KEY_NAME
          + "]}") Integer usersId) {
    return new DisableUsersTasklet(usersDao, usersId, findByIndexNameSessionRepository);
  }

  public JobExecutionListener loggingJobListener() {
    return new LoggingJobListener();
  }

}
