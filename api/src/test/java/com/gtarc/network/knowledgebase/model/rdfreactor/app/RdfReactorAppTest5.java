package com.gtarc.network.knowledgebase.model.rdfreactor.app;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.Reasoning;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.QueryResultTable;
import org.ontoware.rdf2go.model.QueryRow;
import org.ontoware.rdf2go.model.Statement;
import org.ontoware.rdf2go.model.Syntax;
import org.ontoware.rdf2go.model.node.BlankNode;
import org.ontoware.rdf2go.model.node.DatatypeLiteral;
import org.ontoware.rdf2go.model.node.LanguageTagLiteral;
import org.ontoware.rdf2go.model.node.PlainLiteral;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.Variable;
import org.ontoware.rdf2go.vocabulary.XSD;
import org.ontoware.rdfreactor.generator.CodeGenerator;

/**
 * Test queries.
 * Reference: https://github.com/semweb4j/semweb4j/blob/master/org.semweb4j.tutorial/src/main/java/org/ontoware/semweb4j/lessons/lesson2/Step4.java 
 * @author dang
 *
 */
public class RdfReactorAppTest5 {

	public static final String outdir = "target/generated-test-sources/rdfs-classes";

	private static final String ISCO_NS = "http://www.gt-arc.com/isco/ontologies/2017/10/sample-lifecycle-offering-1#";
	private static final String OMN_NS = "http://open-multinet.info/ontology/omn#";
	private static final String OMNLC_NS = "http://open-multinet.info/ontology/omn-lifecycle#";

	private static Model model;

	@Before
	public void init() throws ModelRuntimeException, FileNotFoundException, IOException {
		// specify to use Jena here:
		//RDF2Go.register( new org.ontoware.rdf2go.impl.jena24.ModelFactoryImpl() );
		// if not specified, RDF2Go.getModelFactory() looks into your classpath for ModelFactoryImpls to register.

		model = RDF2Go.getModelFactory().createModel();		
		model.open();

		model.readFrom( new FileReader("src/main/resources/sample.ttl"), Syntax.Turtle );

		// proving the model has been read
		assert model.size() > 0 : "model contains statements after reading a serialized model";

		// dumping model to the screen
		model.dump();

	// creating resources
	}

	@Test
	public void exploreGeneratedModel() throws Exception {


		// Exploring Model
		System.out.println("\n\n--- Exploring Model ---\n");

		// finding statements 'tagged with "Java"'
		
		URI hasReservation = model.createURI(OMN_NS + "hasReservation"); 
		URI reservation = model.createURI(ISCO_NS + "life3"); 
		
		ClosableIterator<? extends Statement> foundTaggedJava;
		foundTaggedJava = model.findStatements(Variable.ANY, hasReservation, reservation);
		System.out.println("Everything reserved for 'life3':");
		while (foundTaggedJava.hasNext()) {
			System.out.println(foundTaggedJava.next().getSubject());
		}
		foundTaggedJava.close();
	}
	
	@Test
	public void sparqlTest () {
		// another SPARQL SELECT
		System.out.println("\n\n\n-------------------------\n");

		PlainLiteral tagJava = model.createPlainLiteral("Java");
		String queryString = "SELECT ?person WHERE { ?person <"+/*URI*/" " +"> "+tagJava.toSPARQL()+" }";
		QueryResultTable results; // = model.sparqlSelect(queryString);

		URI hasReservation = model.createURI(OMN_NS + "hasReservation"); 
		URI reservation = model.createURI(ISCO_NS + "life3"); 
		URI hasID = model.createURI(OMNLC_NS + "hasID"); 

		System.out.println("Query 1:");
		// select resource and tag in every tagging statement 
		
		queryString = "SELECT ?resource WHERE { ?resource <"+hasReservation+"> " + "<" + reservation + ">" + " }";
		System.out.println(queryString);
		//queryString = "SELECT ?resource ?reservation WHERE { ?resource ?p ?reservation }";
		results = model.sparqlSelect(queryString);
		for(String var : results.getVariables()) {
			System.out.println(var);
		}
		for(QueryRow row : results) {
			System.out.println(row.getValue("resource") + " has reservation " + reservation);
		}

		System.out.println("\n\n-------------------------\n");
		System.out.println("Query 2:");
		// select resource and tag in every tagging statement 
		
		queryString = "SELECT ?resource ?reservation WHERE { ?resource <"+hasReservation+"> ?reservation }";
		System.out.println(queryString);
		//queryString = "SELECT ?resource ?reservation WHERE { ?resource ?p ?reservation }";
		results = model.sparqlSelect(queryString);
		for(String var : results.getVariables()) {
			System.out.println(var);
		}
		for(QueryRow row : results) {
			System.out.println(row.getValue("resource") + " has reservation " + row.getValue("reservation"));
		}
		

		/*
		// SPARQL SELECT with typed literal
		System.out.println("\n\n-------------------------\n");
		System.out.println("Query 3:");
		// select name of all resources with integer age of 19:
		queryString = "SELECT ?name WHERE { ?resource <"+hasAge+"> \"19\"^^<"+XSD._integer+"> . ?resource <"+hasName+"> ?name }";
		//works also:
		DatatypeLiteral nineteen = model.createDatatypeLiteral("19",XSD._integer);
		String queryString2 = "SELECT ?name WHERE { ?resource <"+hasAge+"> "+nineteen.toSPARQL()+" . ?resource <"+hasName+"> ?name }";
		assert queryString.equals(queryString2);
		
		results = model.sparqlSelect(queryString);
		for(QueryRow row : results) {
			System.out.println(row);
		}
		*/

		// SPARQL SELECT with regex FILTER
		System.out.println("\n\n-------------------------\n");
		System.out.println("Query 4:");
		// select name and age of all resources with a name like ".*? Völkel" (case-insensitive)
		queryString = "SELECT ?resource ?reservation WHERE { ?resource <"+hasReservation+"> ?reservation . FILTER regex(?reservation, \".*? life\", \"i\") . ?resource <"+hasID+"> ?id }";
		System.out.println("All results of this query formatted:");
		results = model.sparqlSelect(queryString);
		for(QueryRow row : results) {
			System.out.println(row);
		}
	}
}

