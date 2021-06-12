package com.game.prs.game;

import com.game.prs.model.SessionState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SessionTest {

  @Mock
  private Player player1;

  @Mock
  private Player player2;

  @Mock
  private Rules rules;

  private Session session;

  @BeforeEach
  void init() {
    MockitoAnnotations.openMocks(this);
    session = new Session(rules, player1, player2);
  }

  @Test
  void initState() {
    Assertions.assertEquals(SessionState.INIT_SESSION, session.getSessionState());
  }

  @Test
  void initSwitchToNewSession() {
    session.update();
    Assertions.assertEquals(SessionState.NEW_SESSION, session.getSessionState());
  }

}
