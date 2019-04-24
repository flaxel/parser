package com.flaxel.parser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import com.flaxel.parser.utils.TestUtils;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {

	@Test
	public void testSetPrinter() {
		Function<Node, String> printer = (node) -> "test";
		Parser.setPrinter(printer);

		assertEquals(printer, Generator.getPrinter());
	}

	@Test
	public void testSetConfiguration() {
		ParserConfiguration configuration = new ParserConfiguration().setTabSize(3);
		Parser.setConfiguration(configuration);

		assertEquals(configuration, StaticJavaParser.getConfiguration());
		assertEquals(configuration, Analyzer.getConfiguration());
		assertEquals(configuration, Transformer.getConfiguration());
	}

	@Test
	public void testCreateTypeSolverFile() throws IOException {
		File file = TestUtils.getInternFile("Analyzed.txt");
		File folder = TestUtils.getInternFile("analyzed");
		File archive = TestUtils.getInternFile("Analyzed.zip");

		Parser.createTypeSolver(file, folder, archive);
	}

	@Test
	public void testCreateTypeSolverPath() throws URISyntaxException, IOException {
		Path file = TestUtils.getInternPath("Analyzed.txt");
		Path folder = TestUtils.getInternPath("analyzed");
		Path archive = TestUtils.getInternPath("Analyzed.zip");

		Parser.createTypeSolver(file, folder, archive);
	}

	@Test
	public void testCreateClassLoaderFile() throws IOException {
		File file = TestUtils.getInternFile("Analyzed.txt");
		File folder = TestUtils.getInternFile("analyzed");
		File archive = TestUtils.getInternFile("Analyzed.zip");

		Parser.createClassLoader(file, folder, archive);
	}

	@Test
	public void testCreateClassLoaderPath() throws URISyntaxException, IOException {
		Path file = TestUtils.getInternPath("Analyzed.txt");
		Path folder = TestUtils.getInternPath("analyzed");
		Path archive = TestUtils.getInternPath("Analyzed.zip");

		Parser.createClassLoader(file, folder, archive);
	}
}
