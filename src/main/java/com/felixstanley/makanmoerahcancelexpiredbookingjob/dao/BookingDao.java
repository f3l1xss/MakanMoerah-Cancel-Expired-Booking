package com.felixstanley.makanmoerahcancelexpiredbookingjob.dao;

import com.felixstanley.makanmoerahcancelexpiredbookingjob.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Felix
 */
public interface BookingDao extends JpaRepository<Booking, Integer> {

}
