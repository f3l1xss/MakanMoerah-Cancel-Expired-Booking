package com.felixstanley.makanmoerahcancelexpiredbookingjob.entity.view;

import com.felixstanley.makanmoerahcancelexpiredbookingjob.entity.AbstractEntity;
import com.felixstanley.makanmoerahcancelexpiredbookingjob.entity.enums.BookingStatus;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.Data;
import lombok.ToString;

/**
 * @author Felix Represents 'booking_with_current_date_and_timeslot' view
 */
@Entity()
@Table(name = "booking_with_current_date_and_timeslot")
@Data
@ToString(callSuper = true)
public class BookingWithCurrentDateTimeslot extends AbstractEntity implements Serializable {

  @Column(name = "users_id")
  private Integer usersId;

  @Column(name = "confirmed_date")
  private LocalDate confirmedDate;

  private Short timeslot;

  @Column(name = "booking_status")
  @Enumerated
  private BookingStatus bookingStatus;

  @Column(name = "current_local_date")
  private LocalDate currentLocalDate;

  @Column(name = "current_timeslot")
  private Short currentTimeslot;

}
