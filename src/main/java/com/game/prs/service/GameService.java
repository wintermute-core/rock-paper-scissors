package com.game.prs.service;

import com.game.prs.game.Player;
import com.game.prs.game.Session;

/**
 * Service to maintain game sessions
 */
public interface GameService {

  /**
   * Create new game session
   */
  Session newSession(Player humanPlayer);

}
