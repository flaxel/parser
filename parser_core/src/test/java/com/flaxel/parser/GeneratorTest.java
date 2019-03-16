package com.flaxel.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.flaxel.parser.utils.TestUtils;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.PrettyPrinter;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GeneratorTest {

	private Supplier<CompilationUnit> supplier;

	private Callable<CompilationUnit> callable;

	@BeforeEach
	public void setup() {
		CompilationUnit compilationUnit = new CompilationUnit();
		compilationUnit.addClass("TestClass").setPublic(true);

		supplier = () -> compilationUnit;
		callable = () -> compilationUnit;

		Generator.setPrinter(new PrettyPrinter()::print);
	}

	@Test
	public void testSupplierGenerate() throws IOException {
		String code = Generator.generate(supplier);
		assertEquals(TestUtils.readInternFile("PrettyGenerated.txt"), code);
	}

	@Test
	public void testCallableGenerate() throws Exception {
		String code = Generator.generate(callable);

		assertEquals(TestUtils.readInternFile("PrettyGenerated.txt"), code);
	}

	@Test
	public void testSupplierGenerateFile() throws IOException {
		File file = new File("test.txt");
		file.deleteOnExit();

		Generator.generate(supplier, file);

		assertTrue(file.exists());
	}

	@Test
	public void testCallableGenerateFile() throws Exception {
		File file = new File("test.txt");
		file.deleteOnExit();

		Generator.generate(callable, file);

		assertTrue(file.exists());
	}

	@Test
	public void testSupplierGenerateFileNull() throws IOException {
		File file = null;
		assertThrows(AssertionError.class, () -> Generator.generate(supplier, file));
	}

	@Test
	public void testCallableGenerateFileNull() throws Exception {
		File file = null;
		assertThrows(AssertionError.class, () -> Generator.generate(callable, file));
	}

	@Test
	public void testSupplierGeneratePath() throws IOException {
		File file = new File("test.txt");
		file.deleteOnExit();

		Generator.generate(supplier, file.toPath());

		assertTrue(file.exists());
	}

	@Test
	public void testCallableGeneratePath() throws Exception {
		File file = new File("test.txt");
		file.deleteOnExit();

		Generator.generate(callable, file.toPath());

		assertTrue(file.exists());
	}

	@Test
	public void testSupplierGeneratePathNull() throws IOException {
		Path path = null;
		assertThrows(NullPointerException.class, () -> Generator.generate(supplier, path));
	}

	@Test
	public void testCallableGeneratePathNull() throws Exception {
		Path path = null;
		assertThrows(NullPointerException.class, () -> Generator.generate(callable, path));
	}

	@Test
	public void testSetPrinter() throws IOException {
		Generator.setPrinter(LexicalPreservingPrinter::print);

		String code = Generator.generate(supplier);

		assertEquals(TestUtils.readInternFile("LexicalGenerated.txt"), code);
	}

	@Test
	public void testSetPrinterNull() {
		assertThrows(AssertionError.class, () -> Generator.setPrinter(null));
	}

	@Test
	public void testGetPrinter() {
		assertNotNull(Generator.getPrinter());
	}
}
