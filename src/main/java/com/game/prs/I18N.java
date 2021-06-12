package com.game.prs;

import com.game.prs.model.PlayerChoice;
import java.util.Arrays;

/**
 * Application texts
 */
public final class I18N {

  private I18N() {
  }

  public static final String WELCOME_MESSAGE = """
        Paper Rock Scissors game
      """;

  public static final String TCP_SERVER_LOG = """
        *** Paper Rock Scissors game TCP server %s ***
      """;

  public static final String TERMINAL_MESSAGE_LOG = """
        *** Paper Rock Scissors game terminal mode ***
      """;

  public static final String TERMINAL_WELCOME_MESSAGE = " *** Press Enter to start new game ***";

  public static final String PROMPT_GAME_COUNT = "Please enter number of games to be played: ";

  public static final String PROMPT_FAILED_TO_READ_GAME_COUNT = "Failed to parse entered value, expected to get a number";

  public static final String PROMPT_PLAYER_CHOICE = String.format("Choose one of %s:",
      Arrays.toString(PlayerChoice.values()));

  public static final String FAILED_TO_PARSE_INPUT = """
      Failed to parse entered value
      """;

  public static final String GAME_STATUS_LOG = """
      *** Game %s of %s ***
      """;

  public static final String OPPONENT_RESPONSE = """
      Opponent response: %s
      """;

  public static final String GAME_STATUS_PLAYER1_WON = """
      You won
      """;

  public static final String GAME_STATUS_PLAYER2_WON = """
      You lose
      """;

  public static final String GAME_STATUS_DRAW = """
      Draw
      """;

  public static final String SESSION_FINISHED = """
      *** Game session finished ***
      """;


}
