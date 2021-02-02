package project;
import java.io.InputStream;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasonerFactory;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.util.FileManager;


public class rdfCreater {
public static void main(String[] args) {
// TODO Auto-generated method stub
	
	String query1 = 
			" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
			+" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
			+ " SELECT ?s ?o ?p where {?s ?o ?p}";
	
	String query2 = 
			" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
			+" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
			+ " SELECT ?s ?o ?p where {?s ?o <http://xmlns.com/future/planet>}";
	

	
	/**
	 * 
	 * Test rules
	 * 
	 */
	StringBuilder rules = new StringBuilder();
	/**
	 * 
	 * Create a data model and load file
	 * 
	 */

	Model model = ModelFactory.createDefaultModel();

	String pathToOntology = "C:\\Users\\Administrator\\rdf.rdf";

	InputStream in = FileManager.get().open(pathToOntology);

	Long start = System.currentTimeMillis();

	model.read(in, null);

	System.out.println("Import time : " + (System.currentTimeMillis() - start));

	/**
	 * 
	 * Starting a reasoner
	 * 
	 */

	GenericRuleReasoner reasoner = (GenericRuleReasoner) GenericRuleReasonerFactory.theInstance().create(null);

	reasoner.setRules(Rule.parseRules(rules.toString()));

	// change the type of reasoner
	reasoner.setMode(GenericRuleReasoner.HYBRID);

	start = System.currentTimeMillis();

	InfModel inf = ModelFactory.createInfModel(reasoner, model);

	System.out.println("Rules pre-processing time : " + (System.currentTimeMillis() - start));

	/**
	 * 
	 * Create a query object
	 * 
	 */

	Query query = QueryFactory.create(query2);

	start = System.currentTimeMillis();

	QueryExecution qexec = QueryExecutionFactory.create(query, inf);

	System.out.println("Query pre-processing time : " + (System.currentTimeMillis() - start));

	/**
	 * 
	 * Execute Query and print result
	 * 
	 */
	start = System.currentTimeMillis();

	try {

		ResultSet rs = qexec.execSelect();

		ResultSetFormatter.out(System.out, rs, query);

	} finally {

		qexec.close();
	}

	System.out.println("Query + Display time : " + (System.currentTimeMillis() - start));

	 /**
	 *
	 * Export saturated ontology
	 *
	 */

	// PrintWriter resultWriter;
	// try {
	// resultWriter = new PrintWriter("testmycfsat.rdf");
	//
	// inf.write(resultWriter);
	//
	// resultWriter.close();
	//
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// }

}	
	
	
}
