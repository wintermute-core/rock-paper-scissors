package com.game.prs.service;

import static com.game.prs.I18N.TERMINAL_MESSAGE_LOG;
import static com.game.prs.I18N.TERMINAL_WELCOME_MESSAGE;

import com.game.prs.game.HumanPlayer;
import com.game.prs.game.Session;
import com.game.prs.model.SessionState;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConsoleGameService extends AbstractGameServiceImpl {

  @Value("${game.console.enabled:false}")
  private boolean enabledConsoleMode;

  @Autowired
  private ThreadPoolTaskScheduler taskScheduler;

  @Override
  @PostConstruct
  public void start() {
    if (!enabledConsoleMode) {
      return;
    }

    taskScheduler.execute(terminalGame());
  }

  private Runnable terminalGame() {
    return () -> {
      log.info(TERMINAL_MESSAGE_LOG);
      BufferedReader inputReader = new BufferedReader(
          new InputStreamReader(System.in));
      while(true) {
        System.out.println(TERMINAL_WELCOME_MESSAGE);
        try {
          if (inputReader.readLine() == null) {
            log.warn("Failed to access terminal");
            break;
          }
        } catch (IOException e) {
          log.error("Failed to read line", e);
          break;
        }
        HumanPlayer humanPlayer = new HumanPlayer(
            System.in, System.out
        );

        Session session = newSession(humanPlayer);
        while (session.getSessionState() != SessionState.SESSION_FINISHED) {
          session.update();
        }
        session.clearSubscriptions();

      }

    };
  }

}
