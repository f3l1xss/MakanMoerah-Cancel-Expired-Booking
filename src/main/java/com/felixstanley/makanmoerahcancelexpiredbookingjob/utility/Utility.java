package com.felixstanley.makanmoerahcancelexpiredbookingjob.utility;

import com.felixstanley.makanmoerahcancelexpiredbookingjob.constant.Constants;
import java.time.LocalTime;

/**
 * @author Felix
 */
public class Utility {

  public static Short getCurrentTimeslot() {
    return getTimeslotFromLocalTime(LocalTime.now());
  }

  public static Short getTimeslotFromLocalTime(LocalTime localTime) {
    return (short) (Math.floor(((localTime.getHour() * 60) + localTime.getMinute())
        / Constants.DEFAULT_BOOKING_TIME_INTERVAL) + 1);
  }

}
