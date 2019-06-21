package com.gtarc.network.management.ontology.tools.owlapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

public class AppTest {


	private static Path testFile;

	@Test
	public void convertOwl2Rdf() throws Exception {
		//
		// Get hold of an ontology manager
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		// Load the ontology from a local files
		File srcFile = new File("/mnt/extdata/src/80_isco_ws/network-knowledge-base/ontologies/service-information-base.src");
		System.out.println("Loaded ontology: " + srcFile);
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(srcFile);

		// save the file into RDF/XML format
		//in this case, my ontology file and format already manage prefix
		File rdfFile = new File("/mnt/extdata/src/80_isco_ws/network-knowledge-base/ontologies/service-information-base_restore.rdf");
		RDFXMLDocumentFormat rdfxmlFormat = new RDFXMLDocumentFormat();
		manager.saveOntology(ontology, rdfxmlFormat, new FileOutputStream(rdfFile));

		File owlFile = new File("/mnt/extdata/src/80_isco_ws/network-knowledge-base/ontologies/service-information-base_restore.owl");
		OWLXMLDocumentFormat owlxmlFormat = new OWLXMLDocumentFormat();
		manager.saveOntology(ontology, owlxmlFormat, new FileOutputStream(owlFile));

		File ttlFile = new File("/mnt/extdata/src/80_isco_ws/network-knowledge-base/ontologies/service-information-base_restore.ttl");
		TurtleDocumentFormat ttlFormat = new TurtleDocumentFormat();
		manager.saveOntology(ontology, ttlFormat, new FileOutputStream(ttlFile));
	}

	/*
	//@Test
	public void saveFile() throws Exception {
		if (testFile != null) {
			// Only run once
			return;
		}

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();

		// Create ontology with two imports
		IRI ontologyIRI = IRI.create("prov.ttl");
		OWLOntology ontology = manager.createOntology(ontologyIRI);
		IRI provIri = IRI.create("http://www.w3.org/ns/prov");
		OWLOntology prov = manager.loadOntology(provIri);
		IRI foafIri = IRI.create("http://xmlns.com/foaf/0.1");
		OWLOntology foaf = manager.loadOntology(foafIri);

		DefaultPrefixManager pm = new DefaultPrefixManager();
		pm.setDefaultPrefix(ontologyIRI + "#");
		pm.setPrefix("foaf:", "http://xmlns.com/foaf/0.1/");
		pm.setPrefix("prov:", "http://www.w3.org/ns/prov#");

		// NOTE: We can't use the correct URIs (as above) here, as it will
		// wrongly be output as
		// "prov:null" in the Turtle output
		OWLImportsDeclaration provImport = factory
			.getOWLImportsDeclaration(provIri);
		manager.applyChange(new AddImport(ontology, provImport));
		OWLImportsDeclaration foafImport = factory
			.getOWLImportsDeclaration(foafIri);
		manager.applyChange(new AddImport(ontology, foafImport));

		OWLNamedIndividual index = factory.getOWLNamedIndividual(IRI
				.create("index.html"));
		manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(index));


		OWLClass entityClass = factory.getOWLClass("prov:Entity", pm);
		manager.addAxiom(ontology,
				factory.getOWLClassAssertionAxiom(entityClass, index));

		OWLObjectProperty wasAttributedTo = factory.getOWLObjectProperty(
				"prov:wasAttributedTo", pm);
		OWLIndividual me = factory.getOWLNamedIndividual(IRI
				.create("http://soiland-reyes.com/stian/#me"));
		// The order of these parameters is a bit odd.. predicate/subject/object
		manager.addAxiom(ontology, factory.getOWLObjectPropertyAssertionAxiom(
					wasAttributedTo, index, me));

		OWLClass personClass = factory.getOWLClass("foaf:Person", pm);
		manager.addAxiom(ontology,
				factory.getOWLClassAssertionAxiom(personClass, me));

		// Note: Using foaf:lastName instead of foaf:name
		// because it is an invalid dataproperty subproperty of annotation property: 
		// 
		// http://protege-ontology-editor-knowledge-acquisition-system.136.n4.nabble.com/Custom-import-ontology-button-problem-td3684903.html

		OWLDataProperty name = factory.getOWLDataProperty("foaf:lastName", pm);
		OWLDataPropertyAssertionAxiom dataPropertyAssertion = factory
			.getOWLDataPropertyAssertionAxiom(name, me,
					"Soiland-Reyes");
		manager.addAxiom(ontology, dataPropertyAssertion);

		testFile = Files.createTempFile("prov", ".ttl");

		RDFXMLOntologyFormat rdfFormat = new RDFXMLOntologyFormat();
		rdfFormat.copyPrefixesFrom(pm);
		rdfFormat.setDefaultPrefix(ontologyIRI + "#");

		TurtleOntologyFormat turtleFormat = new TurtleOntologyFormat();
		turtleFormat.copyPrefixesFrom(pm);
		turtleFormat.setDefaultPrefix(ontologyIRI + "#");

		try (OutputStream outputStream = Files.newOutputStream(testFile)) {
			manager.saveOntology(ontology, turtleFormat,				
					outputStream);
		}
		System.out.println(testFile);
	}

	//@Test
	public void loadFile() throws Exception {
		if (testFile == null) { 
			// Run save first
			saveFile();
		}
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		
		// Create ontology with two imports
		IRI provIri = IRI.create("http://www.w3.org/ns/prov");
		OWLOntology prov = manager.loadOntology(provIri);
		IRI foafIri = IRI.create("http://xmlns.com/foaf/0.1");
		OWLOntology foaf = manager.loadOntology(foafIri);

		
		OWLDataFactory factory = manager.getOWLDataFactory();

		DefaultPrefixManager pm = new DefaultPrefixManager();
		pm.setPrefix("foaf:", "http://xmlns.com/foaf/0.1/");
		pm.setPrefix("prov:", "http://www.w3.org/ns/prov#");

		OWLClass entityClass = factory.getOWLClass("prov:Entity", pm);

		OWLOntology ontology;
		try (InputStream inStream = Files.newInputStream(testFile)) {
			ontology = manager.loadOntologyFromOntologyDocument(inStream);
			// Don't  load from the File - as it will make URIs absolute 
			// when loading from RDF/XML (fine from Turtle..)
		}
		
		for (OWLIndividual entity : entityClass.getIndividuals(ontology)) {
			System.out.println("Entity " + entity);
			assertEquals("index.html", entity.asOWLNamedIndividual().getIRI()
					.toString());
			OWLObjectProperty wasAttributedTo = factory.getOWLObjectProperty(
					"prov:wasAttributedTo", pm);
			Set<OWLIndividual> attributed = entity.getObjectPropertyValues(
					wasAttributedTo, ontology);
			OWLIndividual me = attributed.iterator().next();
			
			System.out.println("  prov:wasAttributedTo " + me);
			assertEquals("http://soiland-reyes.com/stian/#me", me
					.asOWLNamedIndividual().getIRI().toString());

			OWLClass personClass = factory.getOWLClass("foaf:Person", pm);
			assertTrue(me.getTypes(ontology).contains(personClass));

			
		
			OWLDataProperty name = factory.getOWLDataProperty("foaf:lastName", pm);
			assertEquals("Soiland-Reyes",
					me.getDataPropertyValues(name, ontology).iterator().next()
							.getLiteral());

		}		
	}
	*/

}
