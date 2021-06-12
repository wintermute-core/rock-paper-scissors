package com.game.prs.game;

import com.game.prs.model.PlayerChoice;
import com.game.prs.model.WinResult;

/**
 * Implementation of game rules in java code.
 */
public class GameRulesImpl implements GameRules {

  @Override
  public WinResult evaluate(PlayerChoice player1, PlayerChoice player2) {

    if (player1 == player2) {
      return WinResult.DRAW;
    }

    if (player1 == PlayerChoice.PAPER && player2 == PlayerChoice.ROCK) {
      return WinResult.PLAYER1;
    }
    if (player1 == PlayerChoice.ROCK && player2 == PlayerChoice.SCISSORS) {
      return WinResult.PLAYER1;
    }

    if (player1 == PlayerChoice.SCISSORS && player2 == PlayerChoice.PAPER) {
      return WinResult.PLAYER1;
    }

    return WinResult.PLAYER2;
  }

}
