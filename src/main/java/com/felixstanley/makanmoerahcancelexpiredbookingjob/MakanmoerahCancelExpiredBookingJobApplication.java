package com.felixstanley.makanmoerahcancelexpiredbookingjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude = {RedisRepositoriesAutoConfiguration.class})
public class MakanmoerahCancelExpiredBookingJobApplication {

  public static void main(String[] args) {
    // Need to explicitly exit after job execution since we have Starter Web Dependency
    // to autowire FindByIndexNameSessionRepository and Spring Batch won't automatically shutdown
    // with Starter Web Dependency
    ConfigurableApplicationContext configurableApplicationContext = SpringApplication
        .run(MakanmoerahCancelExpiredBookingJobApplication.class, args);
    System.exit(SpringApplication.exit(configurableApplicationContext));
  }

}
