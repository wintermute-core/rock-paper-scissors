package com.game.prs.game;

import static com.game.prs.I18N.FAILED_TO_PARSE_INPUT;
import static com.game.prs.I18N.GAME_STATUS_DRAW;
import static com.game.prs.I18N.GAME_STATUS_LOG;
import static com.game.prs.I18N.GAME_STATUS_PLAYER1_WON;
import static com.game.prs.I18N.GAME_STATUS_PLAYER2_WON;
import static com.game.prs.I18N.OPPONENT_RESPONSE;
import static com.game.prs.I18N.PROMPT_FAILED_TO_READ_GAME_COUNT;
import static com.game.prs.I18N.PROMPT_GAME_COUNT;
import static com.game.prs.I18N.PROMPT_PLAYER_CHOICE;
import static com.game.prs.I18N.SESSION_FINISHED;

import com.game.prs.model.PlayerChoice;
import com.game.prs.model.SessionState;
import com.game.prs.model.WinResult;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.javatuples.Triplet;

/**
 * Human player, interact with game through IO streams.
 */
@Slf4j
public class HumanPlayer implements Player, SessionListener {

  private final BufferedReader bufferedReader;

  private final BufferedOutputStream bufferedOutput;

  public HumanPlayer(InputStream inputStream, OutputStream outputStream) {
    bufferedReader = new BufferedReader(
        new InputStreamReader(inputStream));
    bufferedOutput = new BufferedOutputStream(outputStream);
  }

  @Override
  public int fetchGameCount() {
    while (true) {
      printOutput(PROMPT_GAME_COUNT);
      try {
        String value = bufferedReader.readLine();
        if (StringUtils.isNotBlank(value)) {
          return Integer.parseInt(value.trim());
        }
      } catch (NumberFormatException | IOException e) {
        printOutput(PROMPT_FAILED_TO_READ_GAME_COUNT);
        log.error("Failed to read input", e);
      }
    }
  }

  @Override
  public PlayerChoice fetchPlayerChoice() {
    while (true) {
      printOutput(PROMPT_PLAYER_CHOICE);
      try {
        String value = bufferedReader.readLine();
        if (StringUtils.isNotBlank(value)) {
          return PlayerChoice.valueOf(value.trim().toUpperCase(Locale.ROOT));
        }
      } catch (IllegalArgumentException | IOException e) {
        printOutput(FAILED_TO_PARSE_INPUT);
        log.error("Failed to read input", e);
      }
    }
  }

  private void printOutput(String message) {
    try {
      bufferedOutput.write((message + "\n").getBytes(StandardCharsets.UTF_8));
      bufferedOutput.flush();
    } catch (IOException e) {
      log.error("Failed to print message", e);
    }
  }

  @Override
  public void update(Session session, SessionState oldState, SessionState newState) {

    switch (newState) {
      case READ_PLAYER1_INPUT -> printOutput(
          String.format(GAME_STATUS_LOG, session.getCurrentGame(), session.getTotalGames()));
      case SINGLE_GAME_FINISHED -> {
        Triplet<PlayerChoice, PlayerChoice, WinResult> status = session.lastResult().get();
        printOutput(String.format(OPPONENT_RESPONSE, status.getValue1()));
        switch (status.getValue2()) {
          case PLAYER1 -> printOutput(GAME_STATUS_PLAYER1_WON);
          case PLAYER2 -> printOutput(GAME_STATUS_PLAYER2_WON);
          case DRAW -> printOutput(GAME_STATUS_DRAW);
        }
      }
      case SESSION_FINISHED -> {
        printOutput(SESSION_FINISHED);

      }
    }


  }
}
