package com.felixstanley.makanmoerahcancelexpiredbookingjob.dao;

import com.felixstanley.makanmoerahcancelexpiredbookingjob.entity.Booking;
import com.felixstanley.makanmoerahcancelexpiredbookingjob.entity.enums.BookingStatus;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Felix
 */
public interface BookingDao extends JpaRepository<Booking, Integer> {

  @Query("SELECT b.usersId FROM Booking b WHERE "
      + "(b.confirmedDate < :currentDate OR (b.confirmedDate = :currentDate "
      + "AND b.timeslot < :currentTimeslot)) AND b.bookingStatus = :bookingStatus "
      + "GROUP BY b.usersId ORDER BY b.usersId ASC")
  List<Integer> findUsersIdExpiredBooking(@Param("currentDate") Date currentDate,
      @Param("currentTimeslot") Short timeslot,
      @Param("bookingStatus") BookingStatus bookingStatus);

}
