package com.felixstanley.makanmoerahcancelexpiredbookingjob.writer;

import com.felixstanley.makanmoerahcancelexpiredbookingjob.dao.BookingDao;
import com.felixstanley.makanmoerahcancelexpiredbookingjob.dao.UsersDao;
import com.felixstanley.makanmoerahcancelexpiredbookingjob.entity.Booking;
import com.felixstanley.makanmoerahcancelexpiredbookingjob.entity.Users;
import com.felixstanley.makanmoerahcancelexpiredbookingjob.entity.enums.BookingStatus;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

/**
 * @author Felix
 */
@AllArgsConstructor
@Slf4j
public class ExpireBookingWriter implements ItemWriter<Booking> {

  private BookingDao bookingDao;
  private UsersDao usersDao;

  private Integer usersId;

  @Override
  public void write(List<? extends Booking> bookings) throws Exception {
    log.info("Expiring Following Bookings: {}", bookings);
    bookings.forEach(booking -> {
      booking.setBookingStatus(BookingStatus.CANCELLED);
      booking.setCancelledBySystem(true);
    });
    bookingDao.saveAll(bookings);
    bookingDao.flush();

    // Increase No Show Count for this user by the number of expired bookings
    log.info("Obtaining Users with Id: {}", usersId);
    Users existingUsers = usersDao.findById(usersId).orElse(null);
    log.info("Obtained Users: {}", existingUsers);
    log.info("Will increase his no show count by {}", bookings.size());
    existingUsers.setNoShowCount((short) (existingUsers.getNoShowCount() + bookings.size()));
    usersDao.saveAndFlush(existingUsers);
  }
}
