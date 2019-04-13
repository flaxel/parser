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
package com.flaxel.parser.filter;

import java.io.File;
import java.util.List;
import java.util.function.Predicate;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;

import static com.github.javaparser.utils.Utils.assertNotNull;

/**
 * This class is used as a collection for all filters.
 * 
 * @author flaxel
 * @since 1.0.0
 */
public class Predicates {

	/**
	 * file filter if it is possible to write and read the file
	 * 
	 * @since 1.0.0
	 */
	public static final Predicate<File> FILE_READ_WRITE = (file) -> {
		assertNotNull(file);
		return file.canWrite() && file.canRead();
	};

	/**
	 * file filter if the file is not visible
	 * 
	 * @since 1.0.0
	 */
	public static final Predicate<File> FILE_HIDDEN = (file) -> {
		return assertNotNull(file).isHidden();
	};

	/**
	 * unit filter if the compilation unit contains constant variables
	 * 
	 * @since 1.0.0
	 */
	public static final Predicate<CompilationUnit> UNIT_CONSTANTS = (unit) -> {
		assertNotNull(unit);

		List<FieldDeclaration> constants = unit.findAll(FieldDeclaration.class,
				(field) -> field.isFinal() && field.isStatic());

		return !constants.isEmpty();
	};
}
