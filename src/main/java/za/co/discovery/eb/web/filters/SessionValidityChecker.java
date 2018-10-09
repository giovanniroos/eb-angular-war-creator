package za.co.discovery.eb.web.filters;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import za.co.discovery.eb.domain.aps.SAProfileResponse;

public class SessionValidityChecker {

  RestTemplate restTemplate = new RestTemplate();
  private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationFilter.class);

  public boolean isSessionValid(String sessionId){
    try{
      ResponseEntity<SAProfileResponse> saProfileResponse = restTemplate.getForEntity
          ("https://employeebenefitsqa/eb-sl-aps/v1/sa-profile?sessionID="+sessionId, SAProfileResponse.class);

      if(!saProfileResponse.getBody().getSharedSession().getSharedSessionItem().isEmpty()){
        LOGGER.info("{}","Session is valid");
        return true;
      }
    }catch(Exception ex){
      ex.printStackTrace();
    }

    return false;
  }
}
