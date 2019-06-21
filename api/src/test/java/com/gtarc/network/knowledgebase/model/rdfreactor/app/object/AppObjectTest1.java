package com.gtarc.network.knowledgebase.model.rdfreactor.app.object;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.ontoware.rdf2go.ModelFactory;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.Reasoning;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Syntax;

public class AppObjectTest1 {
	//private static final String ISCO_NS = "http://www.gt-arc.com/isco/ontologies/2017/10/sample-lifecycle-offering-1#";
	private static final String ISCO_NS = "http://www.gt-arc.com/network/management/ontologies/2017/10/isco-sample-1#";
	private static final String OMN_NS = "http://open-multinet.info/ontology/omn#";
	private static final String OMNLC_NS = "http://open-multinet.info/ontology/omn-lifecycle#";

	private static Model model;


	//@Before
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
	public void test() throws Exception {
		/*
		 * See http://www.w3.org/2003/01/geo/
		 *  and http://www.w3.org/2003/01/geo/wgs84_pos.rdf
		 *  
		 * In WGS84_pos, Point is a subclass of SpatialThing which has
		 *   the properties lat,long,alt which stands for
		 *     latitude (in decimal degrees), a String
		 *     longitude (in decimal degrees), a String
		 *     altitude (in decimal meters above the local reference ellipsoid), a String
		 */

		model = RDF2Go.getModelFactory().createModel();		
		model.open();

		Point.init(model);
		
		@SuppressWarnings("unused")
		Point A = new Point("0","0","0");
		@SuppressWarnings("unused")
		Point B = new Point("1,4","10,1","0");
		
		model.writeTo(System.out, Syntax.Turtle);
	}

}