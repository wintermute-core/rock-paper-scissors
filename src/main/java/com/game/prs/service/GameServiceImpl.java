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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GameServiceImpl implements GameService {

  @Autowired
  private StatisticsService statisticsService;

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

    Player player2 = new CpuPlayer();
    Session session = new Session(new RulesImpl(), humanPlayer, player2);

    session.subscribe(new SessionListener() {
      @Override
      public void update(Session session, SessionState oldState, SessionState newState) {
        statisticsService.countMetric(newState.toString());
      }
    });
    return session;
  }
}
