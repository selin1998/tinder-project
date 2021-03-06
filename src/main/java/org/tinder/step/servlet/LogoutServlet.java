package org.tinder.step.servlet;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.tinder.step.entity.Activity;
import org.tinder.step.entity.User;
import org.tinder.step.service.ActivityService;
import org.tinder.step.service.CookiesService;
import org.tinder.step.service.UserService;
import org.tinder.step.util.TemplateEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Log4j2
public class LogoutServlet extends HttpServlet {
    private TemplateEngine engine;
    private ActivityService activityService;
    CookiesService cookiesService;


    public LogoutServlet(TemplateEngine engine) throws SQLException {
        this.engine = engine;
        activityService = new ActivityService();
    }


    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)  {
        cookiesService = new CookiesService(req, resp);

        int loggedUserId = cookiesService.getCookieValue().orElseThrow(Exception::new);

        ZonedDateTime zonedDateTimeNow = ZonedDateTime.now(ZoneId.of("UTC"));

        Activity activity = new Activity(loggedUserId, zonedDateTimeNow);

        activityService.addLogout_time(activity);

        cookiesService.removeCookie();

        log.info(loggedUserId+" is logged out");
        resp.sendRedirect("/login");
    }

}
