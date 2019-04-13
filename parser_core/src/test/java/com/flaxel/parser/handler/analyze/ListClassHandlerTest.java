package com.flaxel.parser.handler.analyze;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.flaxel.parser.Parser;
import com.flaxel.parser.utils.TestUtils;
import com.github.javaparser.StaticJavaParser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ListClassHandlerTest {

	private File target;

	private File source;

	@BeforeEach
	private void setup() throws IOException {
		target = new File("target.txt");
		Files.deleteIfExists(target.toPath());
		target.deleteOnExit();

		source = TestUtils.getInternFile("handler/unit/ListClassSource.txt");
		StaticJavaParser.getConfiguration().setSymbolResolver(Parser.createTypeSolver(source));
	}

	@Test
	public void testSeparator() throws URISyntaxException, IOException {
		new ListClassHandler(Files.newOutputStream(target.toPath())).separator('%')
				.accept(source, StaticJavaParser.parse(source));
		assertEquals(Files.readAllLines(TestUtils.getInternPath("handler/unit/ListClassSeparator.txt")),
				Files.readAllLines(target.toPath()));
	}

	@Test
	@Disabled
	public void testVerbose() throws URISyntaxException, IOException {
		new ListClassHandler(Files.newOutputStream(target.toPath())).verbose(true)
				.accept(source, StaticJavaParser.parse(source));
		assertEquals(Files.readAllLines(TestUtils.getInternPath("handler/unit/ListClassVerbose.txt")),
				Files.readAllLines(target.toPath()));
	}

	@Test
	public void test() throws IOException, URISyntaxException {
		new ListClassHandler(new FileOutputStream(target)).accept(source, StaticJavaParser.parse(source));
		assertEquals(Files.readAllLines(TestUtils.getInternPath("handler/unit/ListClass.txt")),
				Files.readAllLines(target.toPath()));
	}

	@Test
	public void testNonSuccessful() {
		File file = TestUtils.getInternFile("handler/unit/ListClassSource.txt");

		assertThrows(AssertionError.class, () -> new ListClassHandler(null));

		assertThrows(AssertionError.class, () -> new ListClassHandler(System.out).separator(""));
		assertThrows(AssertionError.class, () -> new ListClassHandler(System.out).separator(null));

		assertThrows(AssertionError.class, () -> new ListClassHandler(System.out).accept(file, null));
		assertThrows(AssertionError.class,
				() -> new ListClassHandler(System.out).accept(null, StaticJavaParser.parse(file)));
	}
}
