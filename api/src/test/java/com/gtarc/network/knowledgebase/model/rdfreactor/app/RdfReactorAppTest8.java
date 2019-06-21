package com.gtarc.network.knowledgebase.model.rdfreactor.app;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.ontoware.aifbcommons.collection.ClosableIterable;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.ModelFactory;
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
import org.ontoware.rdf2go.vocabulary.RDFS;
import org.ontoware.rdf2go.vocabulary.XSD;
import org.ontoware.rdfreactor.generator.CodeGenerator;

/**
 * Test Reasoning.
 * Reference: https://github.com/semweb4j/semweb4j/blob/master/org.semweb4j.tutorial/src/main/java/org/ontoware/semweb4j/lessons/lesson4/Step1.java
 * @author dang
 *
 */
public class RdfReactorAppTest8 {

	public static final String outdir = "target/generated-test-sources/rdfs-classes";

	//private static final String ISCO_NS = "http://www.gt-arc.com/isco/ontologies/2017/10/sample-lifecycle-offering-1#";
	private static final String ISCO_NS = "http://www.gt-arc.com/network/management/ontologies/2017/10/isco-sample-1#";
	private static final String OMN_NS = "http://open-multinet.info/ontology/omn#";
	private static final String OMNLC_NS = "http://open-multinet.info/ontology/omn-lifecycle#";

	private static Model model;

	@Before
	public void init() throws ModelRuntimeException, FileNotFoundException, IOException {
		// specify to use Jena here:
		//RDF2Go.register( new org.ontoware.rdf2go.impl.jena24.ModelFactoryImpl() );
		// if not specified, RDF2Go.getModelFactory() looks into your classpath for ModelFactoryImpls to register.

		// Create model with reasoning
		ModelFactory modelFactory = RDF2Go.getModelFactory();		
		Reasoning reasoning = Reasoning.rdfsAndOwl;

		model = modelFactory.createModel(reasoning);
		model.open();

		//model.readFrom( new FileReader("src/main/resources/sample.ttl"), Syntax.Turtle );
		model.readFrom( new FileReader("src/main/resources/isco-sample-1.rdf"), Syntax.RdfXml );

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
		URI classResource = model.createURI(ISCO_NS + "Resource"); 

		ClosableIterator<? extends Statement> statement;
		statement = model.findStatements(Variable.ANY, hasReservation, reservation);
		System.out.println("Everything reserved for 'life3':");
		while (statement.hasNext()) {
			System.out.println(statement.next().getSubject());
		}
		statement.close();
	}

	@Test
	public void reasoningTest () {
		// another SPARQL SELECT
		System.out.println("\n\n\n--------- REASONING TEST ----------------\n");

		PlainLiteral tagJava = model.createPlainLiteral("Java");
		
		URI owlClass = model.createURI(OMNLC_NS + "Offering"); 
		URI classResource = model.createURI(OMN_NS + "Resource"); 
		//ClosableIterator<? extends Statement> it = model.findStatements(owlClass, RDFS.subClassOf, Variable.ANY);
//		ClosableIterator<? extends Statement> it = model.findStatements(Variable.ANY, RDFS.subClassOf, Variable.ANY);
		ClosableIterator<? extends Statement> it = model.findStatements(owlClass, RDFS.subClassOf, Variable.ANY);
		while (it.hasNext()) {
			Statement st = it.next();
			System.out.println(st.getSubject() + " <" + st.getPredicate() + "> " + st.getObject());
		}
		it.close();
		
		/*
		QueryResultTable results; // = model.sparqlSelect(queryString);

		URI hasReservation = model.createURI(OMN_NS + "hasReservation"); 
		URI reservation = model.createURI(ISCO_NS + "life3"); 
		URI hasID = model.createURI(OMNLC_NS + "hasID"); 

		// simple SPARQL DESCRIBE
		System.out.println("\n\n-------------------------\n");
		System.out.println("Query 1:");
		// describe max
		queryString = "DESCRIBE <"+reservation+">";
		ClosableIterable<? extends Statement> results1 = model.sparqlDescribe(queryString);
		for(Statement result : results1 ) {
			System.out.println(result);
		}

		// another SPARQL DESCRIBE
		System.out.println("\n\n-------------------------\n");
		System.out.println("Query 2:");
		// describe every resource tagged with java 
		queryString = "DESCRIBE ?resource WHERE { ?resource <"+hasReservation+"> " + "<" + reservation + ">" + " }";
		results1 = model.sparqlDescribe(queryString);
		for(Statement result : results1 ) {
			System.out.println(result);
		}
		*/
	}
}

