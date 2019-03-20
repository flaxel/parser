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

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.flaxel.parser.utils.GenerationUtils;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.google.common.collect.Streams;

import static com.flaxel.parser.utils.Utils.assertNonEmpty;
import static com.github.javaparser.utils.Utils.assertNonEmpty;
import static com.github.javaparser.utils.Utils.assertNotNull;
import static com.github.javaparser.utils.Utils.assertPositive;

/**
 * This class is used to generate a plain old java object (pojo) class.
 * 
 * @author flaxel
 * @since 1.0.0
 */
public class PojoGenerator implements Supplier<CompilationUnit> {

	/**
	 * package declaration of the class
	 */
	private Optional<String> packageDeclaration;

	/**
	 * name of the class
	 */
	private String className;

	/**
	 * true if the class should include a constructor with all arguments, otherwise
	 * false
	 */
	private boolean fullArgsConstructor;

	/**
	 * true if the class should include a constructor with no arguments, otherwise
	 * false
	 */
	private boolean noArgsConstructor;

	/**
	 * all field types of the class
	 */
	private List<Class<?>> fieldTypes;

	/**
	 * all field names of the class
	 */
	private List<String> fieldNames;

	/**
	 * true if the class should include the <code>equals</code> and
	 * <code>hashCode</code> method, otherwise false
	 */
	private boolean equalsAndHashCode;

	/**
	 * true if the class should include the <code>toString</code> method, otherwise
	 * false
	 */
	private boolean toString;

	/**
	 * true if the field names should be included in the string representation,
	 * otherwise false
	 */
	private boolean includeFieldNames;

	/**
	 * true if the <code>super.equals(o)</code> method should be called, otherwise
	 * false
	 */
	private boolean callSuper;

	/**
	 * separator for the fields in the string representation
	 */
	private String separator;

	/**
	 * prime number for the hashCode method
	 */
	private Integer primeNumber;

	/**
	 * Initialize the pojo class generator.
	 * 
	 * @param packageDeclaration
	 *            package declaration of the class
	 * @param className
	 *            name of the class
	 * @param fieldTypes
	 *            all field types of the class
	 * @param fieldNames
	 *            all field names of the class
	 * @since 1.0.0
	 */
	public PojoGenerator(String packageDeclaration, String className, List<Class<?>> fieldTypes,
			List<String> fieldNames) {
		this.includeFieldNames = true;
		this.separator = ToStringGenerator.DEFAULT_SEPARATOR;
		this.primeNumber = HashCodeGenerator.DEFAULT_PRIME_NUMBER;

		this.className = GenerationUtils.capitalizeCamelCase(assertNonEmpty(className));
		this.packageDeclaration = packageDeclaration == null ? Optional.empty()
				: Optional.of(GenerationUtils.packageName(assertNonEmpty(packageDeclaration)));

		this.fieldTypes = assertNonEmpty(fieldTypes);
		this.fieldNames = assertNonEmpty(fieldNames).stream()
				.map(GenerationUtils::decapitalizeCamelCase)
				.collect(Collectors.toList());

		if (fieldTypes.size() != fieldNames.stream().distinct().count())
			throw new IllegalArgumentException("Types and fields must be the same size.");
	}

	/**
	 * Initialize the pojo class generator.
	 * 
	 * @param className
	 *            name of the class
	 * @param fieldTypes
	 *            all field types of the class
	 * @param fieldNames
	 *            all field names of the class
	 * @since 1.0.0
	 */
	public PojoGenerator(String className, List<Class<?>> fieldTypes, List<String> fieldNames) {
		this(null, className, fieldTypes, fieldNames);
	}

	/**
	 * Set the value whether the class should include a constructor with no
	 * arguments.
	 * 
	 * @param noArgsConstructor
	 *            new value
	 * @return this instance
	 * @since 1.0.0
	 */
	public PojoGenerator noArgsConstructor(boolean noArgsConstructor) {
		this.noArgsConstructor = assertNotNull(noArgsConstructor);
		return this;
	}

	/**
	 * Set the value whether the class should include a constructor with all
	 * arguments.
	 * 
	 * @param fullArgsConstructor
	 *            new value
	 * @return this instance
	 * @since 1.0.0
	 */
	public PojoGenerator fullArgsConstructor(boolean fullArgsConstructor) {
		this.fullArgsConstructor = assertNotNull(fullArgsConstructor);
		return this;
	}

	/**
	 * Set the value whether the class should include the <code>toString</code>
	 * method.
	 * 
	 * @param toString
	 *            new value
	 * @return this instance
	 * @since 1.0.0
	 */
	public PojoGenerator toString(boolean toString) {
		this.toString = assertNotNull(toString);
		return this;
	}

