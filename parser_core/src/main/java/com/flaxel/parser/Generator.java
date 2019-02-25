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
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.PrettyPrinter;

import static com.github.javaparser.utils.Utils.assertNotNull;

/**
 * This class is used as a facade to generate java code.
 * 
 * @author flaxel
 * @since 1.0.0
 */
public class Generator {

	/**
	 * printer to create a string from a compilation unit
	 */
	private static Function<CompilationUnit, String> printer = new PrettyPrinter()::print;

	/**
	 * default charset for encoding
	 */
	public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	/**
	 * Set a new printer to create the java code from a {@link CompilationUnit}.
	 * 
	 * @param printer
	 *            new printer
	 * @since 1.0.0
	 */
	public static void setPrinter(Function<CompilationUnit, String> printer) {
		Generator.printer = assertNotNull(printer);
	}

	/**
	 * Get the current printer to create a string from a {@link CompilationUnit}.
	 * 
	 * @return current printer
	 * @since 1.0.0
	 */
	public static Function<CompilationUnit, String> getPrinter() {
		return printer;
	}

	/**
	 * Generate java code.
	 * 
	 * @param supplier
	 *            provider to generate code
	 * @return generated string from the printer
	 * @since 1.0.0
	 */
	public static String generate(Supplier<CompilationUnit> supplier) {
		CompilationUnit unit = assertNotNull(supplier).get();
		return printer.apply(unit);
	}

	/**
	 * Generate java code.
	 * 
	 * @param callable
	 *            provider to generate code
	 * @return generated string from the printer
	 * @throws Exception
	 *             if the callable was not executable
	 * @since 1.0.0
	 */
	public static String generate(Callable<CompilationUnit> callable) throws Exception {
		CompilationUnit unit = assertNotNull(callable).call();
		return printer.apply(unit);
	}

	/**
	 * Generate java code.<br>
	 * Note: Uses UTF-8 encoding.
	 * 
	 * @param supplier
	 *            provider to generate code
	 * @param file
	 *            path to the file where the user has write access
	 * @param options
	 *            valid options specifying how the file is opened, see
	 *            {@link StandardOpenOption}
	 * @return generated string from the printer
	 * @throws IOException
	 *             if an i/o error occurred while the file is written
	 * @since 1.0.0
	 */
	public static String generate(Supplier<CompilationUnit> supplier, File file, OpenOption... options)
			throws IOException {
		return generate(supplier, file, DEFAULT_CHARSET, options);
	}

	/**
	 * Generate java code.<br>
	 * Note: Uses UTF-8 encoding.
	 * 
	 * @param callable
	 *            provider to generate code
	 * @param file
	 *            path to the file where the user has write access
	 * @param options
	 *            valid options specifying how the file is opened, see
	 *            {@link StandardOpenOption}
	 * @return generated string from the printer
	 * @throws Exception
	 *             if an i/o error occurred while the file is written or the
	 *             callback was not executable
	 * @since 1.0.0
	 */
	public static String generate(Callable<CompilationUnit> callable, File file, OpenOption... options)
			throws Exception {
		return generate(callable, file, DEFAULT_CHARSET, options);
	}

	/**
	 * Generate java code.<br>
	 * Note: Uses UTF-8 encoding.
	 * 
	 * @param supplier
	 *            provider to generate code
	 * @param path
	 *            path to the file where the user has write access
	 * @param options
	 *            valid options specifying how the file is opened, see
	 *            {@link StandardOpenOption}
	 * @return generated string from the printer
	 * @throws IOException
	 *             if an i/o error occurred while the file is written
	 * @since 1.0.0
	 */
	public static String generate(Supplier<CompilationUnit> supplier, Path path, OpenOption... options)
			throws IOException {
		return generate(supplier, path, DEFAULT_CHARSET, options);
	}

	/**
	 * Generate java code.<br>
	 * Note: Uses UTF-8 encoding.
	 * 
	 * @param callable
	 *            provider to generate code
	 * @param path
	 *            path to the file where the user has write access
	 * @param options
	 *            valid options specifying how the file is opened, see
	 *            {@link StandardOpenOption}
	 * @return generated string from the printer
	 * @throws Exception
	 *             if an i/o error occurred while the file is written or the
	 *             callback was not executable
	 * @since 1.0.0
	 */
	public static String generate(Callable<CompilationUnit> callable, Path path, OpenOption... options)
			throws Exception {
		return generate(callable, path, DEFAULT_CHARSET, options);
	}

	/**
	 * Generate java code.
	 * 
	 * @param supplier
	 *            provider to generate code
	 * @param file
	 *            path to the file where the user has write access
	 * @param charset
	 *            charset for encoding
	 * @param options
	 *            valid options specifying how the file is opened, see
	 *            {@link StandardOpenOption}
	 * @return generated string from the printer
	 * @throws IOException
	 *             if an i/o error occurred while the file is written
	 * @since 1.0.0
	 */
	public static String generate(Supplier<CompilationUnit> supplier, File file, Charset charset, OpenOption... options)
			throws IOException {
		return generate(supplier, assertNotNull(file).toPath(), charset, options);
	}

	/**
	 * Generate java code.
	 * 
	 * @param callable
	 *            provider to generate code
	 * @param file
	 *            path to the file where the user has write access
	 * @param charset
	 *            charset for encoding
	 * @param options
	 *            valid options specifying how the file is opened, see
	 *            {@link StandardOpenOption}
	 * @return generated string from the printer
	 * @throws Exception
	 *             if an i/o error occurred while the file is written or the
	 *             callback was not executable
	 * @since 1.0.0
	 */
	public static String generate(Callable<CompilationUnit> callable, File file, Charset charset, OpenOption... options)
			throws Exception {
		return generate(callable, assertNotNull(file).toPath(), charset, options);
	}

	/**
	 * Generate java code.
	 * 
	 * @param supplier
	 *            provider to generate code
	 * @param path
	 *            path to the file where the user has write access
	 * @param charset
	 *            charset for encoding
	 * @param options
	 *            valid options specifying how the file is opened, see
	 *            {@link StandardOpenOption}
	 * @return generated string from the printer
	 * @throws IOException
	 *             if an i/o error occurred while the file is written
	 * @since 1.0.0
	 */
	public static String generate(Supplier<CompilationUnit> supplier, Path path, Charset charset, OpenOption... options)
			throws IOException {
		String code = generate(supplier);
		Files.writeString(path, code, charset, options);
		return code;
	}

	/**
	 * Generate java code.
	 * 
	 * @param callable
	 *            provider to generate code
	 * @param path
	 *            path to the file where the user has write access
	 * @param charset
	 *            charset for encoding
	 * @param options
	 *            valid options specifying how the file is opened, see
	 *            {@link StandardOpenOption}
	 * @return generated string from the printer
	 * @throws Exception
	 *             if an i/o error occurred while the file is written or the
	 *             callback was not executable
	 * @since 1.0.0
	 */
	public static String generate(Callable<CompilationUnit> callable, Path path, Charset charset, OpenOption... options)
			throws Exception {
		String code = generate(callable);
		Files.writeString(path, code, charset, options);
		return code;
	}
}
