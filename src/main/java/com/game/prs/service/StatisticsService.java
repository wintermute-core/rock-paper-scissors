package com.game.prs.service;

/**
 * Service to count different statistics
 */
public interface StatisticsService {

  /**
   * Count specific metric
   */
  void countMetric(String metricName);

}
