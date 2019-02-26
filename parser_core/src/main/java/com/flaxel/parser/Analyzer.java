/**		
 *		Copyright [2019] [flaxel]
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *		 
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package com.flaxel.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;
import com.github.javaparser.utils.SourceRoot.Callback.Result;
import com.github.javaparser.utils.SourceZip;

import static com.github.javaparser.utils.Utils.assertNotNull;

/**
 * This class is used as a facade to analyze java code.
 * 
 * @author flaxel
 * @since 1.0.0
 */
public class Analyzer {

	/**
	 * default configuration for the parser
	 */
	private static ParserConfiguration configuration = new ParserConfiguration();

	/**
	 * default charset for encoding
	 */
	public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	/**
	 * default file filter
	 */
	public static final Predicate<File> DEFAULT_FILE_FILTER = (file) -> true;

	/**
	 * default unit filter
	 */
	public static final Predicate<CompilationUnit> DEFAULT_UNIT_FILTER = (unit) -> true;

	/**
	 * default problem handler
	 */
	public static final Consumer<List<Problem>> DEFAULT_PROBLEM_HANDLER = (problems) -> {};

	/**
	 * default problem handler with file
	 */
	public static final BiConsumer<File, List<Problem>> DEFAULT_FILE_PROBLEM_HANDLER = (file, problems) -> {};

	/**
	 * Set a new configuration for the parser.
	 * 
	 * @param configuration
	 *            new configuration
	 * @since 1.0.0
	 */
	public static void setConfiguration(ParserConfiguration configuration) {
		Analyzer.configuration = assertNotNull(configuration);
	}

	/**
	 * Get the current configuration for the parser.
	 * 
	 * @return current parser configuration
	 * @since 1.0.0
	 */
	public static ParserConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * Analyze java code from a file.
	 * 
	 * @param file
	 *            path to the file where the user has read access
	 * @param charset
	 *            charset for encoding
	 * @param problemHandler
	 *            handler for parsing problems
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws FileNotFoundException
	 *             if the file cannot be opened to read it
	 * @since 1.0.0
	 */
	public static void analyzeFile(File file, Charset charset, BiConsumer<File, List<Problem>> problemHandler,
			BiConsumer<File, CompilationUnit> unitHandler) throws FileNotFoundException {
		assertNotNull(file);
		try {
			configuration.setSymbolResolver(Parser.createTypeSolver(file));
		} catch (IOException e) {
			// ignore: never happens
		}
		JavaParser parser = new JavaParser(configuration);
		analyze(file, parser.parse(file, charset), DEFAULT_FILE_FILTER, DEFAULT_UNIT_FILTER, problemHandler,
				unitHandler);
	}

	/**
	 * Analyze java code from a file.
	 * 
	 * @param file
	 *            path to the file where the user has read access
	 * @param charset
	 *            charset for encoding
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws FileNotFoundException
	 *             if the file cannot be opened to read it
	 * @since 1.0.0
	 */
	public static void analyzeFile(File file, Charset charset, BiConsumer<File, CompilationUnit> unitHandler)
			throws FileNotFoundException {
		analyzeFile(file, charset, Analyzer.DEFAULT_FILE_PROBLEM_HANDLER, unitHandler);
	}

	/**
	 * Analyze java code from a file.<br>
	 * Note: Uses UTF-8 encoding.
	 * 
	 * @param file
	 *            path to the file where the user has read access
	 * @param problemHandler
	 *            handler for parsing problems
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws FileNotFoundException
	 *             if the file cannot be opened to read it
	 * @since 1.0.0
	 */
	public static void analyzeFile(File file, BiConsumer<File, List<Problem>> problemHandler,
			BiConsumer<File, CompilationUnit> unitHandler) throws FileNotFoundException {
		analyzeFile(file, DEFAULT_CHARSET, problemHandler, unitHandler);
	}

	/**
	 * Analyze java code from a file.<br>
	 * Note: Uses UTF-8 encoding.
	 * 
	 * @param file
	 *            path to the file where the user has read access
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws FileNotFoundException
	 *             if the file cannot be opened to read it
	 * @since 1.0.0
	 */
	public static void analyzeFile(File file, BiConsumer<File, CompilationUnit> unitHandler)
			throws FileNotFoundException {
		analyzeFile(file, DEFAULT_CHARSET, DEFAULT_FILE_PROBLEM_HANDLER, unitHandler);
	}

