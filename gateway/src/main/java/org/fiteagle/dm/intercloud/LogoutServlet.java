package org.fiteagle.dm.intercloud;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
/**
 * Servlet implementation class LogoutServlet
 */
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
        
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        HttpSession session = request.getSession(false);
        Gateway gateway = (Gateway) session.getAttribute("gateway");
        gateway.destroy();
        session.invalidate();
        response.sendRedirect("login.html");
    }
 
}