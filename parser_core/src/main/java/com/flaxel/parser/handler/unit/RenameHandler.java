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
package com.flaxel.parser.handler.unit;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.printer.PrettyPrinter;
import com.github.javaparser.utils.Utils;

import static com.github.javaparser.utils.Utils.assertNonEmpty;
import static com.github.javaparser.utils.Utils.assertNotNull;

/**
 * This class is used to rename a method, variable, class or something else.
 * 
 * @author flaxel
 * @since 1.0.0
 */
public class RenameHandler implements BiConsumer<File, CompilationUnit> {

	/**
	 * regular expression to find the characters which should replace
	 */
	private Pattern pattern;

	/**
	 * function to create a new value from the old value
	 */
	private Function<String, String> replacement;

	/**
	 * output stream to print all verbose information
	 */
	private Optional<OutputStream> outputVerbose;

	/**
	 * output stream to print the result
	 */
	private Optional<OutputStream> outputResult;

	/**
	 * printer to create a string from a node
	 */
	private Function<Node, String> printer;

	/**
	 * true if the program should print verbose information, otherwise false
	 */
	private boolean verbose;

	/**
	 * Initialize the rename handler.
	 * 
	 * @param output
	 *            stream to print the result
	 * @param pattern
	 *            regular expression to find the characters which should replace
	 * @param replacement
	 *            function to create a new value from the old value
	 * @since 1.0.0
	 */
	public RenameHandler(OutputStream output, Pattern pattern, Function<String, String> replacement) {
		this.printer = new PrettyPrinter()::print;
		this.pattern = assertNotNull(pattern);
		this.replacement = assertNotNull(replacement);
		this.outputResult = Optional.ofNullable(output);
		this.outputVerbose = Optional.empty();
	}

	/**
	 * Initialize the rename handler.
	 * 
	 * @param pattern
	 *            regular expression to find the characters which should replace
	 * @param replacement
	 *            function to create a new value from the old value
	 * @since 1.0.0
	 */
	public RenameHandler(Pattern pattern, Function<String, String> replacement) {
		this(null, pattern, replacement);
	}

	/**
	 * Initialize the rename handler.
	 * 
	 * @param output
	 *            stream to print the result
	 * @param regex
	 *            regular expression to find the characters which should replace
	 * @param replacement
	 *            function to create a new value from the old value
	 * @since 1.0.0
	 */
	public RenameHandler(OutputStream output, String regex, Function<String, String> replacement) {
		this(output, Pattern.compile(assertNonEmpty(regex)), replacement);
	}

	/**
	 * Initialize the rename handler.
	 * 
	 * @param regex
	 *            regular expression to find the characters which should replace
	 * @param replacement
	 *            function to create a new value from the old value
	 * @since 1.0.0
	 */
	public RenameHandler(String regex, Function<String, String> replacement) {
		this(null, regex, replacement);
	}

	/**
	 * Set a new printer to create the java code from a {@link Node}.
	 * 
	 * @param printer
	 *            new printer
	 * @return this instance
	 * @since 1.0.0
	 */
	public RenameHandler printer(Function<Node, String> printer) {
		this.printer = assertNotNull(printer);
		return this;
	}

	/**
	 * Set the output stream to print verbose information.
	 * 
	 * @param output
	 *            new output stream
	 * @return this instance
	 * @since 1.0.0
	 */
	public RenameHandler verbose(OutputStream output) {
		this.outputVerbose = Optional.of(assertNotNull(output));
		this.verbose = true;
		return this;
	}

	/**
	 * Set the value whether verbose information should be printed.
	 * 
	 * @param verbose
	 *            new value
	 * @return this instance
	 * @since 1.0.0
	 */
	public RenameHandler verbose(boolean verbose) {
		this.verbose = assertNotNull(verbose);
		return this;
	}

	/**
	 * Write the renamed file to the output stream and write verbose information if
	 * it is necessary.
	 * 
	 * @param source
	 *            source file of the code
	 * @param unit
	 *            entire compilation unit
	 * @since 1.0.0
	 */
	@Override
	public void accept(File source, CompilationUnit unit) {
		assertNotNull(source);
		assertNotNull(unit);

		String content = printer.apply(unit);
		Matcher matcher = pattern.matcher(content);

		try (final OutputStream streamResult = outputResult.isPresent() ? outputResult.get()
				: Files.newOutputStream(source.toPath());
				final OutputStream streamVerbose = outputVerbose.orElse(streamResult)) {

			if (verbose) {
				List<MatchResult> results = matcher.results().collect(Collectors.toList());

				if (!results.isEmpty()) {
					OptionalInt resultMaxStart = results.stream().mapToInt(MatchResult::start).max();
					int maxStart = resultMaxStart.isPresent() ? String.format("%,d", resultMaxStart.getAsInt()).length()
							: 4;

					OptionalInt resultMaxEnd = results.stream().mapToInt(MatchResult::end).max();
					int maxEnd = resultMaxEnd.isPresent() ? String.format("%,d", resultMaxEnd.getAsInt()).length() : 4;

					for (MatchResult result : results) {
						String info = String.format("[%," + maxStart + "d - %," + maxEnd + "d] %s%n", result.start(),
								result.end(), result.group());
						streamVerbose.write(info.getBytes());
					}

					streamVerbose.write(Utils.EOL.getBytes());
				}
			}

			streamResult.write(matcher.replaceAll((result) -> replacement.apply(result.group())).getBytes());
		} catch (IOException e) {
			// do nothing
		}
	}
}
