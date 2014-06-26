package org.fiteagle.dm.intercloud;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ShowResultsServlet
 */
public class ShowResultsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	static String PAGE_HEADER = "<html><head><title>helloworld</title></head><body>";

    static String PAGE_FOOTER = "</body></html>";

    Gateway gateway;

    public ShowResultsServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setIntHeader("Refresh", 2);
		response.setContentType("text/plain");
		
		HttpSession session = request.getSession(false);
		gateway = (Gateway) session.getAttribute("gateway");
				
		PrintWriter out = response.getWriter();
		out.println(PAGE_HEADER);
		ArrayList<String> messageList = gateway.getMessage();
		out.println("<h1>" + Integer.toString(messageList.size()) + " Messages Recieved</h1>");
		for (int i = 0; i != messageList.size(); i++) {
			out.println("<h1>"+ Integer.toString(i) + ": " + messageList.get(i) +"</h1>");
		}
		out.println();
		out.println("<form action=\"LoginSuccess.html\">");
		out.println("<input type=\"submit\" value=\"Back\" >");
		out.println("</form>");
		out.println(PAGE_FOOTER);
		out.close();
		
	}

}
