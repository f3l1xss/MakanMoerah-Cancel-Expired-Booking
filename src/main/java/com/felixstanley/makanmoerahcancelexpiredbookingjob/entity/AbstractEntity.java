package com.felixstanley.makanmoerahcancelexpiredbookingjob.entity;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Data;

/**
 * @author Felix
 */
@MappedSuperclass()
@Data
public class AbstractEntity {

  @Id
  private Integer id;

}
