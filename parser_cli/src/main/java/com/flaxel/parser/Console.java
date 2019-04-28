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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.MaskingCallback;
import org.jline.reader.ParsedLine;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import static com.github.javaparser.utils.Utils.assertNonEmpty;
import static com.github.javaparser.utils.Utils.assertNotNull;

import picocli.CommandLine;
import picocli.shell.jline3.PicocliJLineCompleter;

/**
 * This class is used to initialize and handle all actions with the console.
 * 
 * @author flaxel
 * @since 1.0.0
 */
public class Console {

	/**
	 * reader to get the input from the user
	 */
	private static LineReader reader;

	/**
	 * printer to write to the console
	 */
	private static PrintWriter writer;

	/**
	 * stream to write to the console
	 */
	private static OutputStream output;

	/**
	 * Initialize the console.
	 * 
	 * @param name
	 *            name of the application
	 * @param rootCommand
	 *            basic command
	 * @throws IOException
	 *             terminal cannot be created
	 * @since 1.0.0
	 */
	public static void init(String name, Runnable rootCommand) throws IOException {
		assertNonEmpty(name);
		assertNotNull(rootCommand);

		CommandLine cmd = new CommandLine(rootCommand);
		Terminal terminal = TerminalBuilder.builder().name(name).build();

		reader = LineReaderBuilder.builder()
				.appName(name)
				.terminal(terminal)
				.completer(new PicocliJLineCompleter(cmd.getCommandSpec()))
				.parser(new DefaultParser())
				.build();

		writer = reader.getTerminal().writer();
		output = reader.getTerminal().output();
	}

	/**
	 * Read the input from the user.
	 * 
	 * @param prompt
	 *            extra string that is printed before an command
	 * @return all arguments
	 * @throws UserInterruptException
	 *             if the user types Ctrl-C
	 * @throws EndOfFileException
	 *             if the user types Ctrl-D
	 * @since 1.0.0
	 */
	public static String[] read(String prompt) throws UserInterruptException, EndOfFileException {
		String line = reader.readLine(assertNotNull(prompt), null, (MaskingCallback) null, null);
		ParsedLine pl = reader.getParser().parse(line, 0);
		String[] arguments = pl.words().toArray(String[]::new);

		return arguments;
	}

	/**
	 * Print a string with optional arguments to the console.
	 * 
	 * @param format
	 *            format string
	 * @param args
	 *            arguments referenced to the format string
	 * @since 1.0.0
	 */
	public static void print(String format, Object... args) {
		writer.printf(assertNonEmpty(format), assertNotNull(args));
	}

	/**
	 * Print a string with optional arguments to the console and at the end make a
	 * line break.
	 * 
	 * @param format
	 *            format string
	 * @param args
	 *            arguments referenced to the format string
	 * @since 1.0.0
	 */
	public static void println(String format, Object... args) {
		writer.printf(assertNonEmpty(format).concat("\n"), assertNotNull(args));
	}

	/**
	 * Clear the content of the console.
	 * 
	 * @since 1.0.0
	 */
	public static void clear() {
		((LineReaderImpl) reader).clearScreen();
	}

	/**
	 * Get the print writer from the console.
	 * 
	 * @return print writer
	 * @since 1.0.0
	 */
	public static PrintWriter getWriter() {
		return writer;
	}

	/**
	 * Get the output stream from the console.
	 * 
	 * @return output stream
	 * @since 1.0.0
	 */
	public static OutputStream getOutput() {
		return output;
	}
}
