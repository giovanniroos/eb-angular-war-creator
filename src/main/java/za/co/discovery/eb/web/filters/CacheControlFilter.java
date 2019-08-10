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

import java.io.IOException;
import java.util.Date;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CacheControlFilter implements Filter {
  private static final Logger LOGGER = LoggerFactory.getLogger(CacheControlFilter.class);

  public void init(FilterConfig arg0) throws ServletException {

  }

  public void doFilter(ServletRequest request, ServletResponse response,
                       FilterChain chain) throws IOException, ServletException {
    LOGGER.info("Busting cache...");
    HttpServletResponse resp = (HttpServletResponse) response;
    resp.setHeader("Expires", "0");
    resp.setDateHeader("Last-Modified", new Date().getTime());
    resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");
    resp.setHeader("Pragma", "no-cache");

    chain.doFilter(request, response);
  }

  public void destroy() {
  }
}
