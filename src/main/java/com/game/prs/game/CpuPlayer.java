package com.game.prs.game;

import com.game.prs.model.PlayerChoice;
import java.util.Random;

/**
 * CPU player which return random value for selection
 */
public class CpuPlayer implements Player {

  private final Random random = new Random();

  @Override
  public int fetchGameCount() {
    throw new RuntimeException("Not supported");
  }

  @Override
  public PlayerChoice fetchPlayerChoice() {
    PlayerChoice[] values = PlayerChoice.values();
    return values[random.nextInt(values.length)];
  }

}
