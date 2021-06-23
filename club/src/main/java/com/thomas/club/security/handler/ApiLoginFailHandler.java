package com.thomas.club.security.handler;

import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class ApiLoginFailHandler implements AuthenticationFailureHandler {

    // API 인증에 실패하는 경우
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("login fail handler..................");
        log.info(exception.getMessage());

        // 401 Error
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // json Return
        response.setContentType("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        String msg = exception.getMessage();
        json.put("code", "401");
        json.put("message", msg);

        PrintWriter out = response.getWriter();
        out.print(json);
    }
}