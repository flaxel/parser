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

import com.flaxel.parser.Console;
import com.flaxel.parser.command.utils.ClearCommand;
import com.flaxel.parser.command.utils.ExitCommand;
import com.flaxel.parser.command.utils.ManifestVersionProvider;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * This class represents the basic command for the complete parser command line
 * interface.
 * 
 * @author flaxel
 * @since 1.0.0
 */
@Command(name = "parser",
		header = { "@|green ______  _____   _______   ______  ____  _______ |@",
			"@|green \\____ \\ \\__  \\  \\_  __ \\ /  ___/_/ __ \\ \\_  __ \\|@",
			"@|green |  |_> > / __ \\_ |  | \\/ \\___ \\ \\  ___/  |  | \\/|@",
			"@|green |   __/ (____  / |__|   /____  > \\___  > |__| |@",
			"@|green |__|         \\/              \\/      \\/   |@", "" },
		versionProvider = ManifestVersionProvider.class,
		subcommands = { ClearCommand.class, ExitCommand.class })
public class ParserCommand implements Runnable {

	/**
	 * true if the version information should be printed, otherwise false
	 */
	@Option(names = { "version", "v" }, versionHelp = true, description = "print version information")
	private boolean versionRequested;

	/**
	 * true if the help for the user should be printed, otherwise false
	 */
	@Option(names = { "help", "h" }, usageHelp = true, description = "display this help message")
	private boolean usageHelpRequested;

	/**
	 * Execute the command.
	 * 
	 * @since 1.0.0
	 */
	@Override
	public void run() {
		new CommandLine(this).usage(Console.getWriter());
	}
}
