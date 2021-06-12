package com.game.prs.game;

import com.game.prs.model.SessionState;

/**
 * Listener for session status update.
 */
public interface SessionListener {

  /**
   * Notification of session status update
   */
  void update(Session session, SessionState oldState, SessionState newState);

}
