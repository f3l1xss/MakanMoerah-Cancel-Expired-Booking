package com.felixstanley.makanmoerahcancelexpiredbookingjob.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Data;
import lombok.ToString;

/**
 * @author Felix
 */
@Entity
@Data
@ToString(callSuper = true)
public class Users extends AbstractEntity implements Serializable {

  private String email;

  private String name;

  @Column(name = "no_show_count")
  private Short noShowCount;

  private Boolean enabled;

}
