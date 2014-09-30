package org.fiteagle.dm.intercloud;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 * Servlet implementation class GatewayServlet
 */
public class AddResourceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String addResourceExample(String userName) {
			return "prefix : <http://127.0.0.1/IaaS.owl#> "
			+ "prefix owl: <http://www.w3.org/2002/07/owl#> "
			+ "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
			+ "prefix xsd: <http://www.w3.org/2001/XMLSchema#> "
			+ "insert data { "
			+ ":SampleCompute rdf:type :IBMCompute; "
			+ "rdf:type owl:NamedIndividual; "
			+ "rdfs:label \"IBM Silver 128b Compute Node\"^^xsd:anyURI ; "
			+ ":speed \"5.25\"^^xsd:string ; "
			+ ":cores 12; "
			+ ":memory \"64\"^^xsd:string ; "
			+ ":architecture \"x64\"^^xsd:string ;  "
			+ ":partOf :TUB_"+userName+".}";
	}
    /**
     * Default constructor. 
     */
    static String PAGE_HEADER = "<html><head><title>helloworld</title></head><body>";

    static String PAGE_FOOTER = "</body></html>";
	
	Gateway gateway;
	
    public AddResourceServlet() {
    	super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		HttpSession session = request.getSession(false);
		gateway = (Gateway) session.getAttribute("gateway");
		String user = (String) session.getAttribute("user");
		gateway.sendMessage(addResourceExample(user), "root@localhost/Smack", "update", user.concat("@localhost/Smack"));
    	response.sendRedirect("LoginSuccess.html");
	}
	
	public void destroy() {
		gateway.destroy();
	}
}
