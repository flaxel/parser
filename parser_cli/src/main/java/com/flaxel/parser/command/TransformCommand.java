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
import com.flaxel.parser.command.transform.EditCommand;
import com.flaxel.parser.command.transform.RemoveCommand;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * This class represents the subcommand 'transform'.
 * 
 * @author flaxel
 * @since 1.0.0
 */
@Command(name = "transform",
		aliases = "t",
		description = "transform your own code",
		sortOptions = true,
		subcommands = { EditCommand.class, RemoveCommand.class })
public class TransformCommand implements Runnable {

	/**
	 * true if the help for the user should be printed, otherwise false
	 */
	@Option(names = { "--help", "-h" }, usageHelp = true, description = "display this help message")
	private boolean usageHelpRequested;

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
}
