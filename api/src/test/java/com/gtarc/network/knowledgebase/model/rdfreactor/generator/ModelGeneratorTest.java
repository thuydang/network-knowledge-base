package com.gtarc.network.knowledgebase.model.rdfreactor.generator;
import org.junit.Test;
import org.ontoware.rdf2go.Reasoning;
import org.ontoware.rdfreactor.generator.CodeGenerator;

public class ModelGeneratorTest {

	public static final String outdir = "target/generated-test-sources/rdfs-classes";

	//@Test
	public void testGeneration() throws Exception {
		//String rdfFileName = "src/test/resources/test001.n3";
		String rdfFileName = "src/test/resources/sample.ttl";

		//CodeGenerator.generate(rdfFileName,	outdir, "com.example.modelgeneratortest", Reasoning.owl, true);
		CodeGenerator.generate(rdfFileName,	outdir, "com.example.modelgeneratortest", Reasoning.rdfs, true);
	}
}

