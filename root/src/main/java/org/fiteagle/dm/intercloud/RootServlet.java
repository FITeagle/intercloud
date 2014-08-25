package org.fiteagle.dm.intercloud;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * Servlet implementation class RootServlet
 */
public class RootServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ArrayList<String> xmppMessageList;
	private ArrayList<String> jmsMessageList;
	private SparqlEndPoint sparqlEndPoint;
	private JMSEndPoint jmsEndPoint;
	private XMPPEndPoint xmppEndPoint;
	static String PAGE_HEADER = "<html><head><title>helloworld</title></head><body>";

    static String PAGE_FOOTER = "</body></html>";
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RootServlet() {
        super();
    }
    
    public void init(ServletConfig config) {
/*    	
    	sparqlEndPoint = new SparqlEndPoint("http://root-intercloud.av.tu-berlin.de:3030/IaaS/");
    	jmsEndPoint = new JMSEndPoint("topic/core", "message = 'org.fiteagle.core.db.resources'");
    	xmppEndPoint = new XMPPEndPoint("root-intercloud.av.tu-berlin.de", 5222);
*/ 
       	sparqlEndPoint = new SparqlEndPoint("http://localhost:3030/IaaS/");

       	xmppEndPoint = new XMPPEndPoint("localhost", 5222);   	
    	xmppEndPoint.setMessageListener(new XMPPMessageListener());
    	xmppEndPoint.performLogin("root", "root");

    	jmsEndPoint = new JMSEndPoint("topic/core", "message = 'org.fiteagle.core.db.resources.response'");
    	jmsEndPoint.setMessageListener(new JMSMessageListener());
        
    	config.getServletContext().setAttribute("sparqlEndPoint", sparqlEndPoint);
        config.getServletContext().setAttribute("jmsEndPoint", jmsEndPoint);
        config.getServletContext().setAttribute("xmppEndPoint", xmppEndPoint);
        
        xmppMessageList = new ArrayList<String>();
        jmsMessageList = new ArrayList<String>();
    }

    class XMPPMessageListener implements org.jivesoftware.smack.MessageListener {
    	@Override
    	public void processMessage(Chat chat, org.jivesoftware.smack.packet.Message message) {
            String body = message.getBody();
			xmppMessageList.add(body);
            try {
				jmsEndPoint.sendMessage(body, "org.fiteagle.core.db.resources.request");
			} catch (JMSException e) {
				e.printStackTrace();
			}
        }
        
    }
    
    class JMSMessageListener implements javax.jms.MessageListener {
		@Override
		public void onMessage(javax.jms.Message arg0) {
			try {
				TextMessage textMessage = (TextMessage) arg0;
				String messageBody = textMessage.getText();
				jmsMessageList.add(messageBody);
				Model model = stringToRDF(messageBody);
				Resource rdfMessage = model.getResource("http://RDFMessage");
				String reciever = rdfMessage.getProperty(RDFS.isDefinedBy).getString();
				xmppEndPoint.sendMessage(messageBody, reciever);
			} catch (JMSException e) {
				e.printStackTrace();
			} catch (XMPPException e) {
				e.printStackTrace();
			}
		}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.setIntHeader("Refresh", 2);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(PAGE_HEADER);
		out.println("<h1>" + Integer.toString(xmppMessageList.size()) + " Messages Recieved from XMPP</h1>");
		for (int i = 0; i != xmppMessageList.size(); i++) {
			out.println("<p>"+ Integer.toString(i) + ": " +  xmppMessageList.get(i) +"</p>");
		}
		out.println("<h1>" + Integer.toString(jmsMessageList.size()) + " Messages Recieved from JMS</h1>");
		for (int i = 0; i != jmsMessageList.size(); i++) {
			out.println("<p>"+ Integer.toString(i) + ": " +  jmsMessageList.get(i) +"</p>");
		}
		out.println();
		out.println(PAGE_FOOTER);
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public Model stringToRDF(String modelInString) {
		InputStream in = new ByteArrayInputStream(modelInString.getBytes(StandardCharsets.UTF_8));
		Model model = ModelFactory.createDefaultModel();
		model.read(in, null, "JSON-LD");
		return model;
	}

	public String RDFToString(Model model) {
    	StringWriter out = new StringWriter();
    	model.write(out, "JSON-LD");
    	String modelXML = out.toString();
    	return modelXML;
    }

}
