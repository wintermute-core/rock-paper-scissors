package com.game.prs.service;

import com.game.prs.game.CpuPlayer;
import com.game.prs.game.HumanPlayer;
import com.game.prs.game.Player;
import com.game.prs.game.RulesImpl;
import com.game.prs.game.Session;
import com.game.prs.game.SessionListener;
import com.game.prs.model.SessionState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class AbstractGameServiceImpl implements GameService {

  @Autowired
  private StatisticsService statisticsService;

  @Override
  public Session newSession(Player humanPlayer) {

    Player player2 = new CpuPlayer();
    Session session = new Session(new RulesImpl(), humanPlayer, player2);

    session.subscribe(new SessionListener() {
      @Override
      public void update(Session session, SessionState oldState, SessionState newState) {
        statisticsService.countMetric(newState.toString());
        if (newState == SessionState.SESSION_FINISHED) {
          session.unsubscribe(this);
        }
      }
    });
    return session;
  }
}
