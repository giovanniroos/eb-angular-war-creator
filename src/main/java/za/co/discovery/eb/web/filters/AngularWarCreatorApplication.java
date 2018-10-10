package za.co.discovery.eb.web.filters;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/*
 * Copyright (c) Discovery Holdings Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Discovery Holdings Ltd ("Confidential Information").
 * It may not be copied or reproduced in any manner without the express
 * written permission of Discovery Holdings Ltd.
 *
 */
@SpringBootApplication
public class AngularWarCreatorApplication  extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    System.setProperty("spring.profiles.active", System.getProperty("environment"));
    return application.sources(AngularWarCreatorApplication.class);
  }
}
