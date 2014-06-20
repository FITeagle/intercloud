package org.fiteagle.dm.intercloud;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jivesoftware.smack.XMPPException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.VCARD;
/**
 * Servlet implementation class GatewayServlet
 */
public class GatewayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    static String PAGE_HEADER = "<html><head><title>helloworld</title></head><body>";

    static String PAGE_FOOTER = "</body></html>";
	
	Gateway gateway;
//	Gateway gateway2 = new Gateway("localhost", 5222);
	
    public GatewayServlet() {
    	super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	try {
		response.setContentType("text/html");
		HttpSession session = request.getSession(false);
		gateway = (Gateway) session.getAttribute("gateway");
		String gatewayName = (String) session.getAttribute("user");
		Model model = ModelFactory.createDefaultModel();
		Resource cloud = model.createResource("http://" + gatewayName)
				.addProperty(VCARD.EMAIL, "admin@Cloud")
				.addProperty(VCARD.FN, gatewayName);
		gateway.sendMessage(model, "root@localhost/Smack"); 
		PrintWriter out = response.getWriter();
		out.println(PAGE_HEADER);
		out.println("<h1>RDF Message Send to Root</h1>");
		out.println("<form action=\"LogoutServlet\" method=\"post\">");
		out.println("<input type=\"submit\" value=\"Logout\" >");
		out.println("</form>");
		out.println(PAGE_FOOTER);
		out.close();
    	} catch (XMPPException e) {
    		e.printStackTrace();
    	}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public void destroy() {
		gateway.destroy();
	}
}
