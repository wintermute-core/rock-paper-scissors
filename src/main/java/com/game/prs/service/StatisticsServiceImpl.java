package com.game.prs.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {

  private Map<String, AtomicLong> counters;

  @PostConstruct
  public void init() {
    counters = new ConcurrentHashMap<>();
  }

  @Override
  public void countMetric(String metricName) {
    if (!counters.containsKey(metricName)) {
      counters.put(metricName, new AtomicLong(0));
    }
    counters.get(metricName).incrementAndGet();
  }
}
