package com.game.prs.model;

/**
 * States of game session
 */
public enum SessionState {

  INIT_SESSION,
  NEW_SESSION,
  READ_GAME_COUNT,
  READ_PLAYER_INPUT,
  SHOW_GAME_RESULT,
  SESSION_FINISHED,

  INVALID_PLAYER_INPUT

}
