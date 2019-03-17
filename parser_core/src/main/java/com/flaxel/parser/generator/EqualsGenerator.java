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
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.flaxel.parser.utils.GenerationUtils;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.utils.ClassUtils;
import com.github.javaparser.utils.Utils;
import com.google.common.collect.Streams;

import static com.flaxel.parser.utils.Utils.assertNonEmpty;
import static com.github.javaparser.utils.Utils.assertNonEmpty;
import static com.github.javaparser.utils.Utils.assertNotNull;

/**
 * This class is used to generate a <code>equals</code> method for a class.
 * 
 * @author flaxel
 * @since 1.0.0
 */
public class EqualsGenerator implements Supplier<Node> {

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
	 * true if the <code>super.equals(o)</code> method should be called, otherwise
	 * false
	 */
	private boolean callSuper;

	/**
	 * parent compilation unit
	 */
	private CompilationUnit unit;

	/**
	 * Initialize the <code>equals</code> method generator.
	 * 
	 * @param className
	 *            name of the class
	 * @param fieldTypes
	 *            all field types of the class
	 * @param fieldNames
	 *            all field names of the class
	 * @since 1.0.0
	 */
	public EqualsGenerator(String className, List<Class<?>> fieldTypes, List<String> fieldNames) {
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
	public EqualsGenerator callSuper(boolean callSuper) {
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
	public EqualsGenerator unit(CompilationUnit unit) {
		this.unit = assertNotNull(unit);
		return this;
	}

	/**
	 * Return the <code>equals</code> method declaration.
	 * 
	 * @return method declaration
	 * @since 1.0.0
	 */
	@Override
	public MethodDeclaration get() {
		String objectName = "o";
		String otherClassObject = "other";

		BlockStmt body = new BlockStmt();

		IfStmt ifThis = new IfStmt()
				.setCondition(new BinaryExpr(new NameExpr(objectName), new ThisExpr(), BinaryExpr.Operator.EQUALS))
				.setThenStmt(new ReturnStmt("true"));
		body.addStatement(ifThis);

		IfStmt ifInstanceof = new IfStmt();
		ifInstanceof.setCondition(new UnaryExpr()
				.setExpression(new EnclosedExpr(new InstanceOfExpr().setType(className).setExpression(objectName)))
				.setOperator(UnaryExpr.Operator.LOGICAL_COMPLEMENT));
		ifInstanceof.setThenStmt(new ReturnStmt("false"));
		body.addStatement(ifInstanceof);

		AssignExpr castObjectExpr = new AssignExpr()
				.setTarget(new NameExpr(String.format("%s %s", className, otherClassObject)))
				.setValue(new CastExpr().setType(className).setExpression(objectName))
				.setOperator(AssignExpr.Operator.ASSIGN);
		body.addStatement(castObjectExpr);

		if (callSuper) {
			IfStmt ifSuper = new IfStmt();
			ifSuper.setCondition(new UnaryExpr().setExpression(new MethodCallExpr().setName("equals")
					.setArguments(new NodeList<>(new NameExpr(objectName)))
					.setScope(new SuperExpr())).setOperator(UnaryExpr.Operator.LOGICAL_COMPLEMENT));
			ifSuper.setThenStmt(new ReturnStmt("false"));
			body.addStatement(ifSuper);
		}

		Streams.forEachPair(fieldTypes.stream(), fieldNames.stream(), (type, name) -> {
			Expression condition = null;

			if (ClassUtils.isPrimitiveOrWrapper(type)) {
				String scope = Utils.capitalize(type.getSimpleName());

				if (type.equals(char.class)) {
					scope = "Character";
				} else if (type.equals(int.class)) {
					scope = "Integer";
				}

				condition = new BinaryExpr()
						.setLeft(new MethodCallExpr().setScope(new NameExpr(scope))
								.setName("compare")
								.setArguments(new NodeList<>(new NameExpr(String.format("this.%s", name)),
										new NameExpr(String.format("%s.%s", otherClassObject, name)))))
						.setRight(new NameExpr("0"))
						.setOperator(BinaryExpr.Operator.NOT_EQUALS);
			} else if (type.isArray()) {
				Class<Arrays> arrayClass = Arrays.class;

				if (unit != null) {
					unit.addImport(arrayClass);
				}

				String scopeName = (unit == null ? arrayClass.getName() : arrayClass.getSimpleName());

				if (ClassUtils.isPrimitiveOrWrapper(type.getComponentType())) {
					condition = new UnaryExpr().setOperator(UnaryExpr.Operator.LOGICAL_COMPLEMENT)
							.setExpression(new MethodCallExpr()
									.setArguments(new NodeList<>(new NameExpr(String.format("this.%s", name)),
											new NameExpr(String.format("%s.%s", otherClassObject, name))))
									.setScope(new NameExpr(scopeName))
									.setName("equals"));
				} else {
					condition = new UnaryExpr().setOperator(UnaryExpr.Operator.LOGICAL_COMPLEMENT)
							.setExpression(new MethodCallExpr()
									.setArguments(new NodeList<>(new NameExpr(String.format("this.%s", name)),
											new NameExpr(String.format("%s.%s", otherClassObject, name))))
									.setScope(new NameExpr(scopeName))
									.setName("deepEquals"));
				}
			} else {
				condition = new UnaryExpr().setOperator(UnaryExpr.Operator.LOGICAL_COMPLEMENT)
						.setExpression(new MethodCallExpr()
								.setArguments(
										new NodeList<>(new NameExpr(String.format("%s.%s", otherClassObject, name))))
								.setScope(new NameExpr(String.format("this.%s", name)))
								.setName("equals"));
			}

			IfStmt ifField = new IfStmt();
			ifField.setCondition(condition);
			ifField.setThenStmt(new ReturnStmt("false"));
			body.addStatement(ifField);
		});

		body.addStatement(new ReturnStmt("true"));

		MethodDeclaration method = new MethodDeclaration();
		method.addAnnotation(Override.class);
		method.setName("equals");
		method.setType(boolean.class);
		method.setModifiers(Keyword.PUBLIC);
		method.addParameter(Object.class, objectName);
		method.setBody(body);

		return method;
	}
}
