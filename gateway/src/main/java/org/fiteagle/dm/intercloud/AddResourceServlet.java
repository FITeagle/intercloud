package org.fiteagle.dm.intercloud;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jivesoftware.smack.XMPPException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.RDFS;
/**
 * Servlet implementation class GatewayServlet
 */
public class AddResourceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    static String PAGE_HEADER = "<html><head><title>helloworld</title></head><body>";

    static String PAGE_FOOTER = "</body></html>";
	
	Gateway gateway;
//	Gateway gateway2 = new Gateway("localhost", 5222);
	
    public AddResourceServlet() {
    	super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	try {
			response.setContentType("text/html");
	
			HttpSession session = request.getSession(false);
			gateway = (Gateway) session.getAttribute("gateway");
	
			String resourceURI = request.getParameter("resourceURI");
			String resourceName = request.getParameter("resourceName");
			String resourceHomePage = request.getParameter("resourceHomePage");
			String mailBox = request.getParameter("mailBox");
			String resourceDescription = request.getParameter("resourceDescription");
			String geoLat = request.getParameter("geoLat");
			String geoLong = request.getParameter("geoLong");
			
			Model model = ModelFactory.createDefaultModel();
			Property geoLa = model.createProperty("http://www.w3.org/2003/01/geo/wgs84_pos#", "lat");
			Property geoLo = model.createProperty("http://www.w3.org/2003/01/geo/wgs84_pos#", "long");
			model.createResource(resourceURI)
				.addProperty(RDFS.label, resourceName)
				.addProperty(FOAF.homepage, resourceHomePage)
				.addProperty(FOAF.mbox, mailBox)
				.addProperty(RDFS.comment, resourceDescription)
				.addProperty(geoLa, geoLat)
				.addProperty(geoLo, geoLong);
					
			gateway.sendMessage(model, "rootAdd@localhost/Smack"); 
   	} catch (XMPPException e) {
    		e.printStackTrace();
    	}
    	response.sendRedirect("LoginSuccess.html");
	}
	
	public void destroy() {
		gateway.destroy();
	}
}
