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
//    deleteEBSessionCookies(req, resp);
    showCookies(req, resp);
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

//  private void deleteEBSessionCookies(ServletRequest req, ServletResponse resp) {
//    try {
//      HttpServletRequest request = (HttpServletRequest) req;
//      Cookie[] cookies = request.getCookies();
//
//      for (Cookie ck : cookies) {
//        if (ck.getName().equalsIgnoreCase("EBSESSIONID")) {
//          ck.setMaxAge(0);
//          ck.setValue("");
//        }
//      }
//
//    } catch (Exception ex) {
//      ex.printStackTrace();
//    }
//  }

  private void showCookies(ServletRequest req, ServletResponse resp) {
    try {
      HttpServletRequest request = (HttpServletRequest) req;
      Cookie[] cookies = request.getCookies();

      for (Cookie ck : cookies) {
        if (ck.getName().equalsIgnoreCase("PORTALWLJSESSIONID")) {
          Cookie ebSessionCookie = findCookie(cookies, "EBSESSIONID");
          System.out.println(String.format("%s = %s, %s = %s ", ck.getName(), ck.getValue(), ebSessionCookie.getName(),
              ebSessionCookie.getValue()));
          if (ebSessionCookie != null) {
            System.out.println("Setting value of existing");
            ebSessionCookie.setValue(ck.getValue());
          }
          else {
            System.out.println("Add a new cookie");
            Cookie myCookie = new Cookie("EBSESSIONID", ck.getValue());
            ((HttpServletResponse) resp).addCookie(myCookie);
          }
          break;
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
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
