package com.gtarc.network.knowledgebase.model.rdfreactor.generator;
import org.junit.Test;
import org.ontoware.rdf2go.Reasoning;
import org.ontoware.rdfreactor.generator.CodeGenerator;

public class IscoModelGeneratorTest {

	public static final String outdir = "target/generated-test-sources/rdfs-classes";

	@Test
	public void testGeneration() throws Exception {
		//String rdfFileName = "src/test/resources/test001.n3";
		String rdfFileName = "/home/dang/data/src/80_isco_ws/network-knowledge-base/ontologies/data/samples/isco/isco-sample-1.rdf";

		//CodeGenerator.generate(rdfFileName,	outdir, "com.example.modelgeneratortest", Reasoning.owl, true);
		CodeGenerator.generate(rdfFileName,	outdir, "com.gtarc.network.knowledgebase.model.rdfreactor", Reasoning.rdfs, true, "", "Owl");
	}
}

