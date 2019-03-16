package com.flaxel.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import com.flaxel.parser.utils.TestUtils;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AnalyzerTest {

	private static final BiConsumer<File, CompilationUnit> DEFAULT_FILE_UNIT_HANDLER = (file, unit) -> {};

	private static final Predicate<File> DEFAULT_NON_FILE_FILTER = (file) -> false;

	private static final Predicate<CompilationUnit> DEFAULT_NON_UNIT_FILTER = (file) -> false;

	@Test
	public void testAnalyzeFileNull() {
		File nullFile = null;
		File file = TestUtils.getInternFile("Analyzed.txt");

		assertThrows(AssertionError.class, () -> Analyzer.analyzeFile(file, Analyzer.DEFAULT_CHARSET,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, null));
		assertThrows(AssertionError.class,
				() -> Analyzer.analyzeFile(file, Analyzer.DEFAULT_CHARSET, null, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeFile(file, null, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER,
				DEFAULT_FILE_UNIT_HANDLER));

		assertThrows(AssertionError.class, () -> Analyzer.analyzeFile(nullFile, Analyzer.DEFAULT_CHARSET,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class,
				() -> Analyzer.analyzeFile(nullFile, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class,
				() -> Analyzer.analyzeFile(nullFile, Analyzer.DEFAULT_CHARSET, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeFile(nullFile, DEFAULT_FILE_UNIT_HANDLER));
	}

	@Test
	public void testAnalyzeFileNotExist() {
		File file = new File("test_analyze.txt");

		assertThrows(FileNotFoundException.class, () -> Analyzer.analyzeFile(file, Analyzer.DEFAULT_CHARSET,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class,
				() -> Analyzer.analyzeFile(file, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class,
				() -> Analyzer.analyzeFile(file, Analyzer.DEFAULT_CHARSET, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class, () -> Analyzer.analyzeFile(file, DEFAULT_FILE_UNIT_HANDLER));
	}

	@Test
	public void testAnalyzeFileNotFile() {
		File folder = new File("src");

		assertThrows(FileNotFoundException.class, () -> Analyzer.analyzeFile(folder, Analyzer.DEFAULT_CHARSET,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class,
				() -> Analyzer.analyzeFile(folder, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class,
				() -> Analyzer.analyzeFile(folder, Analyzer.DEFAULT_CHARSET, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class, () -> Analyzer.analyzeFile(folder, DEFAULT_FILE_UNIT_HANDLER));
	}

	@Test
	public void testAnalyzeFile() throws FileNotFoundException {
		File file = TestUtils.getInternFile("Analyzed.txt");

		Analyzer.analyzeFile(file, Analyzer.DEFAULT_CHARSET, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER,
				DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeFile(file, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeFile(file, Analyzer.DEFAULT_CHARSET, DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeFile(file, DEFAULT_FILE_UNIT_HANDLER);

		File nonFile = new File(getClass().getClassLoader().getResource("com/flaxel/parser/NoAnalyzed.txt").getFile());
		Analyzer.analyzeFile(nonFile, DEFAULT_FILE_UNIT_HANDLER);
	}

	@Test
	public void testAnalyzePathNull() throws URISyntaxException {
		Path nullPath = null;
		Path path = TestUtils.getInternPath("Analyzed.txt");

		assertThrows(AssertionError.class, () -> Analyzer.analyzeFile(path, Analyzer.DEFAULT_CHARSET,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, null));
		assertThrows(AssertionError.class,
				() -> Analyzer.analyzeFile(path, Analyzer.DEFAULT_CHARSET, null, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeFile(path, null, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER,
				DEFAULT_FILE_UNIT_HANDLER));

		assertThrows(AssertionError.class, () -> Analyzer.analyzeFile(nullPath, Analyzer.DEFAULT_CHARSET,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class,
				() -> Analyzer.analyzeFile(nullPath, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class,
				() -> Analyzer.analyzeFile(nullPath, Analyzer.DEFAULT_CHARSET, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeFile(nullPath, DEFAULT_FILE_UNIT_HANDLER));
	}

	@Test
	public void testAnalyzePathNotExist() {
		Path path = new File("test_analyze.txt").toPath();

		assertThrows(FileNotFoundException.class, () -> Analyzer.analyzeFile(path, Analyzer.DEFAULT_CHARSET,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class,
				() -> Analyzer.analyzeFile(path, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class,
				() -> Analyzer.analyzeFile(path, Analyzer.DEFAULT_CHARSET, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class, () -> Analyzer.analyzeFile(path, DEFAULT_FILE_UNIT_HANDLER));
	}

	@Test
	public void testAnalyzePathNotFile() {
		Path folder = new File("src").toPath();

		assertThrows(FileNotFoundException.class, () -> Analyzer.analyzeFile(folder, Analyzer.DEFAULT_CHARSET,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class,
				() -> Analyzer.analyzeFile(folder, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class,
				() -> Analyzer.analyzeFile(folder, Analyzer.DEFAULT_CHARSET, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class, () -> Analyzer.analyzeFile(folder, DEFAULT_FILE_UNIT_HANDLER));
	}

	@Test
	public void testAnalyzePath() throws FileNotFoundException, URISyntaxException {
		Path path = TestUtils.getInternPath("Analyzed.txt");

		Analyzer.analyzeFile(path, Analyzer.DEFAULT_CHARSET, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER,
				DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeFile(path, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeFile(path, Analyzer.DEFAULT_CHARSET, DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeFile(path, DEFAULT_FILE_UNIT_HANDLER);

		Path nonPath = Paths.get(getClass().getClassLoader().getResource("com/flaxel/parser/NoAnalyzed.txt").toURI());
		Analyzer.analyzeFile(nonPath, DEFAULT_FILE_UNIT_HANDLER);
	}

	@Test
	public void testAnalyzeZipFileNull() {
		File nullFile = null;
		File file = TestUtils.getInternFile("Analyzed.zip");

		assertThrows(AssertionError.class, () -> Analyzer.analyzeZip(nullFile, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeZip(nullFile, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeZip(nullFile, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeZip(nullFile, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class,
				() -> Analyzer.analyzeZip(nullFile, Analyzer.DEFAULT_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class,
				() -> Analyzer.analyzeZip(nullFile, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class,
				() -> Analyzer.analyzeZip(nullFile, DEFAULT_FILE_UNIT_HANDLER, Analyzer.DEFAULT_UNIT_FILTER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeZip(nullFile, DEFAULT_FILE_UNIT_HANDLER));

		assertThrows(AssertionError.class, () -> Analyzer.analyzeZip(file, null, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeZip(file, Analyzer.DEFAULT_FILE_FILTER, null,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeZip(file, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, null, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeZip(file, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER, null));
	}

	@Test
	public void testAnalyzeZipFileNotExist() {
		File file = new File("test_analyze.zip");

		assertThrows(NoSuchFileException.class, () -> Analyzer.analyzeZip(file, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(NoSuchFileException.class, () -> Analyzer.analyzeZip(file, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(NoSuchFileException.class, () -> Analyzer.analyzeZip(file, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(NoSuchFileException.class, () -> Analyzer.analyzeZip(file, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(NoSuchFileException.class,
				() -> Analyzer.analyzeZip(file, Analyzer.DEFAULT_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(NoSuchFileException.class,
				() -> Analyzer.analyzeZip(file, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(NoSuchFileException.class,
				() -> Analyzer.analyzeZip(file, DEFAULT_FILE_UNIT_HANDLER, Analyzer.DEFAULT_UNIT_FILTER));
		assertThrows(NoSuchFileException.class, () -> Analyzer.analyzeZip(file, DEFAULT_FILE_UNIT_HANDLER));
	}

	@Test
	public void testAnalyzeZipFileNotFile() {
		File folder = new File("src");

		assertThrows(FileNotFoundException.class, () -> Analyzer.analyzeZip(folder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class, () -> Analyzer.analyzeZip(folder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class, () -> Analyzer.analyzeZip(folder,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class, () -> Analyzer.analyzeZip(folder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class,
				() -> Analyzer.analyzeZip(folder, Analyzer.DEFAULT_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class,
				() -> Analyzer.analyzeZip(folder, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class,
				() -> Analyzer.analyzeZip(folder, DEFAULT_FILE_UNIT_HANDLER, Analyzer.DEFAULT_UNIT_FILTER));
		assertThrows(FileNotFoundException.class, () -> Analyzer.analyzeZip(folder, DEFAULT_FILE_UNIT_HANDLER));
	}

	@Test
	public void testAnalyzeZipFile() throws IOException {
		File file = TestUtils.getInternFile("Analyzed.zip");

		Analyzer.analyzeZip(file, Analyzer.DEFAULT_FILE_FILTER, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeZip(file, Analyzer.DEFAULT_FILE_FILTER, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER,
				DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeZip(file, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER,
				DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeZip(file, Analyzer.DEFAULT_FILE_FILTER, Analyzer.DEFAULT_UNIT_FILTER,
				DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeZip(file, Analyzer.DEFAULT_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeZip(file, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeZip(file, DEFAULT_FILE_UNIT_HANDLER, Analyzer.DEFAULT_UNIT_FILTER);
		Analyzer.analyzeZip(file, DEFAULT_FILE_UNIT_HANDLER);

		Analyzer.analyzeZip(file, DEFAULT_NON_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeZip(file, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeZip(file, DEFAULT_FILE_UNIT_HANDLER, DEFAULT_NON_UNIT_FILTER);
	}

	@Test
	public void testAnalyzeZipPathNull() throws URISyntaxException {
		Path nullPath = null;
		Path path = TestUtils.getInternPath("Analyzed.zip");

		assertThrows(AssertionError.class, () -> Analyzer.analyzeZip(nullPath, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeZip(nullPath, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeZip(nullPath, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeZip(nullPath, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class,
				() -> Analyzer.analyzeZip(nullPath, Analyzer.DEFAULT_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class,
				() -> Analyzer.analyzeZip(nullPath, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class,
				() -> Analyzer.analyzeZip(nullPath, DEFAULT_FILE_UNIT_HANDLER, Analyzer.DEFAULT_UNIT_FILTER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeZip(nullPath, DEFAULT_FILE_UNIT_HANDLER));

		assertThrows(AssertionError.class, () -> Analyzer.analyzeZip(path, null, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeZip(path, Analyzer.DEFAULT_FILE_FILTER, null,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeZip(path, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, null, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeZip(path, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER, null));
	}

	@Test
	public void testAnalyzeZipPathNotExist() {
		Path path = new File("test_analyze.zip").toPath();

		assertThrows(NoSuchFileException.class, () -> Analyzer.analyzeZip(path, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(NoSuchFileException.class, () -> Analyzer.analyzeZip(path, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(NoSuchFileException.class, () -> Analyzer.analyzeZip(path, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(NoSuchFileException.class, () -> Analyzer.analyzeZip(path, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(NoSuchFileException.class,
				() -> Analyzer.analyzeZip(path, Analyzer.DEFAULT_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(NoSuchFileException.class,
				() -> Analyzer.analyzeZip(path, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(NoSuchFileException.class,
				() -> Analyzer.analyzeZip(path, DEFAULT_FILE_UNIT_HANDLER, Analyzer.DEFAULT_UNIT_FILTER));
		assertThrows(NoSuchFileException.class, () -> Analyzer.analyzeZip(path, DEFAULT_FILE_UNIT_HANDLER));
	}

	@Test
	public void testAnalyzeZipPathNotFile() {
		Path folder = new File("src").toPath();

		assertThrows(FileNotFoundException.class, () -> Analyzer.analyzeZip(folder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class, () -> Analyzer.analyzeZip(folder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class, () -> Analyzer.analyzeZip(folder,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class, () -> Analyzer.analyzeZip(folder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class,
				() -> Analyzer.analyzeZip(folder, Analyzer.DEFAULT_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class,
				() -> Analyzer.analyzeZip(folder, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class,
				() -> Analyzer.analyzeZip(folder, DEFAULT_FILE_UNIT_HANDLER, Analyzer.DEFAULT_UNIT_FILTER));
		assertThrows(FileNotFoundException.class, () -> Analyzer.analyzeZip(folder, DEFAULT_FILE_UNIT_HANDLER));
	}

	@Test
	public void testAnalyzeZipPath() throws IOException, URISyntaxException {
		Path path = TestUtils.getInternPath("Analyzed.zip");

		Analyzer.analyzeZip(path, Analyzer.DEFAULT_FILE_FILTER, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeZip(path, Analyzer.DEFAULT_FILE_FILTER, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER,
				DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeZip(path, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER,
				DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeZip(path, Analyzer.DEFAULT_FILE_FILTER, Analyzer.DEFAULT_UNIT_FILTER,
				DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeZip(path, Analyzer.DEFAULT_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeZip(path, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeZip(path, DEFAULT_FILE_UNIT_HANDLER, Analyzer.DEFAULT_UNIT_FILTER);
		Analyzer.analyzeZip(path, DEFAULT_FILE_UNIT_HANDLER);

		Analyzer.analyzeZip(path, DEFAULT_NON_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeZip(path, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeZip(path, DEFAULT_FILE_UNIT_HANDLER, DEFAULT_NON_UNIT_FILTER);
	}

	@Test
	public void testAnalyzeFolderFileNull() {
		File nullFolder = null;
		File folder = TestUtils.getInternFile("analyzed");

		assertThrows(AssertionError.class, () -> Analyzer.analyzeFolder(nullFolder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeFolder(nullFolder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeFolder(nullFolder,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeFolder(nullFolder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class,
				() -> Analyzer.analyzeFolder(nullFolder, Analyzer.DEFAULT_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeFolder(nullFolder,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class,
				() -> Analyzer.analyzeFolder(nullFolder, DEFAULT_FILE_UNIT_HANDLER, Analyzer.DEFAULT_UNIT_FILTER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeFolder(nullFolder, DEFAULT_FILE_UNIT_HANDLER));

		assertThrows(AssertionError.class, () -> Analyzer.analyzeFolder(folder, null,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER, null,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, null, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER, null));
	}

	@Test
	public void testAnalyzeFolderFileNotExist() {
		File folder = new File("test_analyze");

		assertThrows(IllegalArgumentException.class, () -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Analyzer.analyzeFolder(folder,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class,
				() -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class,
				() -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class,
				() -> Analyzer.analyzeFolder(folder, DEFAULT_FILE_UNIT_HANDLER, Analyzer.DEFAULT_UNIT_FILTER));
		assertThrows(IllegalArgumentException.class, () -> Analyzer.analyzeFolder(folder, DEFAULT_FILE_UNIT_HANDLER));
	}

	@Test
	public void testAnalyzeFolderFileNotFolder() {
		File folder = TestUtils.getInternFile("Analyzed.zip");

		assertThrows(IllegalArgumentException.class, () -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Analyzer.analyzeFolder(folder,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class,
				() -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class,
				() -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class,
				() -> Analyzer.analyzeFolder(folder, DEFAULT_FILE_UNIT_HANDLER, Analyzer.DEFAULT_UNIT_FILTER));
		assertThrows(IllegalArgumentException.class, () -> Analyzer.analyzeFolder(folder, DEFAULT_FILE_UNIT_HANDLER));
	}

	@Test
	public void testAnalyzeFolderFile() throws IOException {
		File folder = TestUtils.getInternFile("analyzed");

		Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER,
				DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER,
				DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER, Analyzer.DEFAULT_UNIT_FILTER,
				DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeFolder(folder, DEFAULT_FILE_UNIT_HANDLER, Analyzer.DEFAULT_UNIT_FILTER);
		Analyzer.analyzeFolder(folder, DEFAULT_FILE_UNIT_HANDLER);

		Analyzer.analyzeFolder(folder, DEFAULT_NON_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeFolder(folder, DEFAULT_FILE_UNIT_HANDLER, DEFAULT_NON_UNIT_FILTER);
	}

	@Test
	public void testAnalyzeFolderPathNull() throws URISyntaxException {
		Path nullFolder = null;
		Path folder = TestUtils.getInternPath("analyzed");

		assertThrows(AssertionError.class, () -> Analyzer.analyzeFolder(nullFolder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeFolder(nullFolder,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeFolder(nullFolder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class,
				() -> Analyzer.analyzeFolder(nullFolder, Analyzer.DEFAULT_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeFolder(nullFolder,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class,
				() -> Analyzer.analyzeFolder(nullFolder, DEFAULT_FILE_UNIT_HANDLER, Analyzer.DEFAULT_UNIT_FILTER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeFolder(nullFolder, DEFAULT_FILE_UNIT_HANDLER));

		assertThrows(AssertionError.class, () -> Analyzer.analyzeFolder(folder, null,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER, null,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, null, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER, null));
	}

	@Test
	public void testAnalyzeFolderPathNotExist() {
		Path folder = new File("test_analyze").toPath();

		assertThrows(IllegalArgumentException.class, () -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Analyzer.analyzeFolder(folder,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class,
				() -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class,
				() -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class,
				() -> Analyzer.analyzeFolder(folder, DEFAULT_FILE_UNIT_HANDLER, Analyzer.DEFAULT_UNIT_FILTER));
		assertThrows(IllegalArgumentException.class, () -> Analyzer.analyzeFolder(folder, DEFAULT_FILE_UNIT_HANDLER));
	}

	@Test
	public void testAnalyzeFolderPathNotFolder() throws URISyntaxException {
		Path folder = TestUtils.getInternPath("Analyzed.zip");

		assertThrows(IllegalArgumentException.class, () -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Analyzer.analyzeFolder(folder,
				Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class,
				() -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class,
				() -> Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class,
				() -> Analyzer.analyzeFolder(folder, DEFAULT_FILE_UNIT_HANDLER, Analyzer.DEFAULT_UNIT_FILTER));
		assertThrows(IllegalArgumentException.class, () -> Analyzer.analyzeFolder(folder, DEFAULT_FILE_UNIT_HANDLER));
	}

	@Test
	public void testAnalyzeFolderPath() throws IOException, URISyntaxException {
		Path folder = TestUtils.getInternPath("analyzed");

		Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER,
				Analyzer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER,
				DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, Analyzer.DEFAULT_UNIT_FILTER,
				DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER, Analyzer.DEFAULT_UNIT_FILTER,
				DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeFolder(folder, DEFAULT_FILE_UNIT_HANDLER, Analyzer.DEFAULT_UNIT_FILTER);
		Analyzer.analyzeFolder(folder, DEFAULT_FILE_UNIT_HANDLER);

		Analyzer.analyzeFolder(folder, DEFAULT_NON_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeFolder(folder, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER);
		Analyzer.analyzeFolder(folder, DEFAULT_FILE_UNIT_HANDLER, DEFAULT_NON_UNIT_FILTER);
	}

	@Test
	public void testSetConfigurationNull() {
		assertThrows(AssertionError.class, () -> Analyzer.setConfiguration(null));
	}

	@Test
	public void testSetGetConfiguration() {
		ParserConfiguration configuration = new ParserConfiguration().setTabSize(2);
		Analyzer.setConfiguration(configuration);

		assertEquals(configuration, Analyzer.getConfiguration());
		assertNotEquals(new ParserConfiguration(), Analyzer.getConfiguration());
	}
}