	/**
	 * Analyze java code from a file.
	 * 
	 * @param path
	 *            path to the file where the user has read access
	 * @param charset
	 *            charset for encoding
	 * @param problemHandler
	 *            handler for parsing problems
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws FileNotFoundException
	 *             if the file cannot be opened to read it
	 * @since 1.0.0
	 */
	public static void analyzeFile(Path path, Charset charset, BiConsumer<File, List<Problem>> problemHandler,
			BiConsumer<File, CompilationUnit> unitHandler) throws FileNotFoundException {
		analyzeFile(assertNotNull(path).toFile(), charset, problemHandler, unitHandler);
	}

	/**
	 * Analyze java code from a file.
	 * 
	 * @param path
	 *            path to the file where the user has read access
	 * @param charset
	 *            charset for encoding
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws FileNotFoundException
	 *             if the file cannot be opened to read it
	 * @since 1.0.0
	 */
	public static void analyzeFile(Path path, Charset charset, BiConsumer<File, CompilationUnit> unitHandler)
			throws FileNotFoundException {
		analyzeFile(path, charset, DEFAULT_FILE_PROBLEM_HANDLER, unitHandler);
	}

	/**
	 * Analyze java code from a file.<br>
	 * Note: Uses UTF-8 encoding.
	 * 
	 * @param path
	 *            path to the file where the user has read access
	 * @param problemHandler
	 *            handler for parsing problems
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws FileNotFoundException
	 *             if the file cannot be opened to read it
	 * @since 1.0.0
	 */
	public static void analyzeFile(Path path, BiConsumer<File, List<Problem>> problemHandler,
			BiConsumer<File, CompilationUnit> unitHandler) throws FileNotFoundException {
		analyzeFile(path, DEFAULT_CHARSET, problemHandler, unitHandler);
	}

	/**
	 * Analyze java code from a file.<br>
	 * Note: Uses UTF-8 encoding.
	 * 
	 * @param path
	 *            path to the file where the user has read access
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws FileNotFoundException
	 *             if the file cannot be opened to read it
	 * @since 1.0.0
	 */
	public static void analyzeFile(Path path, BiConsumer<File, CompilationUnit> unitHandler)
			throws FileNotFoundException {
		analyzeFile(path, DEFAULT_CHARSET, DEFAULT_FILE_PROBLEM_HANDLER, unitHandler);
	}

