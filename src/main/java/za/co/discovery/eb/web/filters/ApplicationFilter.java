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
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class ApplicationFilter implements Filter {
  public void init(FilterConfig arg0) throws ServletException {

  }

  public void doFilter(ServletRequest req, ServletResponse resp,
                       FilterChain chain) throws IOException, ServletException {
    String path = ((HttpServletRequest) req).getRequestURI();
    if (path.contains("/assets") || path.contains(".js") || path.contains(".css") || path.contains(".png") || path
        .contains(".svg")) {
      chain.doFilter(req, resp);
    }
    else {
      RequestDispatcher rd = req.getRequestDispatcher("/index.html");
      rd.forward(req, resp);
    }
  }

  public void destroy() {
  }
}
