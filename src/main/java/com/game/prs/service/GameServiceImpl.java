package com.game.prs.service;

import com.game.prs.game.CpuPlayer;
import com.game.prs.game.HumanPlayer;
import com.game.prs.game.Player;
import com.game.prs.game.RulesImpl;
import com.game.prs.game.Session;
import com.game.prs.game.SessionListener;
import com.game.prs.model.SessionState;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GameServiceImpl implements GameService {

  @PostConstruct
  public void init() {
    System.out.println("Game service init");
    HumanPlayer humanPlayer = new HumanPlayer(
        System.in, System.out
    );
    Session session = newSession(humanPlayer);
    session.subscribe(humanPlayer);
    while (session.getSessionState() != SessionState.SESSION_FINISHED) {
      session.update();
    }
  }

  @Override
  public Session newSession(Player humanPlayer) {
    Session session = new Session(new RulesImpl(), humanPlayer, new CpuPlayer());
    session.subscribe(new SessionListener() {
      @Override
      public void update(Session session, SessionState oldState, SessionState newState) {
        log.info("Session " + session.hashCode() + " New session: " + newState);
      }
    });
    return session;
  }
}
