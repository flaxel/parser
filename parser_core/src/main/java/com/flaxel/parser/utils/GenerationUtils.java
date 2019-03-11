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
package com.flaxel.parser.utils;

import static com.github.javaparser.utils.Utils.assertNonEmpty;
import static com.github.javaparser.utils.Utils.capitalize;
import static com.github.javaparser.utils.Utils.decapitalize;
import static com.github.javaparser.utils.Utils.screamingToCamelCase;

/**
 * This class is used as a utils collection for code generation.
 * 
 * @author flaxel
 * @since 1.0.0
 */
public class GenerationUtils {

	/**
	 * Remove file separators and replace it with a dot.
	 * 
	 * @param name
	 *            old package name
	 * @return new package name
	 * @since 1.0.0
	 */
	public static String packageName(String name) {
		return assertNonEmpty(name).replaceAll("/|\\\\", ".");
	}

	/**
	 * Create a string in camel case and/or capitalize the first letter.
	 * 
	 * @param name
	 *            old name
	 * @return new name
	 * @since 1.0.0
	 */
	public static String capitalizeCamelCase(String name) {
		assertNonEmpty(name);

		if (name.contains("_"))
			return capitalize(screamingToCamelCase(name));

		return capitalize(name);
	}

	/**
	 * Create a string in camel case and/or decapitalize the first letter.
	 * 
	 * @param name
	 *            old name
	 * @return new name
	 * @since 1.0.0
	 */
	public static String decapitalizeCamelCase(String name) {
		assertNonEmpty(name);

		if (name.contains("_"))
			return decapitalize(screamingToCamelCase(name));

		return decapitalize(name);
	}
}
