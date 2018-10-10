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

public class ApplicationFilter implements Filter {
  private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationFilter.class);

  @Autowired
  SessionValidityChecker sessionValidityChecker;

  public void init(FilterConfig arg0) throws ServletException {

  }

  public void doFilter(ServletRequest req, ServletResponse resp,
                       FilterChain chain) throws IOException, ServletException {
    String path = ((HttpServletRequest) req).getRequestURI();
    String sessionId = copyCookies(req, resp, path);

    boolean sessionValid = sessionValidityChecker.isSessionValid(sessionId);

    if ((sessionId != null && !sessionId.isEmpty()) && !sessionValid) {
      RequestDispatcher rd = req.getRequestDispatcher("https://newtestwww.discsrv.co.za/portal/individual/login");
      rd.forward(req, resp);
    }
    else {
      if (path.contains("/assets") || path.contains(".js") || path.contains(".css") || path.contains(".png") || path
          .contains(".svg")) {
        chain.doFilter(req, resp);
      }
      else {
        RequestDispatcher rd = req.getRequestDispatcher("/index.html");
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
        addNew(resp, original.getValue(), path);
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
    else {
      myCookie.setPath("/eb-web-employer-zone/employer-onboarding");
    }
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
