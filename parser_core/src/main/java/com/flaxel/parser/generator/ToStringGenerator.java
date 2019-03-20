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
package com.flaxel.parser.generator;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.flaxel.parser.utils.GenerationUtils;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.utils.ClassUtils;
import com.google.common.collect.Streams;

import static com.flaxel.parser.utils.Utils.assertNonEmpty;
import static com.github.javaparser.utils.Utils.assertNonEmpty;
import static com.github.javaparser.utils.Utils.assertNotNull;

/**
 * This class is used to generate a <code>toString</code> method for a class.
 * 
 * @author flaxel
 * @since 1.0.0
 */
public class ToStringGenerator implements Supplier<MethodDeclaration> {

	/**
	 * name of the class
	 */
	private String className;

	/**
	 * all field types of the class
	 */
	private List<Class<?>> fieldTypes;

	/**
	 * all field names of the class
	 */
	private List<String> fieldNames;

	/**
	 * true if the <code>super.equals(o)</code> should be called, otherwise false
	 */
	private boolean callSuper;

	/**
	 * true if the field names should be included in the string representation,
	 * otherwise false
	 */
	private boolean includeFieldNames;

	/**
	 * separator for the fields
	 */
	private Optional<String> separator;

	/**
	 * parent compilation unit
	 */
	private CompilationUnit unit;

	/**
	 * default separator for the fields
	 */
	public static final String DEFAULT_SEPARATOR = ",";

	/**
	 * Initialize the <code>toString</code> method generator.
	 * 
	 * @param className
	 *            name of the class
	 * @param fieldTypes
	 *            all field types of the class
	 * @param fieldNames
	 *            all field names of the class
	 * @since 1.0.0
	 */
	public ToStringGenerator(String className, List<Class<?>> fieldTypes, List<String> fieldNames) {
		this.includeFieldNames = true;
		this.separator = Optional.empty();

		this.className = GenerationUtils.capitalizeCamelCase(assertNonEmpty(className));
		this.fieldTypes = assertNonEmpty(fieldTypes);
		this.fieldNames = assertNonEmpty(fieldNames).stream()
				.map(GenerationUtils::decapitalizeCamelCase)
				.collect(Collectors.toList());

		if (fieldTypes.size() != fieldNames.stream().distinct().count())
			throw new IllegalArgumentException("Types and unique fields must be the same size.");
	}

	/**
	 * Set the value whether the <code>super.equals(o)</code> method should be
	 * called.
	 * 
	 * @param callSuper
	 *            new value
	 * @return this instance
	 * @since 1.0.0
	 */
	public ToStringGenerator callSuper(boolean callSuper) {
		this.callSuper = assertNotNull(callSuper);
		return this;
	}

	/**
	 * Set the value whether the field names should be included in the string
	 * representation.
	 * 
	 * @param includeFieldNames
	 *            new value
	 * @return this instance
	 * @since 1.0.0
	 */
	public ToStringGenerator includeFieldNames(boolean includeFieldNames) {
		this.includeFieldNames = assertNotNull(includeFieldNames);
		return this;
	}

	/**
	 * Set the separator for the fields.
	 * 
	 * @param separator
	 *            new value
	 * @return this instance
	 * @since 1.0.0
	 */
	public ToStringGenerator separator(String separator) {
		this.separator = Optional.of(assertNonEmpty(separator));
		return this;
	}

	/**
	 * Set the separator for the fields.
	 * 
	 * @param separator
	 *            new value
	 * @return this instance
	 * @since 1.0.0
	 */
	public ToStringGenerator separator(char separator) {
		separator(Character.toString(assertNotNull(separator)));
		return this;
	}

	/**
	 * Set the parent compilation unit.
	 * 
	 * @param unit
	 *            new value
	 * @return this instance
	 * @since 1.0.0
	 */
	public ToStringGenerator unit(CompilationUnit unit) {
		this.unit = assertNotNull(unit);
		return this;
	}

	/**
	 * Return the <code>toString</code> method declaration.
	 * 
	 * @return method declaration
	 * @since 1.0.0
	 */
	@Override
	public MethodDeclaration get() {
		String separatorVariable = String.format("+ \"%s", separator.orElse(DEFAULT_SEPARATOR));
		StringBuilder builder = new StringBuilder(String.format("return \"%s(", className));

		if (callSuper) {
			if (includeFieldNames) {
				builder.append("super=");
			}

			builder.append(String.format("\" + super.toString() %s ", separatorVariable));
		}

		Streams.forEachPair(fieldTypes.stream(), fieldNames.stream(), (type, name) -> {
			if (includeFieldNames) {
				builder.append(String.format("%s=", name));
			}

			if (type.isArray()) {
				Class<Arrays> arrayClass = Arrays.class;

				if (unit != null) {
					unit.addImport(arrayClass);
				}

				String scopeName = (unit == null ? arrayClass.getName() : arrayClass.getSimpleName());

				if (ClassUtils.isPrimitiveOrWrapper(type.getComponentType())) {
					name = String.format("%s.toString(this.%s)", scopeName, name);
				} else {
					name = String.format("%s.deepToString(this.%s)", scopeName, name);
				}
			} else {
				name = String.format("this.%s", name);
			}

			builder.append(String.format("\" + %s %s ", name, separatorVariable));
		});

		builder.delete(builder.length() - separatorVariable.length() - 1, builder.length()).append("+ \")\";");

		BlockStmt body = new BlockStmt();
		body.addStatement(builder.toString());

		MethodDeclaration method = new MethodDeclaration();
		method.addAnnotation(Override.class);
		method.setName("toString");
		method.setType(String.class);
		method.setModifiers(Keyword.PUBLIC);
		method.setBody(body);

		return method;
	}
}
