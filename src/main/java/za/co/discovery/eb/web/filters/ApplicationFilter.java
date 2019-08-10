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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import za.co.discovery.eb.web.SessionValidityChecker;

@Component
public class ApplicationFilter implements Filter {
  private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationFilter.class);

  @Autowired
  SessionValidityChecker sessionValidityChecker;

  @Value("${login.url}")
  private String loginUrl;

  public void init(FilterConfig arg0) throws ServletException {

  }

  public void doFilter(ServletRequest req, ServletResponse resp,
                       FilterChain chain) throws IOException, ServletException {
    String path = ((HttpServletRequest) req).getRequestURI();
    String sessionId = copyCookies(req, resp, path);

    LOGGER.info("Checker...{} Session...{} ", sessionValidityChecker, sessionId);
    boolean sessionValid = true; //sessionValidityChecker.isSessionValid(sessionId);

    LOGGER.info("Session Valid... {} ", sessionValid);

    if ((sessionId != null && !sessionId.isEmpty()) && !sessionValid) {
      LOGGER.info("Redirecting to login");
      ((HttpServletResponse) resp).sendRedirect(loginUrl);
    }
    else {
      if (path.contains("/assets") || path.contains(".js") || path.contains(".css") || path.contains(".png") || path
          .contains(".svg")) {
        chain.doFilter(req, resp);
      }
      else {
        String page = (sessionValid) ? "index.html" : "session-expired.html";
        RequestDispatcher rd = req.getRequestDispatcher("/" + page);
        rd.forward(req, resp);
      }
    }
  }

  private String copyCookies(ServletRequest req, ServletResponse resp, String path) {
    String sessionId = "";
    try {
      HttpServletRequest request = (HttpServletRequest) req;
      Cookie[] cookies = request.getCookies();
      Cookie original = findCookie(cookies, "PORTALWLJSESSIONID");
      Cookie ebSessionCookie = findCookie(cookies, "EBSESSIONID");

      if (ebSessionCookie != null) {
        for (Cookie old : cookies) {
          if (old.getName().equalsIgnoreCase("EBSESSIONID")) {
            old.setMaxAge(0);
            old.setPath("/");
            old.setValue("");
            ((HttpServletResponse) resp).addCookie(old);
          }
        }
        addNew(resp, original.getValue(), path);
      }
      else {
        if (original != null) {
          addNew(resp, original.getValue(), path);
        }
      }
      sessionId = (original != null) ? original.getValue() : "";
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return sessionId;
  }

  private void addNew(ServletResponse resp, String value, String path) {
    Cookie myCookie = new Cookie("EBSESSIONID", value);
    if (path.contains("eb-web-broker-zone")) {
      myCookie.setPath("/eb-web-broker-zone");
    }
    else if (path.contains("eb-web-member-zone")) {
      myCookie.setPath("/eb-web-member-zone");
    }
    else {
      myCookie.setPath("/eb-web-employer-zone/employer-onboarding");
    }
    ((HttpServletResponse) resp).addCookie(myCookie);
  }

  private Cookie findCookie(Cookie[] cookies, String cookieName) {
    if (cookies != null) {
      for (Cookie ck : cookies) {
        if (ck.getName().equalsIgnoreCase(cookieName)) {
          return ck;
        }
      }
    }
    return null;
  }

  public void destroy() {
  }
}
