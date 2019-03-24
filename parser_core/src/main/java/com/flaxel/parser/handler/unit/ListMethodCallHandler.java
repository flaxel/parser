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
import java.util.function.BiConsumer;

import com.github.javaparser.Position;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.utils.PositionUtils;

import static com.github.javaparser.utils.Utils.assertNotNull;

/**
 * This class is used to list all method calls of a class.
 * 
 * @author flaxel
 * @since 1.0.0
 */
public class ListMethodCallHandler implements BiConsumer<File, CompilationUnit> {

	/**
	 * true if it should ignore the annotations for the sorting, otherwise false
	 */
	private boolean ignoringAnnotations;

	/**
	 * output stream to print all information
	 */
	private final OutputStream output;

	/**
	 * Initialize the list method call handler.
	 * 
	 * @param output
	 *            output stream to print all information
	 * @since 1.0.0
	 */
	public ListMethodCallHandler(OutputStream output) {
		this.output = assertNotNull(output);
	}

	/**
	 * Set the value whether the annotations should be ignored.
	 * 
	 * @param ignoringAnnotations
	 *            new value
	 * @return this instance
	 * @since 1.0.0
	 */
	public ListMethodCallHandler ignoringAnnotations(boolean ignoringAnnotations) {
		this.ignoringAnnotations = assertNotNull(ignoringAnnotations);
		return this;
	}

	/**
	 * Write all method calls of a class to the output stream.
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

		List<MethodCallExpr> methodCalls = unit.findAll(MethodCallExpr.class);
		PositionUtils.sortByBeginPosition(methodCalls, ignoringAnnotations);

		try (output) {
			for (MethodCallExpr methodCall : methodCalls) {
				String content = String.format("%s %s%n", methodCall.getBegin().orElse(Position.pos(-1, -1)),
						methodCall);
				output.write(content.getBytes());
			}
		} catch (IOException e) {
			// do nothing
		}
	}
}
