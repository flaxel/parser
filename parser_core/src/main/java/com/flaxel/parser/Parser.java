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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import static com.github.javaparser.utils.Utils.assertNotNull;

/**
 * This class is used as facade to change global properties.
 * 
 * @author flaxel
 * @since 1.0.0
 */
public class Parser {

	/**
	 * default configuration for the parser
	 */
	private static ParserConfiguration configuration = new ParserConfiguration();

	/**
	 * Create a new combined type solver for all files and the jre. A file can be a
	 * folder or a jar archive.
	 * 
	 * @param files
	 *            all files
	 * @return java symbol resolver
	 * @throws IOException
	 *             if an i/o error occurred while a archive is read
	 * @since 1.0.0
	 */
	public static JavaSymbolSolver createTypeSolver(File... files) throws IOException {
		assertNotNull(files);

		List<String> availableArchives = List.of("jar", "zip");
		CombinedTypeSolver combined = new CombinedTypeSolver();
		combined.add(new ReflectionTypeSolver());

		for (File file : files) {
			assertNotNull(file);
			if (file.isDirectory()) {
				combined.add(new JavaParserTypeSolver(file, configuration));
			} else if (file.isFile() && availableArchives.contains(file.getName().split("\\.")[1])) {
				combined.add(JarTypeSolver.getJarTypeSolver(file.getPath()));
			} else {
				if (file.getParent() != null) {
					combined.add(new JavaParserTypeSolver(file.getParent()));
				}
			}
		}

		return new JavaSymbolSolver(combined);
	}

	/**
	 * Create a new combined type solver for all files and the jre. A file can be a
	 * folder or a jar archive.
	 * 
	 * @param paths
	 *            all paths
	 * @return java symbol resolver
	 * @throws IOException
	 *             if an i/o error occurred
	 * @since 1.0.0
	 */
	public static JavaSymbolSolver createTypeSolver(Path... paths) throws IOException {
		assertNotNull(paths);
		return createTypeSolver(Arrays.stream(paths).map(Path::toFile).toArray(File[]::new));
	}

	/**
	 * Create a new class loader for all files and the jre. A file can be a jar
	 * archive or a class file.
	 * 
	 * @param files
	 *            all files
	 * @return java class loader
	 * @throws MalformedURLException
	 *             malformed url occured
	 * @since 1.0.0
	 */
	public static ClassLoader createClassLoader(File... files) throws MalformedURLException {
		assertNotNull(files);

		List<URL> urls = new ArrayList<>();

		for (File file : files) {
			String name = assertNotNull(file).getName();
			if (name.endsWith(".jar") || name.endsWith(".class")) {
				urls.add(file.toURI().toURL());
			}
		}

		return new URLClassLoader(urls.toArray(URL[]::new), Parser.class.getClassLoader());
	}

	/**
	 * Create a new class loader for all files and the jre. A file can be a jar
	 * archive or a class file.
	 * 
	 * @param paths
	 *            all paths
	 * @return java class loader
	 * @throws MalformedURLException
	 *             malformed url occured
	 * @since 1.0.0
	 */
	public static ClassLoader createClassLoader(Path... paths) throws MalformedURLException {
		assertNotNull(paths);
		return createClassLoader(Arrays.stream(paths).map(Path::toFile).toArray(File[]::new));
	}

	/**
	 * Set all parser configuration.
	 * 
	 * @param configuration
	 *            new parser configuration
	 * @since 1.0.0
	 */
	public static void setConfiguration(ParserConfiguration configuration) {
		assertNotNull(configuration);

		Analyzer.setConfiguration(configuration);
		Transformer.setConfiguration(configuration);
		StaticJavaParser.setConfiguration(configuration);
		Parser.configuration = configuration;
	}

	/**
	 * Set a new printer to create a string from a {@link Node}
	 * 
	 * @param printer
	 *            new printer
	 * @since 1.0.0
	 */
	public static void setPrinter(Function<Node, String> printer) {
		assertNotNull(printer);

		Generator.setPrinter(printer);
	}
}
