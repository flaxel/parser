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
package com.flaxel.parser.handler.unit;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import static com.github.javaparser.utils.Utils.assertNonEmpty;
import static com.github.javaparser.utils.Utils.assertNotNull;

/**
 * This class is used to list all elements of a class.
 * 
 * @author flaxel
 * @since 1.0.0
 */
public class ListClassHandler implements BiConsumer<File, CompilationUnit> {

	/**
	 * output stream to print all information
	 */
	private final OutputStream output;

	/**
	 * character to separate any problems
	 */
	private Optional<String> separator;

	/**
	 * true if the program should print verbose information, otherwise false
	 */
	private boolean verbose;

	/**
	 * default separator for the problems
	 */
	public static final String DEFAULT_SEPARATOR = "#";

	/**
	 * Initialize the list class handler.
	 * 
	 * @param output
	 *            output stream to print all information
	 * @since 1.0.0
	 */
	public ListClassHandler(final OutputStream output) {
		this.output = assertNotNull(output);
		this.separator = Optional.empty();
	}

	/**
	 * Set the separator for the file name.
	 * 
	 * @param separator
	 *            new value
	 * @return this instance
	 * @since 1.0.0
	 */
	public ListClassHandler separator(String separator) {
		this.separator = Optional.of(assertNonEmpty(separator));
		return this;
	}

	/**
	 * Set the separator for the file name.
	 * 
	 * @param separator
	 *            new value
	 * @return this instance
	 * @since 1.0.0
	 */
	public ListClassHandler separator(char separator) {
		separator(Character.toString(assertNotNull(separator)));
		return this;
	}

	/**
	 * Set the value whether verbose information should be printed.
	 * 
	 * @param verbose
	 *            new value
	 * @return this instance
	 * @since 1.0.0
	 */
	public ListClassHandler verbose(boolean verbose) {
		this.verbose = assertNotNull(verbose);
		return this;
	}

	/**
	 * Write all elements of a class to the output stream and write verbose
	 * information if it is necessary.
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

		String splitter = separator.orElse(DEFAULT_SEPARATOR).repeat(20);

		StringBuilder builder = new StringBuilder();
		builder.append(String.format("%s%n%s%n%s%n%n", splitter, verbose ? source.getAbsolutePath() : source.getName(),
				splitter));

		executeIfNotEmpty(builder, "classes", unit.findAll(ClassOrInterfaceDeclaration.class), (classOrInterface) -> {
			return verbose ? classOrInterface.resolve().getQualifiedName() : classOrInterface.getNameAsString();
		});

		executeIfNotEmpty(builder, "constructors", unit.findAll(ConstructorDeclaration.class), (constructor) -> {
			return verbose ? constructor.getDeclarationAsString() : constructor.getDeclarationAsString(false, false);
		});

		executeIfNotEmpty(builder, "fields", unit.findAll(FieldDeclaration.class), (field) -> {
			return verbose ? field.getVariables().get(0).getNameAsString() : field.toString();
		});

		executeIfNotEmpty(builder, "methods", unit.findAll(MethodDeclaration.class), (method) -> {
			return verbose ? method.getDeclarationAsString() : method.getDeclarationAsString(false, false);
		});

		executeIfNotEmpty(builder, "annotations", unit.findAll(AnnotationDeclaration.class), (annotation) -> {
			return verbose ? annotation.resolve().getQualifiedName() : annotation.getNameAsString();
		});

		executeIfNotEmpty(builder, "enums", unit.findAll(EnumDeclaration.class), (enums) -> {
			return String.format("%s %s", enums.getNameAsString(), verbose ? enums.getEntries().toString() : "");
		});

		try (output) {
			output.write(builder.toString().getBytes());
		} catch (IOException e) {
			// do nothing
		}
	}

	private <T> void executeIfNotEmpty(StringBuilder builder, String content, List<T> list,
			Function<T, String> function) {
		if (!list.isEmpty()) {
			builder.append(String.format("%s:%n", content));
			list.forEach(element -> builder.append(String.format("\t%s%n", function.apply(element))));
			builder.append(String.format("%n"));
		}
	}
}
