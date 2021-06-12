package com.game.prs.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.game.prs.I18N;
import com.game.prs.model.SessionState;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test which simulate play session
 */
class PlayerSessionTest {

  private static final String PLAYER_INPUT = "2\nROCK\nPAPER\n";

  private static final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

  private final Player player1 = new HumanPlayer(new ByteArrayInputStream(PLAYER_INPUT.getBytes(StandardCharsets.UTF_8)), outputStream);

  private Session session;

  @BeforeEach
  void init() throws IOException {
    session = new Session(new RulesImpl(), player1, new CpuPlayer());
    outputStream.reset();
  }

  @Test
  void gameLoopEvaluation() {
    int loopId = 0;
    while (loopId < 100) {
      session.update();
      if (session.getSessionState() == SessionState.SESSION_FINISHED) {
        break;
      }
      loopId++;
    }

    assertEquals(SessionState.SESSION_FINISHED, session.getSessionState());
    assertEquals(2, session.getTotalGames());
    assertEquals(2, session.getCurrentGame());

    String output = outputStream.toString();
    assertTrue(output.contains(I18N.SESSION_FINISHED));

  }

}
