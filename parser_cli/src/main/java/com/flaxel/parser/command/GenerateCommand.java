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
package com.flaxel.parser.command;

import java.io.File;
import java.net.MalformedURLException;

import com.flaxel.parser.Console;
import com.flaxel.parser.Parser;
import com.flaxel.parser.command.generate.PojoCommand;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * This class represents the subcommand 'generate'.
 * 
 * @author flaxel
 * @since 1.0.0
 */
@Command(name = "generate",
		aliases = "g",
		description = "generate your own code snippets",
		sortOptions = true,
		subcommands = { PojoCommand.class })
public class GenerateCommand implements Runnable {

	/**
	 * true if the help for the user should be printed, otherwise false
	 */
	@Option(names = { "--help", "-h" }, usageHelp = true, description = "display this help message")
	private boolean usageHelpRequested;

	/**
	 * all modules that should be loaded
	 */
	@Option(names = { "-m", "--modules" }, description = "add class files")
	private File[] modules;

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
	}

	/**
	 * Create a class loader for external resources.
	 * 
	 * @return java class loader
	 * @since 1.0.0
	 */
	public ClassLoader getClassLoader() {
		if (modules == null)
			return getClass().getClassLoader();

		try {
			return Parser.createClassLoader(modules);
		} catch (MalformedURLException e) {
			Console.println("cannot load all modules");
			return getClass().getClassLoader();
		}
	}
}
