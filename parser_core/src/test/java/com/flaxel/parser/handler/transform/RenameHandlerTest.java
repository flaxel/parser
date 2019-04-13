package com.flaxel.parser.handler.transform;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.flaxel.parser.handler.transform.RenameHandler;
import com.flaxel.parser.utils.TestUtils;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.printer.YamlPrinter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RenameHandlerTest {

	private File target;

	private File source;

	@BeforeEach
	public void setup() throws IOException {
		target = new File("target.txt");
		Files.deleteIfExists(target.toPath());
		target.deleteOnExit();

		source = TestUtils.getInternFile("handler/unit/RenameSource.txt");
	}

	@Test
	public void testPrinter() throws IOException, URISyntaxException {
		new RenameHandler(new FileOutputStream(target), "Main", (old) -> "Test").printer(new YamlPrinter(true)::output)
				.accept(source, StaticJavaParser.parse(source));
		assertEquals(Files.readAllLines(TestUtils.getInternPath("handler/unit/RenamePrinter.txt")),
				Files.readAllLines(target.toPath()));
	}

	@Test
	public void testVerbose() throws IOException, URISyntaxException {
		new RenameHandler(new FileOutputStream(target), "Main", (old) -> "Test").verbose(true)
				.accept(source, StaticJavaParser.parse(source));
		assertEquals(Files.readAllLines(TestUtils.getInternPath("handler/unit/RenameVerbose.txt")),
				Files.readAllLines(target.toPath()));
	}

	@Test
	public void test() throws IOException, URISyntaxException {
		new RenameHandler(new FileOutputStream(target), "Main", (old) -> "Test").accept(source,
				StaticJavaParser.parse(source));
		assertEquals(Files.readAllLines(TestUtils.getInternPath("handler/unit/Rename.txt")),
				Files.readAllLines(target.toPath()));
	}

	@Test
	public void testNonSuccessful() {
		File file = TestUtils.getInternFile("handler/unit/RenameSource.txt");

		assertThrows(AssertionError.class, () -> new RenameHandler("", (oldValue) -> "test"));
		assertThrows(AssertionError.class, () -> new RenameHandler("test", null));

		assertThrows(AssertionError.class, () -> new RenameHandler(System.out, "", (oldValue) -> "test"));
		assertThrows(AssertionError.class, () -> new RenameHandler(System.out, "test", null));

		assertThrows(AssertionError.class, () -> new RenameHandler(Pattern.compile("test"), null));
		assertThrows(AssertionError.class, () -> new RenameHandler(System.out, Pattern.compile("test"), null));

		assertThrows(AssertionError.class, () -> new RenameHandler("test", (oldValue) -> "test").accept(file, null));
		assertThrows(AssertionError.class,
				() -> new RenameHandler("test", (oldValue) -> "test").accept(null, StaticJavaParser.parse(file)));
	}
}
