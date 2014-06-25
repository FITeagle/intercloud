package org.fiteagle.dm.intercloud;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jivesoftware.smack.XMPPException;

/**
 * Servlet implementation class QueryResourceServlet
 */
public class QueryResourceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    Gateway gateway;
      
    public QueryResourceServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession(false);
			gateway = (Gateway) session.getAttribute("gateway");
			
			String queryMessage = request.getParameter("query");
			
			gateway.sendMessage(queryMessage, "rootQuery@localhost/Smack");
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		response.sendRedirect("ShowResultsServlet");
	}

}
