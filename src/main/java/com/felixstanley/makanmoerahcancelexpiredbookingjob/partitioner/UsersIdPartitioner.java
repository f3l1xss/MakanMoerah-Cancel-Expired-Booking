package com.felixstanley.makanmoerahcancelexpiredbookingjob.partitioner;

import com.felixstanley.makanmoerahcancelexpiredbookingjob.constant.Constants;
import com.felixstanley.makanmoerahcancelexpiredbookingjob.dao.BookingWithCurrentDateTimeslotDao;
import com.felixstanley.makanmoerahcancelexpiredbookingjob.dao.UsersDao;
import com.felixstanley.makanmoerahcancelexpiredbookingjob.entity.enums.BookingStatus;
import com.felixstanley.makanmoerahcancelexpiredbookingjob.utility.Utility;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

/**
 * @author Felix
 */
@AllArgsConstructor
@Slf4j
public class UsersIdPartitioner implements Partitioner {

  private static final String PARTITION_KEY = "partition";

  private BookingWithCurrentDateTimeslotDao bookingWithCurrentDateTimeslotDao;
  private UsersDao usersDao;

  @Override
  public Map<String, ExecutionContext> partition(int gridSize) {
    log.info(
        "Obtaining UsersId from Expired Bookings at current UTC Date: {} and current UTC Timeslot: {}",
        LocalDate.now(ZoneOffset.UTC), Utility.getCurrentTimeslot(ZoneOffset.UTC));
    List<Integer> usersIds = bookingWithCurrentDateTimeslotDao
        .findUsersIdExpiredBooking(BookingStatus.ONGOING);
    log.info("Obtained following users Ids: {}", usersIds);
    Map<String, ExecutionContext> map = new HashMap<>(gridSize);
    int i = 0;
    for (Integer id : usersIds) {
      map.put(PARTITION_KEY + i, getExecutionContext(id));
      i++;
    }
    return map;
  }

  private ExecutionContext getExecutionContext(Integer usersId) {
    ExecutionContext executionContext = new ExecutionContext();
    executionContext.put(Constants.USERS_ID_EXECUTION_CONTEXT_KEY_NAME, usersId);
    return executionContext;
  }

}
