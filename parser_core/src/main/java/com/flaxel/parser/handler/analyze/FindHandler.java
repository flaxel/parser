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
package com.flaxel.parser.handler.analyze;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
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
 * This class is used to find an element in the source code.
 * 
 * @author flaxel
 * @since 1.0.0
 */
public class FindHandler implements BiConsumer<File, CompilationUnit> {

	/**
	 * output stream to print all information
	 */
	private final OutputStream output;

	/**
	 * regular expression to find the characters which should replace
	 */
	private Pattern pattern;

	/**
	 * printer to create a string from a node
	 */
	private Function<Node, String> printer;

	/**
	 * Initialize the find handler.
	 * 
	 * @param output
	 *            output stream to print all information
	 * @param pattern
	 *            regular expression to find the characters
	 * @since 1.0.0
	 */
	public FindHandler(OutputStream output, Pattern pattern) {
		this.printer = new PrettyPrinter()::print;
		this.output = assertNotNull(output);
		this.pattern = assertNotNull(pattern);
	}

	/**
	 * Initialize the find handler.
	 * 
	 * @param output
	 *            output stream to print all information
	 * @param regex
	 *            regular expression to find the characters
	 * @since 1.0.0
	 */
	public FindHandler(OutputStream output, String regex) {
		this(output, Pattern.compile(assertNonEmpty(regex)));
	}

	/**
	 * Set a new printer to create the java code from a {@link Node}.
	 * 
	 * @param printer
	 *            new printer
	 * @return this instance
	 * @since 1.0.0
	 */
	public FindHandler printer(Function<Node, String> printer) {
		this.printer = assertNotNull(printer);
		return this;
	}

	/**
	 * Write information where you can find the source code snippet.
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

		try (output) {
			List<MatchResult> results = matcher.results().collect(Collectors.toList());

			if (!results.isEmpty()) {
				output.write(String.format("%s%n", source.getCanonicalPath()).getBytes());

				OptionalInt resultMaxStart = results.stream().mapToInt(MatchResult::start).max();
				int maxStart = resultMaxStart.isPresent() ? String.format("%,d", resultMaxStart.getAsInt()).length()
						: 4;

				OptionalInt resultMaxEnd = results.stream().mapToInt(MatchResult::end).max();
				int maxEnd = resultMaxEnd.isPresent() ? String.format("%,d", resultMaxEnd.getAsInt()).length() : 4;

				for (MatchResult result : results) {
					String info = String.format("[%," + maxStart + "d - %," + maxEnd + "d] %s%n", result.start(),
							result.end(), result.group());
					output.write(info.getBytes());
				}

				output.write(Utils.EOL.getBytes());
			}
		} catch (Exception e) {
			// do nothing
		}
	}
}
