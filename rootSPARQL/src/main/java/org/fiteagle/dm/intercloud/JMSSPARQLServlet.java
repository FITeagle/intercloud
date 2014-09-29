package org.fiteagle.dm.intercloud;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * Servlet implementation class JMSSPAEQLServlet
 */
public class JMSSPARQLServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private JMSEndPoint jmsEndPoint;
	private SparqlEndPoint sparqlEndPoint;
	private ArrayList<String> jmsRecievedMessageList;
	private ArrayList<String> jmsSendMessageList;
	static String PAGE_HEADER = "<html><head><title>helloworld</title></head><body>";

    static String PAGE_FOOTER = "</body></html>";
	
    public JMSSPARQLServlet() {
        super();
    }

    public void init(ServletConfig config) {
    	jmsEndPoint = new JMSEndPoint("topic/core", "message = 'org.fiteagle.core.db.resources.request'");
       	sparqlEndPoint = new SparqlEndPoint("http://localhost:3030/IaaS/");
       	jmsRecievedMessageList = new ArrayList<String>();
       	jmsSendMessageList = new ArrayList<String>();
        jmsEndPoint.setMessageListener(new MessageReciever());
    }
    
	public class MessageReciever implements MessageListener {

		@Override
		public void onMessage(Message arg0) {
			try {
				TextMessage textMessage = (TextMessage) arg0;
				String messageBody = textMessage.getText();
				jmsRecievedMessageList.add(messageBody);
				Model rdfMessage = stringToRDF(messageBody);
				Resource rdfMessageBody = rdfMessage.getResource("http://RDFMessage");
				String messageType = rdfMessageBody.getProperty(RDF.type).getString();
				String content = rdfMessageBody.getProperty(RDF.value).getString();
				String sender = rdfMessageBody.getProperty(RDFS.isDefinedBy).getString();
				if (messageType.equals("update")) {
					sparqlEndPoint.updateSparql(content);
				} else if (messageType.equals("query")) {
					String queryResult = sparqlEndPoint.querySparql(content);
					Model model = ModelFactory.createDefaultModel();
					model.createResource("http://RDFMessage")
						.addProperty(RDF.type, "result")
						.addProperty(RDF.value, queryResult)
						.addProperty(RDFS.isDefinedBy, sender);
					jmsSendMessageList.add(RDFToString(model));
					jmsEndPoint.sendMessage(RDFToString(model), "org.fiteagle.core.db.resources.response");
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.setIntHeader("Refresh", 2);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(PAGE_HEADER);
		out.println("<h1>" + Integer.toString(jmsRecievedMessageList.size()) + " Messages Recieved from JMS</h1>");
		for (int i = 0; i != jmsRecievedMessageList.size(); i++) {
			out.println("<p>"+ Integer.toString(i) + ": " +  jmsRecievedMessageList.get(i) +"</p>");
		}
		out.println("<h1>" + Integer.toString(jmsSendMessageList.size()) + " Messages Sent to JMS</h1>");
		for (int i = 0; i != jmsSendMessageList.size(); i++) {
			out.println("<p>"+ Integer.toString(i) + ": " +  jmsSendMessageList.get(i) +"</p>");
		}
		out.println();
		out.println(PAGE_FOOTER);
		out.close();
	}

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
