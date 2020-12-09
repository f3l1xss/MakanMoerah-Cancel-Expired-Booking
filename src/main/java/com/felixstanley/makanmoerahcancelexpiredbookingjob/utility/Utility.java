package com.felixstanley.makanmoerahcancelexpiredbookingjob.utility;

import com.felixstanley.makanmoerahcancelexpiredbookingjob.constant.Constants;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * @author Felix
 */
public class Utility {

  public static Short getCurrentTimeslot(ZoneId zoneId) {
    return getTimeslotFromLocalTime(LocalTime.now(zoneId));
  }

  public static Short getTimeslotFromLocalTime(LocalTime localTime) {
    return (short) (Math.floor(((localTime.getHour() * 60) + localTime.getMinute())
        / Constants.DEFAULT_BOOKING_TIME_INTERVAL) + 1);
  }

}
