package com.game.prs.model;

/**
 * States of game session
 */
public enum SessionState {

  INIT_SESSION,
  NEW_SESSION,
  READ_GAME_COUNT,
  READ_PLAYER1_INPUT,
  READ_PLAYER2_INPUT,
  SHOW_GAME_RESULT,
  SESSION_FINISHED,

  INVALID_PLAYER1_INPUT,
  INVALID_PLAYER2_INPUT

}
