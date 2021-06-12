package com.game.prs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
@ComponentScan
public class PrsApplication {

  public static void main(String[] args) {
    SpringApplication.run(PrsApplication.class, args);
  }

  @Bean
  public ThreadPoolTaskScheduler threadPoolTaskScheduler(@Value("${game.threads:2}") int threads) {
    ThreadPoolTaskScheduler threadPoolTaskScheduler
        = new ThreadPoolTaskScheduler();
    threadPoolTaskScheduler.setPoolSize(threads);
    threadPoolTaskScheduler.setThreadNamePrefix(
        "prs");

    return threadPoolTaskScheduler;
  }

}
