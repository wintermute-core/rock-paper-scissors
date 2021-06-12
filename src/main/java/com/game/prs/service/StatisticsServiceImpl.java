package com.game.prs.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {

  @Autowired
  private MeterRegistry meterRegistry;

  private Map<String, Counter> counters;

  @PostConstruct
  public void init() {
    counters = new ConcurrentHashMap<>();
  }

  @Override
  public void countMetric(String metricName) {
    if (!counters.containsKey(metricName)) {
      Counter counter = Counter.builder(metricName)
          .register(meterRegistry);
      counters.put(metricName, counter);
    }

    counters.get(metricName).increment();
  }
}
