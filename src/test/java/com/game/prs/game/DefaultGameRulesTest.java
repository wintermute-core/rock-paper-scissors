package com.game.prs.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.game.prs.model.PlayerChoice;
import com.game.prs.model.WinResult;
import java.util.Arrays;
import java.util.List;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultGameRulesTest {

  static final List<Pair<PlayerChoice, PlayerChoice>> WIN_PAIRS = Arrays.asList(
      Pair.with(PlayerChoice.PAPER, PlayerChoice.ROCK),
      Pair.with(PlayerChoice.ROCK, PlayerChoice.SCISSORS),
      Pair.with(PlayerChoice.SCISSORS, PlayerChoice.PAPER)
  );

  private GameRules gameRules;

  @BeforeEach
  void init() {
    gameRules = new GameRulesImpl();
  }

  @Test
  void drawRulesEvaluation() {
    Arrays.stream(PlayerChoice.values()).forEach(input -> {
      assertEquals(WinResult.DRAW, gameRules.evaluate(input, input),
          String.format("Expected draw for inputs %s %s", input, input));
    });
  }

  @Test
  void player1WinEvaluation() {
    for (Pair<PlayerChoice, PlayerChoice> pair : WIN_PAIRS) {
      assertEquals(WinResult.PLAYER1, gameRules.evaluate(pair.getValue0(), pair.getValue1()),
          String.format("Expected player1 to win for %s", pair));
    }
  }

  @Test
  void player2WinEvaluation() {
    for (Pair<PlayerChoice, PlayerChoice> pair : WIN_PAIRS) {
      assertEquals(WinResult.PLAYER2, gameRules.evaluate(pair.getValue1(), pair.getValue0()),
          String.format("Expected player2 to win for %s", pair));
    }
  }

}
