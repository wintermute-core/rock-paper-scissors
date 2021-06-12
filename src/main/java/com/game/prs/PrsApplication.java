package com.game.prs;

import com.game.prs.service.GameService;
import com.game.prs.service.GameServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class PrsApplication {

  public static void main(String[] args) {
    SpringApplication.run(PrsApplication.class, args);
  }



}
