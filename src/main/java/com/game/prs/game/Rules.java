package com.game.prs.game;

import com.game.prs.model.PlayerChoice;
import com.game.prs.model.WinResult;

/**
 * Paper Rock Scissors game rules.
 */
public interface Rules {

  /**
   * Evaluate game result based on player input
   */
  WinResult evaluate(PlayerChoice player1, PlayerChoice player2);

}
