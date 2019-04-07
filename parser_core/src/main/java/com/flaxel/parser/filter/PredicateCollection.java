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

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.flaxel.parser.utils.Utils.assertNonEmpty;
import static com.github.javaparser.utils.Utils.assertNotNull;

/**
 * This class is used to summarize all predicates in a collection.
 * 
 * @author flaxel
 * @since 1.0.0
 * @param <T>
 *            type of the input
 */
public class PredicateCollection<T> implements Predicate<T> {

	/**
	 * collection for all predicates
	 */
	private List<Predicate<T>> predicates;

	/**
	 * true if the predicates should be combined with a logical and, otherwise false
	 */
	private boolean conjunction;

	/**
	 * Initialize the predicates filter.
	 * 
	 * @param predicates
	 *            all predicates
	 * @since 1.0.0
	 */
	@SafeVarargs
	public PredicateCollection(Predicate<T>... predicates) {
		this(Arrays.stream(assertNotNull(predicates)).collect(Collectors.toList()));
	}

	/**
	 * Initialize the predicates filter.
	 * 
	 * @param predicates
	 *            all predicates
	 * @since 1.0.0
	 */
	public PredicateCollection(List<Predicate<T>> predicates) {
		this.predicates = assertNonEmpty(predicates);
		this.conjunction = true;
	}

	/**
	 * Set the value whether the predicates should be combined with a logical and.
	 * 
	 * @return this instance
	 * @since 1.0.0
	 */
	public PredicateCollection<T> conjunction() {
		this.conjunction = true;
		return this;
	}

	/**
	 * Set the value whether the predicates should be combined with a logical or.
	 * 
	 * @return this instance
	 * @since 1.0.0
	 */
	public PredicateCollection<T> disjunction() {
		this.conjunction = false;
		return this;
	}

	/**
	 * Return the result of the conjunction or disjunction.
	 * 
	 * @param element
	 *            input argument
	 * @since 1.0.0
	 */
	@Override
	public boolean test(T element) {
		assertNotNull(element);

		if (conjunction) {
			for (Predicate<T> predicate : predicates) {
				if (!assertNotNull(predicate).test(element))
					return false;
			}

			return true;
		}

		for (Predicate<T> predicate : predicates) {
			if (assertNotNull(predicate).test(element))
				return true;
		}

		return false;
	}
}
