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
package com.flaxel.parser.command.transform;

import java.io.File;
import java.io.IOException;

import com.flaxel.parser.Analyzer;
import com.flaxel.parser.Console;
import com.flaxel.parser.handler.problem.OutputHandler;
import com.flaxel.parser.handler.transform.RenameHandler;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * This class represents the subcommand 'rename' to rename a part of the source
 * code.
 * 
 * @author flaxel
 * @since 1.0.0
 */
@Command(name = "remove", description = "remove source code", sortOptions = true)
public class RemoveCommand implements Runnable {

	/**
	 * true if the help for the user should be printed, otherwise false
	 */
	@Option(names = { "--help", "-h" }, usageHelp = true, description = "display this help message")
	private boolean usageHelpRequested;

	/**
	 * file of the source code
	 */
	@Parameters(index = "0", description = "source code")
	private File file;

	/**
	 * pattern to find content that should be replaced
	 */
	@Parameters(index = "1", description = "regex to find content")
	private String regex;

	/**
	 * Execute the command.
	 * 
	 * @since 1.0.0
	 */
	@Override
	public void run() {
		if (usageHelpRequested) {
			new CommandLine(this).usage(Console.getWriter());
		}

		RenameHandler handler = new RenameHandler(regex, (oldValue) -> "").verbose(Console.getOutput());
		OutputHandler problemHandler = new OutputHandler(Console.getOutput());

		try {
			if (file.isDirectory()) {
				Analyzer.analyzeFolder(file, problemHandler, handler);
			} else if (file.getName().endsWith(".zip")) {
				Analyzer.analyzeZip(file, problemHandler, handler);
			} else {
				Analyzer.analyzeFile(file, problemHandler, handler);
			}
		} catch (IOException e) {
			Console.print("not possible to edit class");
		}
	}
}
