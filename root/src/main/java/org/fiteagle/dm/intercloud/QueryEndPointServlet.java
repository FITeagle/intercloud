package org.fiteagle.dm.intercloud;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

		ServletContext servletContext = request.getServletContext();
		SparqlEndPoint sparqlEndPoint = (SparqlEndPoint) servletContext.getAttribute("sparqlEndPoint");
		
		String queryMessage = request.getParameter("query");
		
		String results = sparqlEndPoint.querySparql(queryMessage);
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(PAGE_HEADER);
		
		out.println("<textarea rows=\"10\" cols=\"150\">");
		out.println(results);
		out.println("</textarea>");
		out.println("<br>");
		
		out.println("<input type=\"button\" value=\"Back\" onclick=\"window.location='queryResource.html'\" >");
		out.println(PAGE_FOOTER);
		out.close();
	}

}
