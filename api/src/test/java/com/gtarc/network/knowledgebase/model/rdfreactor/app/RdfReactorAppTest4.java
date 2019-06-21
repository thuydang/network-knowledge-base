package com.gtarc.network.knowledgebase.model.rdfreactor.app;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.Reasoning;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
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

public class RdfReactorAppTest4 {

	public static final String outdir = "target/generated-test-sources/rdfs-classes";

	private static final String ISCONS = "http://www.gt-arc.com/isco/ontologies/2017/10/sample-lifecycle-offering-1#";
	private static final String OMNNS = "http://open-multinet.info/ontology/omn#";

	private static Model model;

	@Before
	public void init() throws ModelRuntimeException {
		// specify to use Jena here:
		//RDF2Go.register( new org.ontoware.rdf2go.impl.jena24.ModelFactoryImpl() );
		// if not specified, RDF2Go.getModelFactory() looks into your classpath for ModelFactoryImpls to register.

		model = RDF2Go.getModelFactory().createModel();		
		model.open();
	}

	@Test
	public void exploreGeneratedModel() throws Exception {
		// creating resources
		String foafURI = "http://xmlns.com/foaf/0.1/";
		URI max = model.createURI("http://xam.de/foaf.rdf.xml#i");
		URI name = model.createURI(foafURI+"#term_name");
		URI icqId = model.createURI(foafURI+"#term_icqChatID");
		URI typeInteger = XSD._integer; 

		model.readFrom( new FileReader("src/main/resources/sample.ttl"), Syntax.Turtle );

		// proving the model has been read
		assert model.size() > 0 : "model contains statements after reading a serialized model";
		// dumping model to the screen
		model.dump();

		// Exploring Model
		System.out.println("\n\n--- Exploring Model ---\n");

		// finding statements 'tagged with "Java"'
		
		URI hasReservation = model.createURI(OMNNS + "hasReservation"); 
		URI reservation = model.createURI(ISCONS + "life3"); 
		
		ClosableIterator<? extends Statement> foundTaggedJava;
		foundTaggedJava = model.findStatements(Variable.ANY, hasReservation, reservation);
		System.out.println("Everything reserved for 'life3':");
		while (foundTaggedJava.hasNext()) {
			System.out.println(foundTaggedJava.next().getSubject());
		}
		foundTaggedJava.close();
	}
}

