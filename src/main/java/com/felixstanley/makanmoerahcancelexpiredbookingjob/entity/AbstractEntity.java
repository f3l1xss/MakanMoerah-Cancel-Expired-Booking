package com.felixstanley.makanmoerahcancelexpiredbookingjob.entity;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Felix
 */
@MappedSuperclass()
@Data
@EqualsAndHashCode
public class AbstractEntity {

  @Id
  private Integer id;

}