	/**
	 * Analyze java code from all files of a folder.
	 * 
	 * @param folder
	 *            path to the folder
	 * @param fileFilter
	 *            filter for a file
	 * @param problemHandler
	 *            handler for parsing problems
	 * @param unitFilter
	 *            filter for compilation unit
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeFolder(Path folder, Predicate<File> fileFilter,
			BiConsumer<File, List<Problem>> problemHandler, Predicate<CompilationUnit> unitFilter,
			BiConsumer<File, CompilationUnit> unitHandler) throws IOException {
		assertNotNull(folder);
		configuration.setSymbolResolver(Parser.createTypeSolver(folder));
		SourceRoot root = new SourceRoot(folder, configuration);
		root.parse("", (localPath, absolutePath, result) -> {
			analyze(absolutePath.toFile(), result, fileFilter, unitFilter, problemHandler, unitHandler);
			return Result.DONT_SAVE;
		});
	}

	/**
	 * Analyze java code from all files of a folder.
	 * 
	 * @param folder
	 *            path to the folder
	 * @param fileFilter
	 *            filter for a file
	 * @param problemHandler
	 *            handler for parsing problems
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeFolder(Path folder, Predicate<File> fileFilter,
			BiConsumer<File, List<Problem>> problemHandler, BiConsumer<File, CompilationUnit> unitHandler)
			throws IOException {
		analyzeFolder(folder, fileFilter, problemHandler, DEFAULT_UNIT_FILTER, unitHandler);
	}

	/**
	 * Analyze java code from all files of a folder.
	 * 
	 * @param folder
	 *            path to the folder
	 * @param problemHandler
	 *            handler for parsing problems
	 * @param unitFilter
	 *            filter for compilation unit
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeFolder(Path folder, BiConsumer<File, List<Problem>> problemHandler,
			Predicate<CompilationUnit> unitFilter, BiConsumer<File, CompilationUnit> unitHandler) throws IOException {
		analyzeFolder(folder, DEFAULT_FILE_FILTER, problemHandler, unitFilter, unitHandler);
	}

	/**
	 * Analyze java code from all files of a folder.
	 * 
	 * @param folder
	 *            path to the folder
	 * @param fileFilter
	 *            filter for a file
	 * @param unitFilter
	 *            filter for compilation unit
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeFolder(Path folder, Predicate<File> fileFilter, Predicate<CompilationUnit> unitFilter,
			BiConsumer<File, CompilationUnit> unitHandler) throws IOException {
		analyzeFolder(folder, fileFilter, DEFAULT_FILE_PROBLEM_HANDLER, unitFilter, unitHandler);
	}

	/**
	 * Analyze java code from all files of a folder.
	 * 
	 * @param folder
	 *            path to the folder
	 * @param fileFilter
	 *            filter for a file
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeFolder(Path folder, Predicate<File> fileFilter,
			BiConsumer<File, CompilationUnit> unitHandler) throws IOException {
		analyzeFolder(folder, fileFilter, DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_UNIT_FILTER, unitHandler);
	}

	/**
	 * Analyze java code from all files of a folder.
	 * 
	 * @param folder
	 *            path to the folder
	 * @param problemHandler
	 *            handler for parsing problems
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeFolder(Path folder, BiConsumer<File, List<Problem>> problemHandler,
			BiConsumer<File, CompilationUnit> unitHandler) throws IOException {
		analyzeFolder(folder, DEFAULT_FILE_FILTER, problemHandler, DEFAULT_UNIT_FILTER, unitHandler);
	}

	/**
	 * Analyze java code from all files of a folder.
	 * 
	 * @param folder
	 *            path to the folder
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @param unitFilter
	 *            filter for compilation unit
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeFolder(Path folder, BiConsumer<File, CompilationUnit> unitHandler,
			Predicate<CompilationUnit> unitFilter) throws IOException {
		analyzeFolder(folder, DEFAULT_FILE_FILTER, DEFAULT_FILE_PROBLEM_HANDLER, unitFilter, unitHandler);
	}

	/**
	 * Analyze java code from all files of a folder.
	 * 
	 * @param folder
	 *            path to the folder
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeFolder(Path folder, BiConsumer<File, CompilationUnit> unitHandler) throws IOException {
		analyzeFolder(folder, DEFAULT_FILE_FILTER, DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_UNIT_FILTER, unitHandler);
	}

	/**
	 * Analyze java code from all files of a folder.
	 * 
	 * @param folder
	 *            path to the folder
	 * @param fileFilter
	 *            filter for a file
	 * @param problemHandler
	 *            handler for parsing problems
	 * @param unitFilter
	 *            filter for compilation unit
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeFolder(File folder, Predicate<File> fileFilter,
			BiConsumer<File, List<Problem>> problemHandler, Predicate<CompilationUnit> unitFilter,
			BiConsumer<File, CompilationUnit> unitHandler) throws IOException {
		analyzeFolder(assertNotNull(folder).toPath(), fileFilter, problemHandler, unitFilter, unitHandler);
	}

	/**
	 * Analyze java code from all files of a folder.
	 * 
	 * @param folder
	 *            path to the folder
	 * @param fileFilter
	 *            filter for a file
	 * @param problemHandler
	 *            handler for parsing problems
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeFolder(File folder, Predicate<File> fileFilter,
			BiConsumer<File, List<Problem>> problemHandler, BiConsumer<File, CompilationUnit> unitHandler)
			throws IOException {
		analyzeFolder(folder, fileFilter, problemHandler, DEFAULT_UNIT_FILTER, unitHandler);
	}

	/**
	 * Analyze java code from all files of a folder.
	 * 
	 * @param folder
	 *            path to the folder
	 * @param problemHandler
	 *            handler for parsing problems
	 * @param unitFilter
	 *            filter for compilation unit
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeFolder(File folder, BiConsumer<File, List<Problem>> problemHandler,
			Predicate<CompilationUnit> unitFilter, BiConsumer<File, CompilationUnit> unitHandler) throws IOException {
		analyzeFolder(folder, DEFAULT_FILE_FILTER, problemHandler, unitFilter, unitHandler);
	}

	/**
	 * Analyze java code from all files of a folder.
	 * 
	 * @param folder
	 *            path to the folder
	 * @param fileFilter
	 *            filter for a file
	 * @param unitFilter
	 *            filter for compilation unit
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeFolder(File folder, Predicate<File> fileFilter, Predicate<CompilationUnit> unitFilter,
			BiConsumer<File, CompilationUnit> unitHandler) throws IOException {
		analyzeFolder(folder, fileFilter, DEFAULT_FILE_PROBLEM_HANDLER, unitFilter, unitHandler);
	}

	/**
	 * Analyze java code from all files of a folder.
	 * 
	 * @param folder
	 *            path to the folder
	 * @param fileFilter
	 *            filter for a file
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeFolder(File folder, Predicate<File> fileFilter,
			BiConsumer<File, CompilationUnit> unitHandler) throws IOException {
		analyzeFolder(folder, fileFilter, DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_UNIT_FILTER, unitHandler);
	}

	/**
	 * Analyze java code from all files of a folder.
	 * 
	 * @param folder
	 *            path to the folder
	 * @param problemHandler
	 *            handler for parsing problems
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeFolder(File folder, BiConsumer<File, List<Problem>> problemHandler,
			BiConsumer<File, CompilationUnit> unitHandler) throws IOException {
		analyzeFolder(folder, DEFAULT_FILE_FILTER, problemHandler, DEFAULT_UNIT_FILTER, unitHandler);
	}

	/**
	 * Analyze java code from all files of a folder.
	 * 
	 * @param folder
	 *            path to the folder
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @param unitFilter
	 *            filter for compilation unit
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeFolder(File folder, BiConsumer<File, CompilationUnit> unitHandler,
			Predicate<CompilationUnit> unitFilter) throws IOException {
		analyzeFolder(folder, DEFAULT_FILE_FILTER, DEFAULT_FILE_PROBLEM_HANDLER, unitFilter, unitHandler);
	}

	/**
	 * Analyze java code from all files of a folder.
	 * 
	 * @param folder
	 *            path to the folder
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeFolder(File folder, BiConsumer<File, CompilationUnit> unitHandler) throws IOException {
		analyzeFolder(folder, DEFAULT_FILE_FILTER, DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_UNIT_FILTER, unitHandler);
	}

	/**
	 * Analyze java code from all files in a zip file.
	 * 
	 * @param zipPath
	 *            path to the file
	 * @param fileFilter
	 *            filter for a file
	 * @param problemHandler
	 *            handler for parsing problems
	 * @param unitFilter
	 *            filter for compilation unit
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeZip(Path zipPath, Predicate<File> fileFilter,
			BiConsumer<File, List<Problem>> problemHandler, Predicate<CompilationUnit> unitFilter,
			BiConsumer<File, CompilationUnit> unitHandler) throws IOException {
		assertNotNull(zipPath);
		configuration.setSymbolResolver(Parser.createTypeSolver(zipPath));
		SourceZip archive = new SourceZip(zipPath, configuration);
		archive.parse((relativeZipEntryPath, result) -> {
			analyze(relativeZipEntryPath.toFile(), result, fileFilter, unitFilter, problemHandler, unitHandler);
		});
	}

	/**
	 * Analyze java code from all files in a zip file.
	 * 
	 * @param zipPath
	 *            path to the file
	 * @param fileFilter
	 *            filter for a file
	 * @param problemHandler
	 *            handler for parsing problems
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeZip(Path zipPath, Predicate<File> fileFilter,
			BiConsumer<File, List<Problem>> problemHandler, BiConsumer<File, CompilationUnit> unitHandler)
			throws IOException {
		analyzeZip(zipPath, fileFilter, problemHandler, DEFAULT_UNIT_FILTER, unitHandler);
	}

	/**
	 * Analyze java code from all files in a zip file.
	 * 
	 * @param zipPath
	 *            path to the file
	 * @param problemHandler
	 *            handler for parsing problems
	 * @param unitFilter
	 *            filter for compilation unit
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeZip(Path zipPath, BiConsumer<File, List<Problem>> problemHandler,
			Predicate<CompilationUnit> unitFilter, BiConsumer<File, CompilationUnit> unitHandler) throws IOException {
		analyzeZip(zipPath, DEFAULT_FILE_FILTER, problemHandler, unitFilter, unitHandler);
	}

	/**
	 * Analyze java code from all files in a zip file.
	 * 
	 * @param zipPath
	 *            path to the file
	 * @param fileFilter
	 *            filter for a file
	 * @param unitFilter
	 *            filter for compilation unit
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeZip(Path zipPath, Predicate<File> fileFilter, Predicate<CompilationUnit> unitFilter,
			BiConsumer<File, CompilationUnit> unitHandler) throws IOException {
		analyzeZip(zipPath, fileFilter, DEFAULT_FILE_PROBLEM_HANDLER, unitFilter, unitHandler);
	}

	/**
	 * Analyze java code from all files in a zip file.
	 * 
	 * @param zipPath
	 *            path to the file
	 * @param fileFilter
	 *            filter for a file
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeZip(Path zipPath, Predicate<File> fileFilter,
			BiConsumer<File, CompilationUnit> unitHandler) throws IOException {
		analyzeZip(zipPath, fileFilter, DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_UNIT_FILTER, unitHandler);
	}

	/**
	 * Analyze java code from all files in a zip file
	 * 
	 * @param zipPath
	 *            path to the file
	 * @param problemHandler
	 *            handler for parsing problems
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeZip(Path zipPath, BiConsumer<File, List<Problem>> problemHandler,
			BiConsumer<File, CompilationUnit> unitHandler) throws IOException {
		analyzeZip(zipPath, DEFAULT_FILE_FILTER, problemHandler, DEFAULT_UNIT_FILTER, unitHandler);
	}

	/**
	 * Analyze java code from all files in a zip file.
	 * 
	 * @param zipPath
	 *            path to the file
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @param unitFilter
	 *            filter for compilation unit
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeZip(Path zipPath, BiConsumer<File, CompilationUnit> unitHandler,
			Predicate<CompilationUnit> unitFilter) throws IOException {
		analyzeZip(zipPath, DEFAULT_FILE_FILTER, DEFAULT_FILE_PROBLEM_HANDLER, unitFilter, unitHandler);
	}

	/**
	 * Analyze java code from all files in a zip file.
	 * 
	 * @param zipPath
	 *            path to the file
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeZip(Path zipPath, BiConsumer<File, CompilationUnit> unitHandler) throws IOException {
		analyzeZip(zipPath, DEFAULT_FILE_FILTER, DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_UNIT_FILTER, unitHandler);
	}

	/**
	 * Analyze java code from all files in a zip file.
	 * 
	 * @param zipFile
	 *            path to the file
	 * @param fileFilter
	 *            filter for a file
	 * @param problemHandler
	 *            handler for parsing problems
	 * @param unitFilter
	 *            filter for compilation unit
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeZip(File zipFile, Predicate<File> fileFilter,
			BiConsumer<File, List<Problem>> problemHandler, Predicate<CompilationUnit> unitFilter,
			BiConsumer<File, CompilationUnit> unitHandler) throws IOException {
		analyzeZip(assertNotNull(zipFile).toPath(), fileFilter, problemHandler, unitFilter, unitHandler);
	}

	/**
	 * Analyze java code from all files in a zip file.
	 * 
	 * @param zipFile
	 *            path to the file
	 * @param fileFilter
	 *            filter for a file
	 * @param problemHandler
	 *            handler for parsing problems
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeZip(File zipFile, Predicate<File> fileFilter,
			BiConsumer<File, List<Problem>> problemHandler, BiConsumer<File, CompilationUnit> unitHandler)
			throws IOException {
		analyzeZip(zipFile, fileFilter, problemHandler, DEFAULT_UNIT_FILTER, unitHandler);
	}

	/**
	 * Analyze java code from all files in a zip file.
	 * 
	 * @param zipFile
	 *            path to the file
	 * @param problemHandler
	 *            handler for parsing problems
	 * @param unitFilter
	 *            filter for compilation unit
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeZip(File zipFile, BiConsumer<File, List<Problem>> problemHandler,
			Predicate<CompilationUnit> unitFilter, BiConsumer<File, CompilationUnit> unitHandler) throws IOException {
		analyzeZip(zipFile, DEFAULT_FILE_FILTER, problemHandler, unitFilter, unitHandler);
	}

	/**
	 * Analyze java code from all files in a zip file.
	 * 
	 * @param zipFile
	 *            path to the file
	 * @param fileFilter
	 *            filter for a file
	 * @param unitFilter
	 *            filter for compilation unit
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeZip(File zipFile, Predicate<File> fileFilter, Predicate<CompilationUnit> unitFilter,
			BiConsumer<File, CompilationUnit> unitHandler) throws IOException {
		analyzeZip(zipFile, fileFilter, DEFAULT_FILE_PROBLEM_HANDLER, unitFilter, unitHandler);
	}

	/**
	 * Analyze java code from all files in a zip file.
	 * 
	 * @param zipFile
	 *            path to the file
	 * @param fileFilter
	 *            filter for a file
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeZip(File zipFile, Predicate<File> fileFilter,
			BiConsumer<File, CompilationUnit> unitHandler) throws IOException {
		analyzeZip(zipFile, fileFilter, DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_UNIT_FILTER, unitHandler);
	}

	/**
	 * Analyze java code from all files in a zip file.
	 * 
	 * @param zipFile
	 *            path to the file
	 * @param problemHandler
	 *            handler for parsing problems
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeZip(File zipFile, BiConsumer<File, List<Problem>> problemHandler,
			BiConsumer<File, CompilationUnit> unitHandler) throws IOException {
		analyzeZip(zipFile, DEFAULT_FILE_FILTER, problemHandler, DEFAULT_UNIT_FILTER, unitHandler);
	}

	/**
	 * Analyze java code from all files in a zip file.
	 * 
	 * @param zipFile
	 *            path to the file
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @param unitFilter
	 *            filter for compilation unit
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeZip(File zipFile, BiConsumer<File, CompilationUnit> unitHandler,
			Predicate<CompilationUnit> unitFilter) throws IOException {
		analyzeZip(zipFile, DEFAULT_FILE_FILTER, DEFAULT_FILE_PROBLEM_HANDLER, unitFilter, unitHandler);
	}

	/**
	 * Analyze java code from all files in a zip file.
	 * 
	 * @param zipFile
	 *            path to the file
	 * @param unitHandler
	 *            handler for compilation unit and resolving
	 * @throws IOException
	 *             if an i/o error occurred while a file is parsed
	 * @since 1.0.0
	 */
	public static void analyzeZip(File zipFile, BiConsumer<File, CompilationUnit> unitHandler) throws IOException {
		analyzeZip(zipFile, DEFAULT_FILE_FILTER, DEFAULT_FILE_PROBLEM_HANDLER, DEFAULT_UNIT_FILTER, unitHandler);
	}

