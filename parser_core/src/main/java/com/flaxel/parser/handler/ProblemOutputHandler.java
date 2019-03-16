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
package com.flaxel.parser.handler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import com.github.javaparser.Problem;

import static com.github.javaparser.utils.Utils.assertNonEmpty;
import static com.github.javaparser.utils.Utils.assertNotNull;

/**
 * This class is used to handle the output of all compilation problems.
 * 
 * @author flaxel
 * @since 1.0.0
 */
public class ProblemOutputHandler implements BiConsumer<File, List<Problem>> {

	/**
	 * any output stream to write data
	 */
	private final OutputStream output;

	/**
	 * character to separate any problems
	 */
	private Optional<String> separator;

	/**
	 * true if the complete cause should be printed, otherwise false
	 */
	private boolean fullStacktrace;

	/**
	 * default separator for the problems
	 */
	public static final String DEFAULT_SEPARTOR = "-";

	/**
	 * Initialize the problem handler.
	 * 
	 * @param output
	 *            stream to write data
	 * @since 1.0.0
	 */
	public ProblemOutputHandler(final OutputStream output) {
		this.output = assertNotNull(output);
		this.separator = Optional.empty();
	}

	/**
	 * Set the value whether the complete cause should be printed.
	 * 
	 * @param fullStacktrace
	 *            new value
	 * @return this instance
	 */
	public ProblemOutputHandler fullStacktrace(boolean fullStacktrace) {
		this.fullStacktrace = assertNotNull(fullStacktrace);
		return this;
	}

	/**
	 * Set the separator for the problems.
	 * 
	 * @param separator
	 *            new value
	 * @return this instance
	 * @since 1.0.0
	 */
	public ProblemOutputHandler separator(String separator) {
		this.separator = Optional.of(assertNonEmpty(separator));
		return this;
	}

	/**
	 * Set the separator for the problems.
	 * 
	 * @param separator
	 *            new value
	 * @return this instance
	 * @since 1.0.0
	 */
	public ProblemOutputHandler separator(char separator) {
		separator(Character.toString(assertNotNull(separator)));
		return this;
	}

	/**
	 * Write the compilation problems from the file on the output stream.
	 * 
	 * @param source
	 *            source file of the code
	 * @param problems
	 *            all compilation problems
	 * @since 1.0.0
	 */
	@Override
	public void accept(File source, List<Problem> problems) {
		assertNotNull(source);
		assertNotNull(problems);

		StringBuilder builder = new StringBuilder(String.format("problems in the file: %s%n%n", source.getPath()));

		for (Problem problem : problems) {
			String message = createMessage(problem);
			builder.append(message);
		}

		String info = builder.toString();

		try (output) {
			output.write(info.getBytes());
		} catch (IOException e) {
			// do nothing
		}
	}

	/**
	 * Create a message for a problem.
	 * 
	 * @param problem
	 *            any problem
	 * @return message
	 * @since 1.0.0
	 */
	private String createMessage(Problem problem) {
		if (fullStacktrace)
			return String.format("%s%n%n%s%n%n", problem.toString(), separator.orElse(DEFAULT_SEPARTOR).repeat(20));

		return String.format("%s%n", problem.getVerboseMessage());
	}
}
