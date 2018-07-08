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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationFilter implements Filter {
  private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationFilter.class);

  public void init(FilterConfig arg0) throws ServletException {

  }

  public void doFilter(ServletRequest req, ServletResponse resp,
                       FilterChain chain) throws IOException, ServletException {
    LOGGER.info("Do filter ");
    String path = ((HttpServletRequest) req).getRequestURI();
    showCookies(req, resp);
    if (path.contains("/assets") || path.contains(".js") || path.contains(".css") || path.contains(".png") || path
        .contains(".svg")) {
      LOGGER.info("Chain " + path);
      chain.doFilter(req, resp);
    }
    else {
      LOGGER.info("Go to index.html ");
      RequestDispatcher rd = req.getRequestDispatcher("/index.html");
      rd.forward(req, resp);
    }
  }

  private void showCookies(ServletRequest req, ServletResponse resp) {
    try {
      HttpServletRequest request = (HttpServletRequest) req;
      Cookie[] cookies = request.getCookies();
      Cookie original = findCookie(cookies, "PORTALWLJSESSIONID");
      Cookie ebSessionCookie = findCookie(cookies, "EBSESSIONID");
      if (ebSessionCookie != null) {
        LOGGER.info(String.format("Refresh: original {%s} ebsession{%s}", original.getValue(), ebSessionCookie.getValue
            ()));
        for (Cookie old : cookies) {
          if (old.getName().equalsIgnoreCase("EBSESSIONID")) {
            old.setMaxAge(0);
            old.setPath("/");
            old.setValue("");
            ((HttpServletResponse) resp).addCookie(old);
          }
        }
        addNew(resp, original.getValue());
      }
      else {
        LOGGER.info(String.format("Add new: original {%s} ", original.getValue()));
        addNew(resp, original.getValue());
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void addNew(ServletResponse resp, String value) {
    Cookie myCookie = new Cookie("EBSESSIONID", value);
    ((HttpServletResponse) resp).addCookie(myCookie);
  }

  private Cookie findCookie(Cookie[] cookies, String cookieName) {
    for (Cookie ck : cookies) {
      if (ck.getName().equalsIgnoreCase(cookieName)) {
        return ck;
      }
    }
    return null;
  }

  public void destroy() {
  }
}
