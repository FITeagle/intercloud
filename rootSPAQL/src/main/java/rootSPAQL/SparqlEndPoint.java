package rootSPAQL;

import com.hp.hpl.jena.update.GraphStore;
import com.hp.hpl.jena.update.GraphStoreFactory;
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
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;


public class SparqlEndPoint {
	private Dataset intercloudDataset;
	SparqlEndPoint(String serviceURI) {
		intercloudDataset = DatasetFactory.create(serviceURI+"data?default");
	}
	public void updateSparql(String updateMessage) {
		intercloudDataset.begin(ReadWrite.WRITE);
		try {
			GraphStore graphStore = GraphStoreFactory.create(intercloudDataset);
			UpdateRequest request = UpdateFactory.create(updateMessage);
			UpdateProcessor proc = UpdateExecutionFactory.create(request, graphStore);
			proc.execute();
			intercloudDataset.commit();
		} finally {
			intercloudDataset.end();
		}
	}
    public String querySparql(String queryMessage) {
    	intercloudDataset.begin(ReadWrite.READ);
    	String resultMessage = new String();
        try {
            Query query = QueryFactory.create(queryMessage);
            QueryExecution qExe = QueryExecutionFactory.create(query, intercloudDataset);
        	ResultSet results = qExe.execSelect();
        	resultMessage = ResultSetFormatter.asXMLString(results);
        } finally {
        	intercloudDataset.end();
        }
    	return resultMessage;
    }
}
