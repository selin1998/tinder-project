package org.tinder.step.servlet;

import org.tinder.step.entity.Message;
import org.tinder.step.entity.User;
import org.tinder.step.service.CookiesService;
import org.tinder.step.service.MessageService;
import org.tinder.step.service.UserService;
import org.tinder.step.util.TemplateEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;

public class MessageServlet extends HttpServlet {
    private final TemplateEngine engine;
    MessageService mservice=new MessageService();
    UserService uservice=new UserService();
    CookiesService cookiesService;
    int loggedUserId;

    public MessageServlet(TemplateEngine engine)  {
        this.engine = engine;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        cookiesService=new CookiesService(req,resp);

        try {
            loggedUserId=cookiesService.getCookieValue().orElseThrow(Exception::new);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, Object> data = new HashMap<>();
        String path = req.getPathInfo();
        int user_id_to = Integer.parseInt(path.substring(1));

        List<Message> archiveChat = mservice.getMessagesForCurrentChat(loggedUserId, user_id_to);
        User targetUser = null;
        User loggedUser=null;
        try {
            targetUser = uservice.getById(user_id_to);
            loggedUser=uservice.getById(loggedUserId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        data.put("messages",archiveChat);
        data.put("targetUser",targetUser);
        data.put("loggedUser",loggedUser);
        engine.render("chat.ftl",data,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        int user_id_to = Integer.parseInt(path.substring(1));
        ZonedDateTime time=ZonedDateTime.now();

        mservice.add(new Message(loggedUserId,user_id_to,req.getParameter("input"),time));

        doGet(req,resp);

    }
}
