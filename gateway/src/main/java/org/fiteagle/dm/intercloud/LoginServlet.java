package org.fiteagle.dm.intercloud;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;

import org.jivesoftware.smack.XMPPException;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static String createNodeExample = 
    		"prefix : <http://127.0.0.1/IaaS.owl#> "
    		+ "prefix owl: <http://www.w3.org/2002/07/owl#> "
    		+ "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
    		+ "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
    		+ "prefix wgs: <http://www.w3.org/2003/01/geo/wgs84_pos#> "
    		+ "prefix IaaS: <http://127.0.0.1/IaaS.owl#> "
    		+ "prefix xsd: <http://www.w3.org/2001/XMLSchema#> "
    		+ "base <http://127.0.0.1/IaaS.owl> "
    		+ "insert data { "
    		+ ":TUB_Eve rdf:type :Infrastructure; "
    		+ "rdf:type owl:NamedIndividual; "
    		+ "rdfs:label \"Eve Cloud in China\"^^xsd:anyURI ; "
    		+ "wgs:lat \"31.594786\" ; "
    		+ "wgs:long \"115.348503\"; "
    		+ ":partOf :IEEE_Intercloud_Testbed .}";
	public LoginServlet() {
		super();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = request.getParameter("user");
        String pwd = request.getParameter("pwd");
    	//Gateway gateway = new Gateway("root-intercloud.av.tu-berlin.de", 5222);
        Gateway gateway = new Gateway("localhost", 5222);
    	try {
    		gateway.init();
    	} catch (XMPPException e) {
    		e.printStackTrace();
    	}
       if(gateway.performLogin(user, pwd)){
            HttpSession session = request.getSession();
            session.setAttribute("gateway", gateway);
            session.setAttribute("user", user);
            //setting session to expiry in 30 mins
            session.setMaxInactiveInterval(30*60);
            Cookie userName = new Cookie("user", user);
            userName.setMaxAge(30*60);
            response.addCookie(userName);
            gateway.sendMessage(createNodeExample, "root@localhost/Smack", "update", user.concat("@localhost/Smack"));
            response.sendRedirect("LoginSuccess.html");
        }else{
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
            PrintWriter out= response.getWriter();
            out.println("<font color=red>Either user name or password is wrong.</font>");
            rd.include(request, response);
        }
	}

}
