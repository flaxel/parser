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
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.utils.ClassUtils;
import com.github.javaparser.utils.Utils;
import com.google.common.collect.Streams;

import static com.flaxel.parser.utils.Utils.assertNonEmpty;
import static com.github.javaparser.utils.Utils.assertNotNull;
import static com.github.javaparser.utils.Utils.assertPositive;

/**
 * This class is used to generate a <code>hashCode</code> method for a class.
 * 
 * @author flaxel
 * @since 1.0.0
 */
public class HashCodeGenerator implements Supplier<MethodDeclaration> {

	/**
	 * all field types of the class
	 */
	private List<Class<?>> fieldTypes;

	/**
	 * all field names of the class
	 */
	private List<String> fieldNames;

	/**
	 * true if the <code>super.hashCode()</code> method should be called, otherwise
	 * false
	 */
	private boolean callSuper;

	/**
	 * prime number
	 */
	private Optional<Integer> primeNumber;

	/**
	 * parent compilation unit
	 */
	private CompilationUnit unit;

	/**
	 * default prime number
	 */
	public static final int DEFAULT_PRIME_NUMBER = 59;

	/**
	 * Initialize the <code>hashCode</code> method generator.
	 * 
	 * @param fieldTypes
	 *            all field types of the class
	 * @param fieldNames
	 *            all field names of the class
	 * @since 1.0.0
	 */
	public HashCodeGenerator(List<Class<?>> fieldTypes, List<String> fieldNames) {
		this.primeNumber = Optional.empty();

		this.fieldTypes = assertNonEmpty(fieldTypes);
		this.fieldNames = assertNonEmpty(fieldNames).stream()
				.map(GenerationUtils::decapitalizeCamelCase)
				.collect(Collectors.toList());

		if (fieldTypes.size() != fieldNames.stream().distinct().count())
			throw new IllegalArgumentException("Types and unique fields must be the same size.");
	}

	/**
	 * Set the new prime number.
	 * 
	 * @param primeNumber
	 *            new value
	 * @return this instance
	 * @since 1.0.0
	 */
	public HashCodeGenerator primeNumber(int primeNumber) {
		this.primeNumber = Optional.of(assertPositive(assertNotNull(primeNumber)));
		return this;
	}

	/**
	 * Set the value whether the <code>super.hashCode()</code> method should be
	 * called.
	 * 
	 * @param callSuper
	 *            new value
	 * @return this instance
	 * @since 1.0.0
	 */
	public HashCodeGenerator callSuper(boolean callSuper) {
		this.callSuper = assertNotNull(callSuper);
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
	public HashCodeGenerator unit(CompilationUnit unit) {
		this.unit = assertNotNull(unit);
		return this;
	}

	/**
	 * Return the <code>hashCode</code> method declaration.
	 * 
	 * @return method declaration
	 * @since 1.0.0
	 */
	@Override
	public MethodDeclaration get() {
		String prime = "PRIME";
		String result = "result";

		BlockStmt body = new BlockStmt();

		AssignExpr exprPrime = new AssignExpr().setTarget(new NameExpr(String.format("final int %s", prime)))
				.setValue(new NameExpr(Integer.toString(primeNumber.orElse(DEFAULT_PRIME_NUMBER))))
				.setOperator(AssignExpr.Operator.ASSIGN);
		body.addStatement(exprPrime);

		AssignExpr exprResult = new AssignExpr().setTarget(new NameExpr(String.format("int %s", result)))
				.setValue(new NameExpr("1"))
				.setOperator(AssignExpr.Operator.ASSIGN);
		body.addStatement(exprResult);

		if (callSuper) {
			AssignExpr exprSuper = new AssignExpr().setTarget(new NameExpr(result))
					.setValue(new NameExpr(String.format("(%s * %s) + super.hashCode()", result, prime)))
					.setOperator(AssignExpr.Operator.ASSIGN);
			body.addStatement(exprSuper);
		}

		Streams.forEachPair(fieldTypes.stream(), fieldNames.stream(), (type, name) -> {
			String addValue = null;

			if (ClassUtils.isPrimitiveOrWrapper(type)) {
				String scope = Utils.capitalize(type.getSimpleName());

				if (type.equals(char.class)) {
					scope = "Character";
				} else if (type.equals(int.class)) {
					scope = "Integer";
				}

				addValue = String.format("%s.hashCode(%s)", scope, name);

			} else if (type.isArray()) {
				Class<Arrays> arrayClass = Arrays.class;

				if (unit != null) {
					unit.addImport(arrayClass);
				}

				String scopeName = (unit == null ? arrayClass.getName() : arrayClass.getSimpleName());

				if (ClassUtils.isPrimitiveOrWrapper(type.getComponentType())) {
					addValue = String.format("%s.hashCode(%s)", scopeName, name);
				} else {
					addValue = String.format("%s.deepHashCode(%s)", scopeName, name);
				}
			} else {
				addValue = String.format("%s.hashCode()", name);
			}

			AssignExpr exprField = new AssignExpr().setTarget(new NameExpr(result))
					.setValue(new NameExpr(String.format("(%s * %s) + %s", result, prime, addValue)))
					.setOperator(AssignExpr.Operator.ASSIGN);
			body.addStatement(exprField);
		});

		body.addStatement(new ReturnStmt(result));

		MethodDeclaration method = new MethodDeclaration();
		method.addAnnotation(Override.class);
		method.setName("hashCode");
		method.setType(int.class);
		method.setModifiers(Keyword.PUBLIC);
		method.setBody(body);

		return method;
	}
}
