package com.gtarc.network.knowledgebase.model.rdfreactor.app;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
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
import org.ontoware.rdf2go.vocabulary.XSD;
import org.ontoware.rdfreactor.generator.CodeGenerator;

public class RdfReactorAppTest2 {

	public static final String outdir = "target/generated-test-sources/rdfs-classes";

	private static final String FOAFNS = "http://xmlns.com/foaf/0.1/";
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
		// creating URIs
		URI max = model.createURI("http://xam.de/foaf.rdf.xml#i");
		URI currentProject = model.createURI(FOAFNS+"#term_currentProject");
		URI name = model.createURI(FOAFNS+"#term_name");
		URI semweb4j = model.createURI("http://semweb4j.org");

		// adding statements
		model.addStatement(max, currentProject, semweb4j);
		model.addStatement(max, name, "Max Völkel");

		// serializing model with every syntax possible
		OutputStreamWriter writer = new OutputStreamWriter(System.out);
		writer.write("Ntriples following:\n");
		model.writeTo( writer, Syntax.Ntriples);
		writer.write("\nRdfXml following:\n");
		model.writeTo( writer, Syntax.RdfXml);
		writer.flush();

		// it works also with System.out (which is an OutStream):
		writer.write("\nTurtle following:\n");
		model.writeTo( System.out, Syntax.Turtle);
		writer.write("\nTrig following:\n");
		model.writeTo( System.out, Syntax.Trig);
		//TODO add TriG output to web

		// because Trix is not implemented in Jena24, we don't do this:
		// model.writeTo( writer, Syntax.Trix);

		// It's also possible to serialize into a String:
		@SuppressWarnings("unused")
		String turtleSerialization = model.serialize(Syntax.Turtle);
		
		model.writeTo(new FileWriter("src/main/resources/model.ttl"), Syntax.Turtle);
	}
}

