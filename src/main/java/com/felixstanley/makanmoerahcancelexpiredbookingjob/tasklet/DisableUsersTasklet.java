package com.felixstanley.makanmoerahcancelexpiredbookingjob.tasklet;

import com.felixstanley.makanmoerahcancelexpiredbookingjob.constant.Constants;
import com.felixstanley.makanmoerahcancelexpiredbookingjob.dao.UsersDao;
import com.felixstanley.makanmoerahcancelexpiredbookingjob.entity.Users;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * @author Felix
 */
@AllArgsConstructor
@Slf4j
public class DisableUsersTasklet implements Tasklet {

  private UsersDao usersDao;
  private Integer usersId;

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {
    // Check If Existing Users has exceeded max show count, DISABLE Users if he does
    Users existingUsers = usersDao.findById(usersId).orElse(null);
    if (existingUsers.getNoShowCount() > Constants.MAXIMUM_NO_SHOW_COUNT) {
      log.info("Disabling Users {} as he has exceeded Max No Show Count of {}", existingUsers,
          Constants.MAXIMUM_NO_SHOW_COUNT);
      existingUsers.setEnabled(false);
      usersDao.saveAndFlush(existingUsers);
    }
    return RepeatStatus.FINISHED;
  }
}
