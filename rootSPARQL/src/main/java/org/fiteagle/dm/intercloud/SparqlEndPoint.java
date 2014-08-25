package org.fiteagle.dm.intercloud;

import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;


public class SparqlEndPoint {
	private Dataset intercloudDataset;
	private String connectURI;
	SparqlEndPoint(String serviceURI) {
		intercloudDataset = DatasetFactory.create(serviceURI+"data?default");
		connectURI = new String(serviceURI);
	}
	public String updateSparql(String updateMessage) {
		UpdateRequest request = UpdateFactory.create(updateMessage);
		UpdateProcessor uExe = UpdateExecutionFactory.createRemote(request, connectURI+"update");
		uExe.execute();
		return uExe.toString();
	}
    public String querySparql(String queryMessage) {
    	String resultMessage = new String();
        Query query = QueryFactory.create(queryMessage);
        QueryExecution qExe = QueryExecutionFactory.create(query, intercloudDataset);
        try {
        	ResultSet results = qExe.execSelect();
        	resultMessage = ResultSetFormatter.asText(results);
        } finally {
        	qExe.close();
        }
    	return resultMessage;
    }
}
