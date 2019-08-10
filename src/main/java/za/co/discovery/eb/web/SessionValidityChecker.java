package za.co.discovery.eb.web;

/*
 * Copyright (c) Discovery Holdings Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Discovery Holdings Ltd ("Confidential Information").
 * It may not be copied or reproduced in any manner without the express
 * written permission of Discovery Holdings Ltd.
 *
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import za.co.discovery.eb.domain.aps.SAProfileResponse;
import za.co.discovery.eb.web.filters.ApplicationFilter;

@Component
public class SessionValidityChecker {

  RestTemplate restTemplate = new RestTemplate();
  private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationFilter.class);

  @Value("${aps.url}")
  private String apsUrl;


  public boolean isSessionValid(String sessionId) {
    try {
      String url = apsUrl+sessionId;
      LOGGER.info("APS URL: {} ", url);
      ResponseEntity<SAProfileResponse> saProfileResponse = restTemplate.getForEntity(url, SAProfileResponse.class);
      if (!saProfileResponse.getBody().getSharedSession().getSharedSessionItem().isEmpty()) {
        LOGGER.info("{}", "Session is valid");
        return true;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return false;
  }
}
