package com.felixstanley.makanmoerahcancelexpiredbookingjob.tasklet;

import com.felixstanley.makanmoerahcancelexpiredbookingjob.constant.Constants;
import com.felixstanley.makanmoerahcancelexpiredbookingjob.dao.UsersDao;
import com.felixstanley.makanmoerahcancelexpiredbookingjob.entity.Users;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;

/**
 * @author Felix
 */
@AllArgsConstructor
@Slf4j
public class DisableUsersTasklet implements Tasklet {

  private UsersDao usersDao;
  private Integer usersId;
  private FindByIndexNameSessionRepository findByIndexNameSessionRepository;

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {
    // Check If Existing Users has exceeded max show count, DISABLE Users if he does
    Users existingUsers = usersDao.findById(usersId).orElse(null);
    if (existingUsers.getNoShowCount() > Constants.MAXIMUM_NO_SHOW_COUNT) {
      log.info("Disabling Users {} as he has exceeded Max No Show Count of {}", existingUsers,
          Constants.MAXIMUM_NO_SHOW_COUNT);
      // Delete all his sessions
      Map<String, Session> sessionMap = findByIndexNameSessionRepository
          .findByPrincipalName(existingUsers.getEmail());
      sessionMap.keySet().stream()
          .forEach(sessionId -> findByIndexNameSessionRepository.deleteById(sessionId));
      // Disable Users
      existingUsers.setEnabled(false);
      usersDao.saveAndFlush(existingUsers);
    }
    return RepeatStatus.FINISHED;
  }
}