	/**
	 * Set the value whether the class should include the <code>equals</code> and
	 * <code>hashCode</code> method.
	 * 
	 * @param equalsAndHashCode
	 *            new value
	 * @return this instance
	 * @since 1.0.0
	 */
	public PojoGenerator equalsAndHashCode(boolean equalsAndHashCode) {
		this.equalsAndHashCode = assertNotNull(equalsAndHashCode);
		return this;
	}

	/**
	 * Set the value whether the <code>super.equals(o)</code> and
	 * <code>super.hashCode()</code> method should be called.
	 * 
	 * @param callSuper
	 *            new value
	 * @return this instance
	 * @since 1.0.0
	 */
	public PojoGenerator callSuper(boolean callSuper) {
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
	public PojoGenerator includeFieldNames(boolean includeFieldNames) {
		this.includeFieldNames = assertNotNull(includeFieldNames);
		return this;
	}

	/**
	 * Set the separator for the fields in the string representation.
	 * 
	 * @param separator
	 *            new value
	 * @return this instance
	 * @since 1.0.0
	 */
	public PojoGenerator separator(String separator) {
		this.separator = assertNonEmpty(separator);
		return this;
	}

	/**
	 * Set the separator for the fields in the string representation.
	 * 
	 * @param separator
	 *            new value
	 * @return this instance
	 * @since 1.0.0
	 */
	public PojoGenerator separator(char separator) {
		separator(Character.toString(assertNotNull(separator)));
		return this;
	}

	/**
	 * Set the prime number for the hashCode method.
	 * 
	 * @param primeNumber
	 *            new value
	 * @return this instance
	 * @since 1.0.0
	 */
	public PojoGenerator primeNumber(int primeNumber) {
		this.primeNumber = assertPositive(assertNotNull(primeNumber));
		return this;
	}

	/**
	 * Return the new compilation unit.
	 * 
	 * @return generated compilation unit
	 * @since 1.0.0
	 */
	@Override
	public CompilationUnit get() {
		CompilationUnit unit = new CompilationUnit();
		packageDeclaration.ifPresent(unit::setPackageDeclaration);

		ClassOrInterfaceDeclaration clazz = unit.addClass(className);

		addFields(clazz);
		addConstructors(clazz);
		addMethods(unit, clazz);

		return unit;
	}

	/**
	 * Add all private fields.
	 * 
	 * @param clazz
	 *            definition of a class
	 * @since 1.0.0
	 */
	private void addFields(ClassOrInterfaceDeclaration clazz) {
		Streams.forEachPair(fieldTypes.stream(), fieldNames.stream(), clazz::addPrivateField);
	}

	/**
	 * Add all constructors.
	 * 
	 * @param clazz
	 *            definition of a class
	 * @since 1.0.0
	 */
	private void addConstructors(ClassOrInterfaceDeclaration clazz) {
		if (noArgsConstructor) {
			clazz.addConstructor(Keyword.PUBLIC);
		}

		if (fullArgsConstructor) {
			ConstructorDeclaration constructor = clazz.addConstructor(Keyword.PUBLIC);
			BlockStmt body = new BlockStmt();

			clazz.tryAddImportToParentCompilationUnit(Objects.class);

			Streams.forEachPair(fieldTypes.stream(), fieldNames.stream(), (type, name) -> {
				constructor.addParameter(type, name);
				body.addStatement(String.format("this.%s = Objects.requireNonNull(%s);", name, name));
			});

			constructor.setBody(body);
		}
	}

	/**
	 * Add all methods.
	 * 
	 * @param unit
	 *            entire compilation unit
	 * @param clazz
	 *            definition of a class
	 * @since 1.0.0
	 */
	private void addMethods(CompilationUnit unit, ClassOrInterfaceDeclaration clazz) {
		Streams.forEachPair(fieldTypes.stream(), fieldNames.stream(), (type, name) -> {
			BlockStmt voidBody = new BlockStmt().addStatement(String.format("this.%s = %s;", name, name));
			clazz.addMethod(CodeGenerationUtils.setterName(name), Keyword.PUBLIC)
					.addParameter(type, name)
					.setBody(voidBody);

			BlockStmt returnBody = new BlockStmt().addStatement(String.format("return %s;", name));
			clazz.addMethod(CodeGenerationUtils.getterName(type, name), Keyword.PUBLIC)
					.setType(type)
					.setBody(returnBody);
		});

		if (equalsAndHashCode) {
			clazz.addMember(new HashCodeGenerator(fieldTypes, fieldNames).primeNumber(primeNumber)
					.callSuper(callSuper)
					.unit(unit)
					.get());

			clazz.addMember(
					new EqualsGenerator(className, fieldTypes, fieldNames).callSuper(callSuper).unit(unit).get());
		}

		if (toString) {
			clazz.addMember(new ToStringGenerator(className, fieldTypes, fieldNames).callSuper(callSuper)
					.includeFieldNames(includeFieldNames)
					.separator(separator)
					.unit(unit)
					.get());
		}
	}
}
