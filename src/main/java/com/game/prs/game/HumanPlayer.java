package com.game.prs.game;

import com.game.prs.model.PlayerChoice;
import com.game.prs.model.SessionState;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

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
    while(true) {
      printOutput("Please enter number of games to be played:");
      try {
        return Integer.parseInt(bufferedReader.readLine().trim());
      } catch (IOException e) {
        printOutput("Failed to parse entered value, expected to get a number");
        log.error("Failed to read input", e);
        continue;
      }
    }
  }

  @Override
  public PlayerChoice fetchPlayerChoice() {
    while (true) {
      printOutput(String.format("Please enter number of games to be played %s:",
          Arrays.toString(PlayerChoice.values())));
      try {
        return PlayerChoice.valueOf(bufferedReader.readLine().trim());
      } catch (IOException e) {
        printOutput("Failed to parse entered value, expected to get a number");
        log.error("Failed to read input", e);
      }
    }
  }

  private void printOutput(String message) {
    try {
      bufferedOutput.write((message + "\n").getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      log.error("Failed to print message", e);
    }
  }

  @Override
  public void update(Session session, SessionState oldState, SessionState newState) {

    printOutput("Game state switched to " + newState);
    switch (newState) {
      case SESSION_FINISHED -> {
        printOutput("Game session finished");

        try {
          bufferedReader.close();
        } catch (IOException e) {
          log.error("Failed to close reader", e);
        }
        try {
          bufferedOutput.close();
        } catch (IOException e) {
          log.error("Failed to close output");
        }

      }
    }


  }
}
