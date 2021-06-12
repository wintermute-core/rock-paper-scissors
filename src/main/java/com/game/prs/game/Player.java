package com.game.prs.game;

import com.game.prs.model.PlayerChoice;

/**
 * Game player
 */
public interface Player extends SessionListener {

  /**
   * Fetch game count
   */
  int fetchGameCount();

  /**
   * Fetch player choice
   */
  PlayerChoice fetchPlayerChoice();


}
