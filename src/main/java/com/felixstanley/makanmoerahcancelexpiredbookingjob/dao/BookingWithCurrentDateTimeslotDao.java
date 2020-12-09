package com.felixstanley.makanmoerahcancelexpiredbookingjob.dao;

import com.felixstanley.makanmoerahcancelexpiredbookingjob.entity.enums.BookingStatus;
import com.felixstanley.makanmoerahcancelexpiredbookingjob.entity.view.BookingWithCurrentDateTimeslot;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Felix
 */
public interface BookingWithCurrentDateTimeslotDao extends
    JpaRepository<BookingWithCurrentDateTimeslot, Integer> {

  @Query(
      "SELECT b.usersId FROM BookingWithCurrentDateTimeslot b WHERE (b.confirmedDate < b.currentLocalDate"
          + " OR (b.confirmedDate = b.currentLocalDate AND b.timeslot < b.currentTimeslot)) "
          + "AND b.bookingStatus = :bookingStatus GROUP BY b.usersId ORDER BY b.usersId ASC")
  List<Integer> findUsersIdExpiredBooking(@Param("bookingStatus") BookingStatus bookingStatus);

}
