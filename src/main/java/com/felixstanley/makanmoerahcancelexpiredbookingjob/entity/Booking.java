package com.felixstanley.makanmoerahcancelexpiredbookingjob.entity;

import com.felixstanley.makanmoerahcancelexpiredbookingjob.entity.enums.BookingStatus;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import lombok.Data;
import lombok.ToString;

/**
 * @author Felix
 */
@Entity
@Data
@ToString(callSuper = true)
public class Booking extends AbstractEntity implements Serializable {

  @Column(name = "restaurant_id")
  private Integer restaurantId;

  @Column(name = "users_id")
  private Integer usersId;

  @Column(name = "confirmed_date")
  private Date confirmedDate;

  private Short timeslot;

  @Column(name = "booking_code")
  private String bookingCode;

  @Column(name = "booking_status")
  @Enumerated
  private BookingStatus bookingStatus;

  @Column(name = "cancelled_by_system")
  private Boolean cancelledBySystem;

}