	/**
	 * Analyze java code from a file.
	 * 
	 * @param file
	 *            path to the file
	 * @param parseResult
	 *            result of the parsing
	 * @param fileFilter
	 *            filter for a file
	 * @param unitFilter
	 *            filter for compilation unit
	 * @param problemHandler
	 *            handler for parsing problems
	 * @param unitHandler
	 *            handler for compilation unit
	 * @since 1.0.0
	 */
	private static void analyze(File file, ParseResult<CompilationUnit> parseResult, Predicate<File> fileFilter,
			Predicate<CompilationUnit> unitFilter, BiConsumer<File, List<Problem>> problemHandler,
			BiConsumer<File, CompilationUnit> unitHandler) {
		assertNotNull(file);
		assertNotNull(parseResult);
		assertNotNull(fileFilter);
		assertNotNull(unitFilter);
		assertNotNull(problemHandler);
		assertNotNull(unitHandler);

		if (!fileFilter.test(file))
			return;

		if (parseResult.isSuccessful()) {
			Optional<CompilationUnit> result = parseResult.getResult();

			if (result.isEmpty())
				return;

			CompilationUnit unit = result.get();

			if (unitFilter.test(unit)) {
				unitHandler.accept(file, unit);
			}
		} else {
			problemHandler.accept(file, parseResult.getProblems());
		}
	}
}
