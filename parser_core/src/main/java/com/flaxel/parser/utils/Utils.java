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

import java.util.Collection;

/**
 * This class is used as a general utils collection.
 * 
 * @author flaxel
 * @since 1.0.0
 */
public class Utils {

	/**
	 * Check whether a collection is <code>null</code> or empty.
	 * 
	 * @param <T>
	 *            type of the collection
	 * @param collection
	 *            collection instance
	 * @return checked collection
	 * @since 1.0.0
	 */
	public static <T extends Collection<?>> T assertNonEmpty(T collection) {
		if (collection == null || collection.isEmpty())
			throw new AssertionError("A list was unexpectedly empty.");

		return collection;
	}
}
