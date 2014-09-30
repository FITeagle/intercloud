package org.fiteagle.dm.intercloud;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
/**
 * Servlet implementation class LogoutServlet
 */
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static String deleteNodeExample(String userName) { 
    		return "prefix : <http://127.0.0.1/IaaS.owl#> "
    		+ "prefix owl: <http://www.w3.org/2002/07/owl#> "
    		+ "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
    		+ "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
    		+ "prefix wgs: <http://www.w3.org/2003/01/geo/wgs84_pos#> "
    		+ "prefix IaaS: <http://127.0.0.1/IaaS.owl#> "
    		+ "prefix xsd: <http://www.w3.org/2001/XMLSchema#> "
    		+ "base <http://127.0.0.1/IaaS.owl> "
    		+ "delete data { "
    		+ ":TUB_"+userName+" rdf:type :Infrastructure; "
    		+ "rdf:type owl:NamedIndividual; "
    		+ "rdfs:label \"Example Cloud\"^^xsd:anyURI ; "
    		+ "wgs:lat \"31.594786\" ; "
    		+ "wgs:long \"115.348503\"; "
    		+ ":partOf :IEEE_Intercloud_Testbed .}";
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        HttpSession session = request.getSession(false);
        Gateway gateway = (Gateway) session.getAttribute("gateway");
        String user = (String) session.getAttribute("user");
        gateway.sendMessage(deleteNodeExample(user), "root@localhost/Smack", "update", user.concat("@localhost/Smack"));
        gateway.destroy();
        session.invalidate();
        response.sendRedirect("login.html");
    }
 
}