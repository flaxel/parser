package com.flaxel.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransformerTest {

	private static final Consumer<CompilationUnit> DEFAULT_UNIT_HANDLER = (unit) -> {};

	private static final BiConsumer<File, CompilationUnit> DEFAULT_FILE_UNIT_HANDLER = (file, unit) -> {};

	private static final Predicate<File> DEFAULT_NON_FILE_FILTER = (file) -> false;

	private static final Predicate<CompilationUnit> DEFAULT_NON_UNIT_FILTER = (file) -> false;

	@Test
	public void testTransformFileNull() {
		File nullFile = null;
		File file = new File(getClass().getClassLoader().getResource("com/flaxel/parser/Transformed.txt").getFile());

		assertThrows(AssertionError.class, () -> Transformer.transformFile(file, Transformer.DEFAULT_CHARSET,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, null));
		assertThrows(AssertionError.class,
				() -> Transformer.transformFile(file, Transformer.DEFAULT_CHARSET, null, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformFile(file, null,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));

		assertThrows(AssertionError.class, () -> Transformer.transformFile(nullFile, Transformer.DEFAULT_CHARSET,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformFile(nullFile,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class,
				() -> Transformer.transformFile(nullFile, Transformer.DEFAULT_CHARSET, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformFile(nullFile, DEFAULT_FILE_UNIT_HANDLER));
	}

	@Test
	public void testTransformFileNotExist() throws FileNotFoundException {
		File file = new File("test_transform.txt");
		assertThrows(FileNotFoundException.class, () -> Transformer.transformFile(file, Transformer.DEFAULT_CHARSET,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class, () -> Transformer.transformFile(file,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class,
				() -> Transformer.transformFile(file, Transformer.DEFAULT_CHARSET, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class, () -> Transformer.transformFile(file, DEFAULT_FILE_UNIT_HANDLER));
	}

	@Test
	public void testTransformFileNotFile() {
		File folder = new File("src");
		assertThrows(FileNotFoundException.class, () -> Transformer.transformFile(folder, Transformer.DEFAULT_CHARSET,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class, () -> Transformer.transformFile(folder,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class,
				() -> Transformer.transformFile(folder, Transformer.DEFAULT_CHARSET, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class, () -> Transformer.transformFile(folder, DEFAULT_FILE_UNIT_HANDLER));
	}

	@Test
	public void testTransformFile() throws FileNotFoundException {
		File file = new File(getClass().getClassLoader().getResource("com/flaxel/parser/Transformed.txt").getFile());
		Transformer.transformFile(file, Transformer.DEFAULT_CHARSET, Transformer.DEFAULT_FILE_PROBLEM_HANDLER,
				DEFAULT_FILE_UNIT_HANDLER);
		Transformer.transformFile(file, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER);
		Transformer.transformFile(file, Transformer.DEFAULT_CHARSET, DEFAULT_FILE_UNIT_HANDLER);
		Transformer.transformFile(file, DEFAULT_FILE_UNIT_HANDLER);

		File nonFile = new File(
				getClass().getClassLoader().getResource("com/flaxel/parser/NoTransformed.txt").getFile());
		Transformer.transformFile(nonFile, DEFAULT_FILE_UNIT_HANDLER);
	}

	@Test
	public void testTransformPathNull() throws URISyntaxException {
		Path nullPath = null;
		Path path = Paths.get(getClass().getClassLoader().getResource("com/flaxel/parser/Transformed.txt").toURI());

		assertThrows(AssertionError.class, () -> Transformer.transformFile(path, Transformer.DEFAULT_CHARSET,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, null));
		assertThrows(AssertionError.class,
				() -> Transformer.transformFile(path, Transformer.DEFAULT_CHARSET, null, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformFile(path, null,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));

		assertThrows(AssertionError.class, () -> Transformer.transformFile(nullPath, Transformer.DEFAULT_CHARSET,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformFile(nullPath,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class,
				() -> Transformer.transformFile(nullPath, Transformer.DEFAULT_CHARSET, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformFile(nullPath, DEFAULT_FILE_UNIT_HANDLER));
	}

	@Test
	public void testTransformPathNotExist() {
		Path path = new File("test_transform.txt").toPath();
		assertThrows(FileNotFoundException.class, () -> Transformer.transformFile(path, Transformer.DEFAULT_CHARSET,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class, () -> Transformer.transformFile(path,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class,
				() -> Transformer.transformFile(path, Transformer.DEFAULT_CHARSET, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class, () -> Transformer.transformFile(path, DEFAULT_FILE_UNIT_HANDLER));
	}

	@Test
	public void testTransformPathNotFile() {
		Path folder = new File("src").toPath();
		assertThrows(FileNotFoundException.class, () -> Transformer.transformFile(folder, Transformer.DEFAULT_CHARSET,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class, () -> Transformer.transformFile(folder,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class,
				() -> Transformer.transformFile(folder, Transformer.DEFAULT_CHARSET, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class, () -> Transformer.transformFile(folder, DEFAULT_FILE_UNIT_HANDLER));
	}

	@Test
	public void testTransformPath() throws FileNotFoundException, URISyntaxException {
		Path path = Paths.get(getClass().getClassLoader().getResource("com/flaxel/parser/Transformed.txt").toURI());
		Transformer.transformFile(path, Transformer.DEFAULT_CHARSET, Transformer.DEFAULT_FILE_PROBLEM_HANDLER,
				DEFAULT_FILE_UNIT_HANDLER);
		Transformer.transformFile(path, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER);
		Transformer.transformFile(path, Transformer.DEFAULT_CHARSET, DEFAULT_FILE_UNIT_HANDLER);
		Transformer.transformFile(path, DEFAULT_FILE_UNIT_HANDLER);

		Path nonPath = Paths
				.get(getClass().getClassLoader().getResource("com/flaxel/parser/NoTransformed.txt").toURI());
		Transformer.transformFile(nonPath, DEFAULT_FILE_UNIT_HANDLER);
	}

	@Test
	public void testTransformZipFileNull() {
		File nullFile = null;
		File file = new File(getClass().getClassLoader().getResource("com/flaxel/parser/Transformed.zip").getFile());

		assertThrows(AssertionError.class, () -> Transformer.transformZip(nullFile, Transformer.DEFAULT_FILE_FILTER,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformZip(nullFile, Transformer.DEFAULT_FILE_FILTER,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformZip(nullFile,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformZip(nullFile, Transformer.DEFAULT_FILE_FILTER,
				Transformer.DEFAULT_UNIT_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(AssertionError.class,
				() -> Transformer.transformZip(nullFile, Transformer.DEFAULT_FILE_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformZip(nullFile,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_UNIT_HANDLER));
		assertThrows(AssertionError.class,
				() -> Transformer.transformZip(nullFile, DEFAULT_UNIT_HANDLER, Transformer.DEFAULT_UNIT_FILTER));
		assertThrows(AssertionError.class, () -> Transformer.transformZip(nullFile, DEFAULT_UNIT_HANDLER));

		assertThrows(AssertionError.class, () -> Transformer.transformZip(file, null,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformZip(file, Transformer.DEFAULT_FILE_FILTER, null,
				Transformer.DEFAULT_UNIT_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformZip(file, Transformer.DEFAULT_FILE_FILTER,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, null, DEFAULT_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformZip(file, Transformer.DEFAULT_FILE_FILTER,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER, null));
	}

	@Test
	public void testTransformZipFileNotExist() {
		File file = new File("test_transform.zip");
		assertThrows(NoSuchFileException.class, () -> Transformer.transformZip(file, Transformer.DEFAULT_FILE_FILTER,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(NoSuchFileException.class, () -> Transformer.transformZip(file, Transformer.DEFAULT_FILE_FILTER,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_UNIT_HANDLER));
		assertThrows(NoSuchFileException.class, () -> Transformer.transformZip(file,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(NoSuchFileException.class, () -> Transformer.transformZip(file, Transformer.DEFAULT_FILE_FILTER,
				Transformer.DEFAULT_UNIT_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(NoSuchFileException.class,
				() -> Transformer.transformZip(file, Transformer.DEFAULT_FILE_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(NoSuchFileException.class,
				() -> Transformer.transformZip(file, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_UNIT_HANDLER));
		assertThrows(NoSuchFileException.class,
				() -> Transformer.transformZip(file, DEFAULT_UNIT_HANDLER, Transformer.DEFAULT_UNIT_FILTER));
		assertThrows(NoSuchFileException.class, () -> Transformer.transformZip(file, DEFAULT_UNIT_HANDLER));
	}

	@Test
	public void testTransformZipFileNotFile() {
		File folder = new File("src");
		assertThrows(FileNotFoundException.class,
				() -> Transformer.transformZip(folder, Transformer.DEFAULT_FILE_FILTER,
						Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER,
						DEFAULT_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class, () -> Transformer.transformZip(folder,
				Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class, () -> Transformer.transformZip(folder,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class, () -> Transformer.transformZip(folder,
				Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class,
				() -> Transformer.transformZip(folder, Transformer.DEFAULT_FILE_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class,
				() -> Transformer.transformZip(folder, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class,
				() -> Transformer.transformZip(folder, DEFAULT_UNIT_HANDLER, Transformer.DEFAULT_UNIT_FILTER));
		assertThrows(FileNotFoundException.class, () -> Transformer.transformZip(folder, DEFAULT_UNIT_HANDLER));
	}

	@Test
	public void testTransformZipFile() throws IOException {
		File file = new File(getClass().getClassLoader().getResource("com/flaxel/parser/Transformed.zip").getFile());
		Transformer.transformZip(file, Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_FILE_PROBLEM_HANDLER,
				Transformer.DEFAULT_UNIT_FILTER, DEFAULT_UNIT_HANDLER);
		Transformer.transformZip(file, Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_FILE_PROBLEM_HANDLER,
				DEFAULT_UNIT_HANDLER);
		Transformer.transformZip(file, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER,
				DEFAULT_UNIT_HANDLER);
		Transformer.transformZip(file, Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_UNIT_FILTER,
				DEFAULT_UNIT_HANDLER);
		Transformer.transformZip(file, Transformer.DEFAULT_FILE_FILTER, DEFAULT_UNIT_HANDLER);
		Transformer.transformZip(file, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_UNIT_HANDLER);
		Transformer.transformZip(file, DEFAULT_UNIT_HANDLER, Transformer.DEFAULT_UNIT_FILTER);
		Transformer.transformZip(file, DEFAULT_UNIT_HANDLER);

		Transformer.transformZip(file, DEFAULT_NON_FILE_FILTER, DEFAULT_UNIT_HANDLER);
		Transformer.transformZip(file, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_UNIT_HANDLER);
		Transformer.transformZip(file, DEFAULT_UNIT_HANDLER, DEFAULT_NON_UNIT_FILTER);
	}

	@Test
	public void testTransformZipPathNull() throws URISyntaxException {
		Path nullPath = null;
		Path path = Paths.get(getClass().getClassLoader().getResource("com/flaxel/parser/Transformed.zip").toURI());

		assertThrows(AssertionError.class, () -> Transformer.transformZip(nullPath, Transformer.DEFAULT_FILE_FILTER,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformZip(nullPath, Transformer.DEFAULT_FILE_FILTER,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformZip(nullPath,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformZip(nullPath, Transformer.DEFAULT_FILE_FILTER,
				Transformer.DEFAULT_UNIT_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(AssertionError.class,
				() -> Transformer.transformZip(nullPath, Transformer.DEFAULT_FILE_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformZip(nullPath,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_UNIT_HANDLER));
		assertThrows(AssertionError.class,
				() -> Transformer.transformZip(nullPath, DEFAULT_UNIT_HANDLER, Transformer.DEFAULT_UNIT_FILTER));
		assertThrows(AssertionError.class, () -> Transformer.transformZip(nullPath, DEFAULT_UNIT_HANDLER));

		assertThrows(AssertionError.class, () -> Transformer.transformZip(path, null,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformZip(path, Transformer.DEFAULT_FILE_FILTER, null,
				Transformer.DEFAULT_UNIT_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformZip(path, Transformer.DEFAULT_FILE_FILTER,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, null, DEFAULT_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformZip(path, Transformer.DEFAULT_FILE_FILTER,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER, null));
	}

	@Test
	public void testTransformZipPathNotExist() {
		Path path = new File("test_transform.zip").toPath();
		assertThrows(NoSuchFileException.class, () -> Transformer.transformZip(path, Transformer.DEFAULT_FILE_FILTER,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(NoSuchFileException.class, () -> Transformer.transformZip(path, Transformer.DEFAULT_FILE_FILTER,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_UNIT_HANDLER));
		assertThrows(NoSuchFileException.class, () -> Transformer.transformZip(path,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(NoSuchFileException.class, () -> Transformer.transformZip(path, Transformer.DEFAULT_FILE_FILTER,
				Transformer.DEFAULT_UNIT_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(NoSuchFileException.class,
				() -> Transformer.transformZip(path, Transformer.DEFAULT_FILE_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(NoSuchFileException.class,
				() -> Transformer.transformZip(path, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_UNIT_HANDLER));
		assertThrows(NoSuchFileException.class,
				() -> Transformer.transformZip(path, DEFAULT_UNIT_HANDLER, Transformer.DEFAULT_UNIT_FILTER));
		assertThrows(NoSuchFileException.class, () -> Transformer.transformZip(path, DEFAULT_UNIT_HANDLER));
	}

	@Test
	public void testTransformZipPathNotFile() {
		Path folder = new File("src").toPath();
		assertThrows(FileNotFoundException.class,
				() -> Transformer.transformZip(folder, Transformer.DEFAULT_FILE_FILTER,
						Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER,
						DEFAULT_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class, () -> Transformer.transformZip(folder,
				Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class, () -> Transformer.transformZip(folder,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class, () -> Transformer.transformZip(folder,
				Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class,
				() -> Transformer.transformZip(folder, Transformer.DEFAULT_FILE_FILTER, DEFAULT_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class,
				() -> Transformer.transformZip(folder, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_UNIT_HANDLER));
		assertThrows(FileNotFoundException.class,
				() -> Transformer.transformZip(folder, DEFAULT_UNIT_HANDLER, Transformer.DEFAULT_UNIT_FILTER));
		assertThrows(FileNotFoundException.class, () -> Transformer.transformZip(folder, DEFAULT_UNIT_HANDLER));
	}

	@Test
	public void testTransformZipPath() throws IOException, URISyntaxException {
		Path path = Paths.get(getClass().getClassLoader().getResource("com/flaxel/parser/Transformed.zip").toURI());
		Transformer.transformZip(path, Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_FILE_PROBLEM_HANDLER,
				Transformer.DEFAULT_UNIT_FILTER, DEFAULT_UNIT_HANDLER);
		Transformer.transformZip(path, Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_FILE_PROBLEM_HANDLER,
				DEFAULT_UNIT_HANDLER);
		Transformer.transformZip(path, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER,
				DEFAULT_UNIT_HANDLER);
		Transformer.transformZip(path, Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_UNIT_FILTER,
				DEFAULT_UNIT_HANDLER);
		Transformer.transformZip(path, Transformer.DEFAULT_FILE_FILTER, DEFAULT_UNIT_HANDLER);
		Transformer.transformZip(path, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_UNIT_HANDLER);
		Transformer.transformZip(path, DEFAULT_UNIT_HANDLER, Transformer.DEFAULT_UNIT_FILTER);
		Transformer.transformZip(path, DEFAULT_UNIT_HANDLER);

		Transformer.transformZip(path, DEFAULT_NON_FILE_FILTER, DEFAULT_UNIT_HANDLER);
		Transformer.transformZip(path, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_UNIT_HANDLER);
		Transformer.transformZip(path, DEFAULT_UNIT_HANDLER, DEFAULT_NON_UNIT_FILTER);
	}

	@Test
	public void testTransformFolderFileNull() {
		File nullFolder = null;
		File folder = new File(getClass().getClassLoader().getResource("com/flaxel/parser/transformed").getFile());

		assertThrows(AssertionError.class,
				() -> Transformer.transformFolder(nullFolder, Transformer.DEFAULT_FILE_FILTER,
						Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER,
						DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformFolder(nullFolder,
				Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformFolder(nullFolder,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformFolder(nullFolder,
				Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformFolder(nullFolder,
				Transformer.DEFAULT_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformFolder(nullFolder,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformFolder(nullFolder, DEFAULT_FILE_UNIT_HANDLER,
				Transformer.DEFAULT_UNIT_FILTER));
		assertThrows(AssertionError.class, () -> Transformer.transformFolder(nullFolder, DEFAULT_FILE_UNIT_HANDLER));

		assertThrows(AssertionError.class, () -> Transformer.transformFolder(folder, null,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_FILTER,
				null, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_FILTER,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, null, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_FILTER,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER, null));
	}

	@Test
	public void testTransformFolderFileNotExist() {
		File folder = new File("test_transform");
		assertThrows(IllegalArgumentException.class,
				() -> Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_FILTER,
						Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER,
						DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Transformer.transformFolder(folder,
				Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Transformer.transformFolder(folder,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Transformer.transformFolder(folder,
				Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class,
				() -> Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Transformer.transformFolder(folder,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class,
				() -> Transformer.transformFolder(folder, DEFAULT_FILE_UNIT_HANDLER, Transformer.DEFAULT_UNIT_FILTER));
		assertThrows(IllegalArgumentException.class,
				() -> Transformer.transformFolder(folder, DEFAULT_FILE_UNIT_HANDLER));
	}

	@Test
	public void testTransformFolderFileNotFolder() {
		File folder = new File(getClass().getClassLoader().getResource("com/flaxel/parser/Transformed.zip").getFile());
		assertThrows(IllegalArgumentException.class,
				() -> Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_FILTER,
						Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER,
						DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Transformer.transformFolder(folder,
				Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Transformer.transformFolder(folder,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Transformer.transformFolder(folder,
				Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class,
				() -> Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Transformer.transformFolder(folder,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class,
				() -> Transformer.transformFolder(folder, DEFAULT_FILE_UNIT_HANDLER, Transformer.DEFAULT_UNIT_FILTER));
		assertThrows(IllegalArgumentException.class,
				() -> Transformer.transformFolder(folder, DEFAULT_FILE_UNIT_HANDLER));
	}

	@Test
	public void testTransformFolderFile() throws IOException {
		File folder = new File(getClass().getClassLoader().getResource("com/flaxel/parser/transformed").getFile());
		Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_FILE_PROBLEM_HANDLER,
				Transformer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER);
		Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_FILE_PROBLEM_HANDLER,
				DEFAULT_FILE_UNIT_HANDLER);
		Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER,
				DEFAULT_FILE_UNIT_HANDLER);
		Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_UNIT_FILTER,
				DEFAULT_FILE_UNIT_HANDLER);
		Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER);
		Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER);
		Transformer.transformFolder(folder, DEFAULT_FILE_UNIT_HANDLER, Transformer.DEFAULT_UNIT_FILTER);
		Transformer.transformFolder(folder, DEFAULT_FILE_UNIT_HANDLER);

		Transformer.transformFolder(folder, DEFAULT_NON_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER);
		Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER);
		Transformer.transformFolder(folder, DEFAULT_FILE_UNIT_HANDLER, DEFAULT_NON_UNIT_FILTER);
	}

	@Test
	public void testTransformFolderPathNull() throws URISyntaxException {
		Path nullFolder = null;
		Path folder = Paths.get(getClass().getClassLoader().getResource("com/flaxel/parser/transformed").toURI());

		assertThrows(AssertionError.class, () -> Transformer.transformFolder(nullFolder,
				Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformFolder(nullFolder,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformFolder(nullFolder,
				Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformFolder(nullFolder,
				Transformer.DEFAULT_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformFolder(nullFolder,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformFolder(nullFolder, DEFAULT_FILE_UNIT_HANDLER,
				Transformer.DEFAULT_UNIT_FILTER));
		assertThrows(AssertionError.class, () -> Transformer.transformFolder(nullFolder, DEFAULT_FILE_UNIT_HANDLER));

		assertThrows(AssertionError.class, () -> Transformer.transformFolder(folder, null,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_FILTER,
				null, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_FILTER,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, null, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(AssertionError.class, () -> Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_FILTER,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER, null));
	}

	@Test
	public void testTransformFolderPathNotExist() {
		Path folder = new File("test_transform").toPath();
		assertThrows(IllegalArgumentException.class,
				() -> Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_FILTER,
						Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER,
						DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Transformer.transformFolder(folder,
				Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Transformer.transformFolder(folder,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Transformer.transformFolder(folder,
				Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class,
				() -> Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Transformer.transformFolder(folder,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class,
				() -> Transformer.transformFolder(folder, DEFAULT_FILE_UNIT_HANDLER, Transformer.DEFAULT_UNIT_FILTER));
		assertThrows(IllegalArgumentException.class,
				() -> Transformer.transformFolder(folder, DEFAULT_FILE_UNIT_HANDLER));
	}

	@Test
	public void testTransformFolderPathNotFolder() throws URISyntaxException {
		Path folder = Paths.get(getClass().getClassLoader().getResource("com/flaxel/parser/Transformed.zip").toURI());
		assertThrows(IllegalArgumentException.class,
				() -> Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_FILTER,
						Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER,
						DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Transformer.transformFolder(folder,
				Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Transformer.transformFolder(folder,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Transformer.transformFolder(folder,
				Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class,
				() -> Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class, () -> Transformer.transformFolder(folder,
				Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER));
		assertThrows(IllegalArgumentException.class,
				() -> Transformer.transformFolder(folder, DEFAULT_FILE_UNIT_HANDLER, Transformer.DEFAULT_UNIT_FILTER));
		assertThrows(IllegalArgumentException.class,
				() -> Transformer.transformFolder(folder, DEFAULT_FILE_UNIT_HANDLER));
	}

	@Test
	public void testTransformFolderPath() throws IOException, URISyntaxException {
		Path folder = Paths.get(getClass().getClassLoader().getResource("com/flaxel/parser/transformed").toURI());
		Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_FILE_PROBLEM_HANDLER,
				Transformer.DEFAULT_UNIT_FILTER, DEFAULT_FILE_UNIT_HANDLER);
		Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_FILE_PROBLEM_HANDLER,
				DEFAULT_FILE_UNIT_HANDLER);
		Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, Transformer.DEFAULT_UNIT_FILTER,
				DEFAULT_FILE_UNIT_HANDLER);
		Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_FILTER, Transformer.DEFAULT_UNIT_FILTER,
				DEFAULT_FILE_UNIT_HANDLER);
		Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER);
		Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER);
		Transformer.transformFolder(folder, DEFAULT_FILE_UNIT_HANDLER, Transformer.DEFAULT_UNIT_FILTER);
		Transformer.transformFolder(folder, DEFAULT_FILE_UNIT_HANDLER);

		Transformer.transformFolder(folder, DEFAULT_NON_FILE_FILTER, DEFAULT_FILE_UNIT_HANDLER);
		Transformer.transformFolder(folder, Transformer.DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_FILE_UNIT_HANDLER);
		Transformer.transformFolder(folder, DEFAULT_FILE_UNIT_HANDLER, DEFAULT_NON_UNIT_FILTER);
	}

	@Test
	public void testSetConfigurationNull() {
		assertThrows(AssertionError.class, () -> Transformer.setConfiguration(null));
	}

	@Test
	public void testSetGetConfiguration() {
		ParserConfiguration configuration = new ParserConfiguration().setTabSize(2);
		Transformer.setConfiguration(configuration);

		assertEquals(configuration, Transformer.getConfiguration());
		assertNotEquals(new ParserConfiguration(), Transformer.getConfiguration());
	}
}
