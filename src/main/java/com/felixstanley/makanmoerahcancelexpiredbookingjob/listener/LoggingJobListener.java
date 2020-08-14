package com.felixstanley.makanmoerahcancelexpiredbookingjob.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * @author Felix
 */
@Slf4j
public class LoggingJobListener implements JobExecutionListener {

  @Override
  public void beforeJob(JobExecution jobExecution) {
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    log.info("Job {} which started on {}, ended on {} with batchStatus: {}, exitStatus: {}",
        jobExecution.getJobInstance().getJobName(), jobExecution.getStartTime(),
        jobExecution.getEndTime(), jobExecution.getStatus(), jobExecution.getExitStatus());
  }
}
