package com.flaxel.parser.handler.unit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.flaxel.parser.Parser;
import com.flaxel.parser.utils.TestUtils;
import com.github.javaparser.StaticJavaParser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ListMethodCallHandlerTest {

	private File target;

	private File source;

	@BeforeEach
	public void setup() throws IOException {
		target = new File("target.txt");
		Files.deleteIfExists(target.toPath());
		target.deleteOnExit();

		source = TestUtils.getInternFile("handler/unit/ListMethodCallSource.txt");
		StaticJavaParser.getConfiguration().setSymbolResolver(Parser.createTypeSolver(source));
	}

	@Test
	public void testIgnoringAnnotations() throws IOException, URISyntaxException {
		new ListMethodCallHandler(new FileOutputStream(target)).ignoringAnnotations(true)
				.accept(source, StaticJavaParser.parse(source));

		assertEquals(Files.readAllLines(TestUtils.getInternPath("handler/unit/ListMethodCall.txt")),
				Files.readAllLines(target.toPath()));
	}

	@Test
	public void test() throws IOException, URISyntaxException {
		new ListMethodCallHandler(new FileOutputStream(target)).accept(source, StaticJavaParser.parse(source));

		assertEquals(Files.readAllLines(TestUtils.getInternPath("handler/unit/ListMethodCall.txt")),
				Files.readAllLines(target.toPath()));
	}

	@Test
	public void testNonSuccessful() {
		File file = TestUtils.getInternFile("handler/unit/ListMethodCallSource.txt");

		assertThrows(AssertionError.class, () -> new ListMethodCallHandler(null));

		assertThrows(AssertionError.class, () -> new ListMethodCallHandler(System.out).accept(file, null));
		assertThrows(AssertionError.class,
				() -> new ListMethodCallHandler(System.out).accept(null, StaticJavaParser.parse(file)));
	}
}
