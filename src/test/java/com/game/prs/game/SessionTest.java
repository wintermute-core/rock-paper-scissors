package com.game.prs.game;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.game.prs.model.PlayerChoice;
import com.game.prs.model.SessionState;
import com.game.prs.model.WinResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.hamcrest.Matchers;
import org.javatuples.Triplet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class SessionTest {

  @Mock
  private Player player1;

  @Mock
  private Player player2;

  @Mock
  private Rules rules;

  private Session session;

  private List<SessionState> sessionStates;

  @BeforeEach
  void init() {
    MockitoAnnotations.openMocks(this);
    session = new Session(rules, player1, player2);

    sessionStates = new ArrayList<>();
    session.subscribe((session, oldState, newState) -> sessionStates.add(newState));
  }

  @Test
  void initState() {
    assertEquals(SessionState.INIT_SESSION, session.getSessionState());
  }

  @Test
  void initSwitchToNewSession() {
    AtomicReference<Boolean> switchExecuted = subscribe(SessionState.INIT_SESSION,
        SessionState.NEW_SESSION);
    session.update();

    assertEquals(SessionState.NEW_SESSION, session.getSessionState());
    assertTrue(switchExecuted.get());
  }

  @Test
  void switchToReadCount() {
    session.setSessionState(SessionState.NEW_SESSION);
    AtomicReference<Boolean> switchExecuted = subscribe(SessionState.NEW_SESSION,
        SessionState.READ_GAME_COUNT);
    session.update();

    assertEquals(SessionState.READ_GAME_COUNT, session.getSessionState());
    assertTrue(switchExecuted.get());
  }

  @Test
  void readPlayer1InvalidGameCount() {
    session.setSessionState(SessionState.READ_GAME_COUNT);
    AtomicReference<Boolean> switchExecuted = subscribe(SessionState.READ_GAME_COUNT,
        SessionState.INVALID_PLAYER1_INPUT);

    Mockito.when(player1.fetchGameCount()).thenReturn(0);

    session.update();

    Mockito.verify(player1).fetchGameCount();
    assertEquals(SessionState.READ_GAME_COUNT, session.getSessionState());
    assertTrue(switchExecuted.get());
    assertThat(sessionStates, Matchers.everyItem(Matchers.isIn(Arrays
        .asList(SessionState.NEW_SESSION, SessionState.READ_GAME_COUNT,
            SessionState.INVALID_PLAYER1_INPUT, SessionState.READ_GAME_COUNT))));
  }

  @Test
  void readPlayer1GameCount() {
    session.setSessionState(SessionState.READ_GAME_COUNT);
    AtomicReference<Boolean> switchExecuted = subscribe(SessionState.READ_GAME_COUNT,
        SessionState.READ_PLAYER1_INPUT);

    Mockito.when(player1.fetchGameCount()).thenReturn(5);

    session.update();

    Mockito.verify(player1).fetchGameCount();
    assertEquals(5, session.getTotalGames());
    assertEquals(SessionState.READ_PLAYER1_INPUT, session.getSessionState());
    assertTrue(switchExecuted.get());
    assertThat(sessionStates, Matchers.everyItem(Matchers.isIn(Arrays
        .asList(SessionState.NEW_SESSION, SessionState.READ_GAME_COUNT,
            SessionState.READ_PLAYER1_INPUT))));
  }

  @Test
  void player1InputInvalid() {
    session.setSessionState(SessionState.READ_PLAYER1_INPUT);
    Mockito.when(player1.fetchPlayerChoice()).thenReturn(null);

    AtomicReference<Boolean> switchExecuted = subscribe(SessionState.READ_PLAYER1_INPUT,
        SessionState.INVALID_PLAYER1_INPUT);

    session.update();

    Mockito.verify(player1).fetchPlayerChoice();

    assertEquals(SessionState.READ_PLAYER1_INPUT, session.getSessionState());
    assertTrue(switchExecuted.get());
    assertThat(sessionStates, Matchers.everyItem(Matchers.isIn(Arrays
        .asList(
            SessionState.INVALID_PLAYER1_INPUT, SessionState.READ_PLAYER1_INPUT))));
  }

  @Test
  void player2InputInvalid() {
    session.setSessionState(SessionState.READ_PLAYER2_INPUT);
    Mockito.when(player2.fetchPlayerChoice()).thenReturn(null);

    AtomicReference<Boolean> switchExecuted = subscribe(SessionState.READ_PLAYER2_INPUT,
        SessionState.INVALID_PLAYER2_INPUT);

    session.update();

    Mockito.verify(player2).fetchPlayerChoice();

    assertEquals(SessionState.READ_PLAYER2_INPUT, session.getSessionState());
    assertTrue(switchExecuted.get());
    assertThat(sessionStates, Matchers.everyItem(Matchers.isIn(Arrays
        .asList(
            SessionState.INVALID_PLAYER2_INPUT, SessionState.READ_PLAYER2_INPUT))));
  }

  @Test
  void player1Input() {
    session.setSessionState(SessionState.READ_PLAYER1_INPUT);
    Mockito.when(player1.fetchPlayerChoice()).thenReturn(PlayerChoice.PAPER);

    AtomicReference<Boolean> switchExecuted = subscribe(SessionState.READ_PLAYER1_INPUT,
        SessionState.READ_PLAYER2_INPUT);

    session.update();

    Mockito.verify(player1).fetchPlayerChoice();

    assertEquals(PlayerChoice.PAPER, session.getPlayer1Choice());
    assertEquals(SessionState.READ_PLAYER2_INPUT, session.getSessionState());
    assertTrue(switchExecuted.get());
  }

  @Test
  void player2Input() {
    session.setSessionState(SessionState.READ_PLAYER2_INPUT);
    Mockito.when(player2.fetchPlayerChoice()).thenReturn(PlayerChoice.ROCK);

    AtomicReference<Boolean> switchExecuted = subscribe(SessionState.READ_PLAYER2_INPUT,
        SessionState.SHOW_GAME_RESULT);

    session.update();

    Mockito.verify(player2).fetchPlayerChoice();

    assertEquals(PlayerChoice.ROCK, session.getPlayer2Choice());
    assertEquals(SessionState.SHOW_GAME_RESULT, session.getSessionState());
    assertTrue(switchExecuted.get());

  }

  @Test
  void gameResult() {
    session.setSessionState(SessionState.SHOW_GAME_RESULT);
    session.setPlayer1Choice(PlayerChoice.PAPER);
    session.setPlayer2Choice(PlayerChoice.ROCK);

    Mockito.when(rules.evaluate(PlayerChoice.PAPER, PlayerChoice.ROCK)).thenReturn(WinResult.DRAW);

    AtomicReference<Boolean> switchExecuted = subscribe(SessionState.SHOW_GAME_RESULT,
        SessionState.GAME_FINISHED);

    session.update();

    Mockito.verify(rules).evaluate(PlayerChoice.PAPER, PlayerChoice.ROCK);

    assertEquals(1, session.getHistory().size());
    Triplet<PlayerChoice, PlayerChoice, WinResult> result = session.getHistory().get(0);
    assertEquals(PlayerChoice.PAPER, result.getValue0());
    assertEquals(PlayerChoice.ROCK, result.getValue1());
    assertEquals(WinResult.DRAW, result.getValue2());

    assertEquals(WinResult.DRAW, session.getWinResult());
    assertEquals(SessionState.GAME_FINISHED, session.getSessionState());
    assertTrue(switchExecuted.get());
  }

  @Test
  void gameRestart() {
    session.setTotalGames(666);
    session.setCurrentGame(1);
    session.setSessionState(SessionState.GAME_FINISHED);

    AtomicReference<Boolean> switchExecuted = subscribe(SessionState.GAME_FINISHED,
        SessionState.READ_PLAYER1_INPUT);

    session.update();

    assertEquals(SessionState.READ_PLAYER1_INPUT, session.getSessionState());
    assertTrue(switchExecuted.get());
    assertEquals(2, session.getCurrentGame());
  }

  @Test
  void sessionTermination() {
    session.setTotalGames(666);
    session.setCurrentGame(666);
    session.setSessionState(SessionState.GAME_FINISHED);

    AtomicReference<Boolean> switchExecuted = subscribe(SessionState.GAME_FINISHED,
        SessionState.SESSION_FINISHED);

    session.update();

    assertEquals(SessionState.SESSION_FINISHED, session.getSessionState());
    assertTrue(switchExecuted.get());
    assertEquals(666, session.getCurrentGame());
  }

  @Test
  void invalidInputPlayer1() {
    session.setSessionState(SessionState.INVALID_PLAYER1_INPUT);

    AtomicReference<Boolean> switchExecuted = subscribe(SessionState.INVALID_PLAYER1_INPUT,
        SessionState.READ_PLAYER1_INPUT);

    session.update();

    assertEquals(SessionState.READ_PLAYER1_INPUT, session.getSessionState());
    assertTrue(switchExecuted.get());
  }

  @Test
  void invalidInputPlayer2() {
    session.setSessionState(SessionState.INVALID_PLAYER2_INPUT);

    AtomicReference<Boolean> switchExecuted = subscribe(SessionState.INVALID_PLAYER2_INPUT,
        SessionState.READ_PLAYER2_INPUT);

    session.update();

    assertEquals(SessionState.READ_PLAYER2_INPUT, session.getSessionState());
    assertTrue(switchExecuted.get());
  }

  private AtomicReference<Boolean> subscribe(SessionState subOld, SessionState subNew) {
    AtomicReference<Boolean> switchExecuted = new AtomicReference<>(false);
    session.subscribe((session, oldState, newState) -> {
      if (oldState == subOld && newState == subNew) {
        switchExecuted.set(true);
      }
    });
    return switchExecuted;
  }

}
