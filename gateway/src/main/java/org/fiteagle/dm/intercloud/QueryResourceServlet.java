package org.fiteagle.dm.intercloud;

import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Servlet implementation class QueryResourceServlet
 */
public class QueryResourceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    Gateway gateway;
      
    public QueryResourceServlet() {
        super();
    }

	public String RDFToString(Model model) {
    	StringWriter out = new StringWriter();
    	model.write(out, "JSON-LD");
    	String modelXML = out.toString();
    	return modelXML;
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		gateway = (Gateway) session.getAttribute("gateway");
		String user = (String) session.getAttribute("user");
		
		String queryMessage = request.getParameter("query");
					
		gateway.sendMessage(queryMessage, "root@localhost/Smack", "query", user.concat("@localhost/Smack"));
		
		response.sendRedirect("ShowResultsServlet");
	}

}
