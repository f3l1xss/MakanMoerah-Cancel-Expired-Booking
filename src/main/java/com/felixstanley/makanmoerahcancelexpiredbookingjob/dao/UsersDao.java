package com.felixstanley.makanmoerahcancelexpiredbookingjob.dao;

import com.felixstanley.makanmoerahcancelexpiredbookingjob.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Felix
 */
public interface UsersDao extends JpaRepository<Users, Integer> {

}
