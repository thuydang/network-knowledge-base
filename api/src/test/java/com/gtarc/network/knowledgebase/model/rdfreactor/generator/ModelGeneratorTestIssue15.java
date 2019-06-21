package com.gtarc.network.knowledgebase.model.rdfreactor.generator;
import org.junit.Test;
import org.ontoware.rdf2go.Reasoning;
import org.ontoware.rdfreactor.generator.CodeGenerator;

public class ModelGeneratorTestIssue15 {

	public static final String outdir = "target/generated-test-sources/rdfs-classes";

	   //@Test
	   public void testReac15() throws Exception {
	   
	   CodeGenerator.generate("./src/test/resources/reac15.rdfs", outdir,
	   "org.ontoware.rdfreactor.test.reac15", "RDFS", true, true, "");
	   //"org.ontoware.rdfreactor.test.reac15", Reasoning.rdfs, true);
	   
	   }
}

