package com.game.prs.game;

import com.game.prs.model.PlayerChoice;
import com.game.prs.model.SessionState;
import com.game.prs.model.WinResult;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Game session performed between 2 players
 */
@Slf4j
public class Session {

  @Getter
  private final Player player1;
  @Getter
  private final Player player2;

  private final Rules gameRules;

  @Getter
  private SessionState sessionState = SessionState.INIT_SESSION;

  @Getter
  private int totalGames;

  @Getter
  private int currentGame;

  @Getter
  private PlayerChoice player1Choice, player2Choice;

  @Getter
  private WinResult winResult;

  private Collection<SessionListener> sessionListeners = new CopyOnWriteArrayList<>();

  public Session(Rules gameRules, Player player1, Player player2) {
    this.gameRules = gameRules;
    this.player1 = player1;
    this.player2 = player2;
    this.currentGame = 0;
  }

  /**
   * Games state update
   */
  public void update() {
    switch (sessionState) {
      case INIT_SESSION -> changeState(SessionState.NEW_SESSION);
      case NEW_SESSION -> changeState(SessionState.READ_GAME_COUNT);
      case READ_GAME_COUNT -> { // read from player number for games
        int gameCount = player1.fetchGameCount();
        if (gameCount <= 0) {
          changeState(SessionState.INVALID_PLAYER1_INPUT);
          changeState(SessionState.READ_GAME_COUNT);
          return;
        }
        totalGames = gameCount;
        changeState(SessionState.READ_PLAYER1_INPUT);
      }
      case READ_PLAYER1_INPUT -> {
        PlayerChoice playerChoice = player1.fetchPlayerChoice();
        if (playerChoice == null) {
          changeState(SessionState.INVALID_PLAYER1_INPUT);
          changeState(SessionState.READ_PLAYER1_INPUT);
        }
        player1Choice = playerChoice;
        changeState(SessionState.READ_PLAYER1_INPUT);
      }
      case READ_PLAYER2_INPUT -> {
        PlayerChoice playerChoice = player2.fetchPlayerChoice();
        if (playerChoice == null) {
          changeState(SessionState.INVALID_PLAYER2_INPUT);
          changeState(SessionState.READ_PLAYER2_INPUT);
        }
        player2Choice = playerChoice;
        winResult = gameRules.evaluate(player1Choice, player2Choice);
        changeState(SessionState.SHOW_GAME_RESULT);
      }
      case SHOW_GAME_RESULT -> changeState(SessionState.SESSION_FINISHED);
      case SESSION_FINISHED -> {
        currentGame++;
        if (currentGame > totalGames) {
          changeState(SessionState.SESSION_FINISHED);
          return;
        }
        changeState(SessionState.READ_PLAYER1_INPUT);
      }
      case INVALID_PLAYER1_INPUT -> changeState(SessionState.READ_PLAYER1_INPUT);
      case INVALID_PLAYER2_INPUT -> changeState(SessionState.READ_PLAYER2_INPUT);
    }
  }

  void changeState(SessionState newState) {
    if (sessionState != newState) {
      sessionListeners.forEach(listener -> listener.update(this, this.getSessionState(), newState));
    }
    this.sessionState = newState;
  }

  public void subscribe(SessionListener listener) {
    sessionListeners.add(listener);
  }

  public void unsubscribe(SessionListener listener) {
    sessionListeners.remove(listener);
  }

}
