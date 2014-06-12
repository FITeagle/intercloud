package de.tu_berlin.av.intercloud;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jivesoftware.smack.XMPPException;
import java.util.ArrayList;
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
	
	Gateway gateway1 = new Gateway("localhost", 5222);
	Gateway gateway2 = new Gateway("localhost", 5222);
	
    public GatewayServlet() {
        // TODO Auto-generated constructor stub
    	try {
    		gateway1.init();
    		gateway1.performLogin("gateway1", "gateway1");
    		gateway2.init();
    		gateway2.performLogin("gateway2", "gateway2");
    	} catch (XMPPException e) {
    		e.printStackTrace();
    	}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
    	try {
		gateway1.sendMessage("Hello gateway2, I'm gateway1", "gateway2@localhost/Smack"); 
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(PAGE_HEADER);
		ArrayList<String> messageList = gateway2.getMessage();
		out.println(messageList.size());
		for (int i = 0; i != messageList.size(); i++) {
			out.println("<h1>"+messageList.get(i)+"</h1>");
		}
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
		// TODO Auto-generated method stub
	}
	
	public void destroy() {
		gateway1.destroy();
		gateway2.destroy();
	}
}
