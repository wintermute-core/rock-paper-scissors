package com.game.prs.game;

import com.game.prs.model.PlayerChoice;
import com.game.prs.model.SessionState;
import com.game.prs.model.WinResult;
import com.google.common.annotations.VisibleForTesting;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Game session performed between 2 players
 * <p>
 * Instance maintain current game state and implement basic game flow.
 */
@Slf4j
public class Session {

  @Getter
  private final Player player1;
  @Getter
  private final Player player2;

  private final Rules rules;

  @Getter
  private SessionState sessionState = SessionState.INIT_SESSION;

  @Getter
  private int totalGames = 0;

  @Getter
  private int currentGame = 0;

  @Getter
  private PlayerChoice player1Choice;

  @Getter
  private PlayerChoice player2Choice;

  @Getter
  private WinResult winResult;

  private final Collection<SessionListener> sessionListeners = new CopyOnWriteArrayList<>();

  public Session(Rules gameRules, Player player1, Player player2) {
    this.rules = gameRules;
    this.player1 = player1;
    this.player2 = player2;
  }

  /**
   * Games state update
   */
  public void update() {
    switch (sessionState) {
      case INIT_SESSION -> changeState(SessionState.NEW_SESSION);
      case NEW_SESSION -> changeState(SessionState.READ_GAME_COUNT);
      case READ_GAME_COUNT -> {
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
        var playerChoice = player1.fetchPlayerChoice();
        if (playerChoice == null) {
          changeState(SessionState.INVALID_PLAYER1_INPUT);
          changeState(SessionState.READ_PLAYER1_INPUT);
          return;
        }
        player1Choice = playerChoice;
        changeState(SessionState.READ_PLAYER2_INPUT);
      }
      case READ_PLAYER2_INPUT -> {
        var playerChoice = player2.fetchPlayerChoice();
        if (playerChoice == null) {
          changeState(SessionState.INVALID_PLAYER2_INPUT);
          changeState(SessionState.READ_PLAYER2_INPUT);
          return;
        }
        player2Choice = playerChoice;
        changeState(SessionState.SHOW_GAME_RESULT);
      }
      case SHOW_GAME_RESULT -> {
        winResult = rules.evaluate(player1Choice, player2Choice);
        changeState(SessionState.SESSION_FINISHED);
      }
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
      sessionListeners.forEach(listener -> {
        try {
          listener.update(this, this.getSessionState(), newState);
        } catch (Exception e) {
          log.error("Notification exception", e);
        }
      });
    }
    this.sessionState = newState;
  }

  public void subscribe(SessionListener listener) {
    sessionListeners.add(listener);
  }

  public void unsubscribe(SessionListener listener) {
    sessionListeners.remove(listener);
  }

  @VisibleForTesting
  void setSessionState(SessionState sessionState) {
    this.sessionState = sessionState;
  }

  @VisibleForTesting
  public void setTotalGames(int totalGames) {
    this.totalGames = totalGames;
  }

  @VisibleForTesting
  public void setCurrentGame(int currentGame) {
    this.currentGame = currentGame;
  }
}
