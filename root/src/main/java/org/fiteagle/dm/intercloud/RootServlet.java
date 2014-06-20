package org.fiteagle.dm.intercloud;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUtils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jivesoftware.smack.XMPPException;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Servlet implementation class RootServlet
 */
public class RootServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static String PAGE_HEADER = "<html><head><title>helloworld</title></head><body>";

    static String PAGE_FOOTER = "</body></html>";
	
	Root root = new Root("localhost", 5222);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RootServlet() {
        super();
        try {
        	root.init();        	
        } catch (XMPPException e) {
        	e.printStackTrace();
        }
        
        root.performLogin("root", "root");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(PAGE_HEADER);
		ArrayList<String> messageList = root.getMessage();
		out.println("<h1>" + Integer.toString(messageList.size()) + " Messages Recieved</h1>");
		for (int i = 0; i != messageList.size(); i++) {
			out.println("<h1>"+ Integer.toString(i) + ": " + messageList.get(i) +"</h1>");
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

}
