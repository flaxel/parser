package com.flaxel.parser.handler.transform;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.flaxel.parser.handler.analyze.FindHandler;
import com.flaxel.parser.utils.TestUtils;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.printer.YamlPrinter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FindHandlerTest {

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
	@Disabled
	public void testPrinter() throws IOException, URISyntaxException {
		new FindHandler(new FileOutputStream(target), "Main").printer(new YamlPrinter(true)::output)
				.accept(source, StaticJavaParser.parse(source));
		assertEquals(Files.readAllLines(TestUtils.getInternPath("handler/unit/Findverbose.txt")),
				Files.readAllLines(target.toPath()));
	}

	@Test
	@Disabled
	public void test() throws IOException, URISyntaxException {
		new FindHandler(new FileOutputStream(target), "Main").accept(source, StaticJavaParser.parse(source));
		assertEquals(Files.readAllLines(TestUtils.getInternPath("handler/unit/RenameVerbose.txt")),
				Files.readAllLines(target.toPath()));
	}

	@Test
	public void testNonSuccessful() {
		File file = TestUtils.getInternFile("handler/unit/RenameSource.txt");

		assertThrows(AssertionError.class, () -> new FindHandler(System.out, ""));
		assertThrows(AssertionError.class, () -> new FindHandler(null, "test"));

		assertThrows(AssertionError.class, () -> new FindHandler(null, Pattern.compile("test")));

		assertThrows(AssertionError.class, () -> new FindHandler(System.out, "test").accept(file, null));
		assertThrows(AssertionError.class,
				() -> new FindHandler(System.out, "test").accept(null, StaticJavaParser.parse(file)));
	}
}
