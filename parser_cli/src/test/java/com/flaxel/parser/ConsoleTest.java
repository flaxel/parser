package com.flaxel.parser;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.flaxel.parser.command.ParserCommand;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConsoleTest {

	@Test
	public void testInit() throws IOException {
		Console.init("test", new ParserCommand());
	}

	@Test
	public void testInitNonSuccessful() {
		ParserCommand command = new ParserCommand();
		assertThrows(AssertionError.class, () -> Console.init(null, command));
		assertThrows(AssertionError.class, () -> Console.init("", command));
		assertThrows(AssertionError.class, () -> Console.init("test", null));
	}

	@Test
	public void testReadNonSuccessful() throws IOException {
		assertThrows(AssertionError.class, () -> Console.read(null));
	}

	@Test
	public void testPrint() throws IOException {
		Console.init("test", new ParserCommand());
		Console.print("test");
	}

	@Test
	public void testPrintNonSuccessful() {
		assertThrows(AssertionError.class, () -> Console.print(null));
		assertThrows(AssertionError.class, () -> Console.print(""));
	}

	@Test
	public void testWriter() throws IOException {
		Console.init("test", new ParserCommand());
		assertNotNull(Console.getWriter());
	}

	@Test
	public void testOutput() throws IOException {
		Console.init("test", new ParserCommand());
		assertNotNull(Console.getOutput());
	}
}
