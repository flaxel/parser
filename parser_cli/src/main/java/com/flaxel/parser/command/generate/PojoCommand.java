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
package com.flaxel.parser.command.generate;

import java.io.File;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.flaxel.parser.Console;
import com.flaxel.parser.Generator;
import com.flaxel.parser.command.GenerateCommand;
import com.flaxel.parser.generator.PojoGenerator;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

/**
 * This class represents the subcommand 'pojo' to generate a plain old java
 * object.
 * 
 * @author flaxel
 * @since 1.0.0
 */
@Command(name = "pojo", description = "create a plain old java object", sortOptions = true)
public class PojoCommand implements Runnable {

	/**
	 * parent command
	 */
	@ParentCommand
	private GenerateCommand parent;

	/**
	 * true if the help for the user should be printed, otherwise false
	 */
	@Option(names = { "--help", "-h" }, usageHelp = true, description = "display this help message")
	private boolean usageHelpRequested;

	/**
	 * true if an all args constructor should be added, otherwise false
	 */
	@Option(names = { "-a", "--all_args" }, description = "add an all args constructor")
	private boolean allArgsConstructor;

	/**
	 * true if a no args constructor should be added, otherwise false
	 */
	@Option(names = { "-n", "--no_args" }, description = "add a no args constructor")
	private boolean noArgsConstructor;

	/**
	 * name of the package
	 */
	@Option(names = { "-p", "--package" }, description = "add a package name")
	private String packageName;

	/**
	 * true if the toString method should be added, otherwise false
	 */
	@Option(names = { "-s", "--to_string" }, description = "add a sring representation")
	private boolean toString;

	/**
	 * true if the equals and hash method should be added, otherwise false
	 */
	@Option(names = { "-e", "--equal_hash" }, description = "add the equals and hash method")
	private boolean equalsHashCode;

	/**
	 * file where the result should be saved
	 */
	@Option(names = { "-f", "--file" }, description = "save it to the file")
	private File file;

	/**
	 * name of the class
	 */
	@Parameters(index = "0", description = "name of the class")
	private String name;

	/**
	 * all variable names
	 */
	@Option(required = true, names = { "-v", "--variables" }, split = ",", description = "all names of the variables")
	private String[] variables;

	/**
	 * all variable types
	 */
	@Option(required = true, names = { "-t", "--types" }, split = ",", description = "all types of the variables")
	private String[] typeNames;

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

		ClassLoader loader = parent.getClassLoader();
		List<Class<?>> types = new ArrayList<>();

		for (String typeName : typeNames) {
			try {
				types.add(Class.forName(typeName, true, loader));
			} catch (ClassNotFoundException e) {
				Console.println("type %s does not exist", typeName);
				return;
			}
		}

		try {
			PojoGenerator generator = new PojoGenerator(packageName, name, types,
					Arrays.stream(variables).collect(Collectors.toList())).fullArgsConstructor(allArgsConstructor)
							.noArgsConstructor(noArgsConstructor)
							.toString(toString)
							.equalsAndHashCode(equalsHashCode)
							.callSuper(true)
							.includeFieldNames(true);

			if (file == null) {
				Console.println(Generator.generate(generator));
			} else {
				Generator.generate(generator, file, StandardOpenOption.CREATE_NEW);
				Console.println("file is written");
			}
		} catch (Exception e) {
			Console.println(e.getMessage());
			return;
		}
	}
}
