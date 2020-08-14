package com.felixstanley.makanmoerahcancelexpiredbookingjob.rowmapper;

import com.felixstanley.makanmoerahcancelexpiredbookingjob.entity.Booking;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author Felix
 */
public class BookingRowMapper implements RowMapper<Booking> {

  // Booking Column Names
  private static final String ID_COLUMN_NAME = "id";
  private static final String RESTAURANT_ID_COLUMN_NAME = "restaurant_id";
  private static final String USERS_ID_COLUMN_NAME = "users_id";
  private static final String CONFIRMED_DATE_COLUMN_NAME = "confirmed_date";
  private static final String TIMESLOT_COLUMN_NAME = "timeslot";
  private static final String BOOKING_CODE_COLUMN_NAME = "booking_code";
  private static final String CANCELLED_BY_SYSTEM_COLUMN_NAME = "cancelled_by_system";

  @Override
  public Booking mapRow(ResultSet resultSet, int rowNum) throws SQLException {
    Booking booking = new Booking();
    booking.setId(resultSet.getInt(ID_COLUMN_NAME));
    booking.setRestaurantId(resultSet.getInt(RESTAURANT_ID_COLUMN_NAME));
    booking.setUsersId(resultSet.getInt(USERS_ID_COLUMN_NAME));
    booking.setConfirmedDate(resultSet.getDate(CONFIRMED_DATE_COLUMN_NAME));
    booking.setTimeslot(resultSet.getShort(TIMESLOT_COLUMN_NAME));
    booking.setBookingCode(resultSet.getString(BOOKING_CODE_COLUMN_NAME));
    booking.setCancelledBySystem(resultSet.getBoolean(CANCELLED_BY_SYSTEM_COLUMN_NAME));
    return booking;
  }
}
