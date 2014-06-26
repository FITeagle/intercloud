package org.fiteagle.dm.intercloud;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class QueryEndPointServlet
 */
public class QueryEndPointServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static String PAGE_HEADER = "<html><head><title>helloworld</title></head><body>";

    static String PAGE_FOOTER = "</body></html>";

    public QueryEndPointServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//HttpSession session = request.getSession(false);
		ServletContext servletContext = request.getServletContext();
		Root root = (Root) servletContext.getAttribute("root");
		
		String queryMessage = request.getParameter("query");
		
		String results = root.querySparql(queryMessage);

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(PAGE_HEADER);
		
		out.println(results);
		
		out.println("<form action=\"queryResource.html\">");
		out.println("<input type=\"submit\" value=\"Back\" >");
		out.println("</form>");
		out.println(PAGE_FOOTER);
		out.close();
	}

}