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

import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;

import com.flaxel.parser.command.ParserCommand;

import picocli.CommandLine;

/**
 * This class is used to create and start the terminal.
 * 
 * @author flaxel
 * @since 1.0.0
 */
public class Main {

	/**
	 * name of the application
	 */
	public static final String NAME = "parser";

	/**
	 * extra string that is printed before an command
	 */
	public static final String PROMPT = "> ";

	/**
	 * create and start the terminal
	 * 
	 * @param args
	 *            all arguments
	 * @throws IOException
	 *             terminal cannot be created
	 * @since 1.0.0
	 */
	public static void main(String[] args) throws IOException {
		ParserCommand command = new ParserCommand();
		Console.init(NAME, command);

		while (true) {
			try {
				String[] arguments = Console.read(PROMPT);
				CommandLine.run(command, arguments);
			} catch (UserInterruptException | EndOfFileException e) {
				return;
			}
		}
	}
}
