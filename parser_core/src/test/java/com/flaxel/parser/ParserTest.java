package com.flaxel.parser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {

	@Test
	public void testSetPrinter() {
		Function<CompilationUnit, String> printer = (unit) -> "test";
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
		File file = new File(getClass().getClassLoader().getResource("com/flaxel/parser/Analyzed.txt").getFile());
		File folder = new File(getClass().getClassLoader().getResource("com/flaxel/parser/analyzed").getFile());
		File archive = new File(getClass().getClassLoader().getResource("com/flaxel/parser/Analyzed.zip").getFile());

		Parser.createTypeSolver(file, folder, archive);
	}

	@Test
	public void testCreateTypeSolverPath() throws URISyntaxException, IOException {
		Path file = Paths.get(getClass().getClassLoader().getResource("com/flaxel/parser/Analyzed.txt").toURI());
		Path folder = Paths.get(getClass().getClassLoader().getResource("com/flaxel/parser/analyzed").toURI());
		Path archive = Paths.get(getClass().getClassLoader().getResource("com/flaxel/parser/Analyzed.zip").toURI());

		Parser.createTypeSolver(file, folder, archive);
	}
}
