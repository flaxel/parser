package com.flaxel.parser.filter;

import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PredicateCollectionTest {

	private static final Predicate<String> PREDICATE_FALSE = (s) -> false;

	private static final Predicate<String> PREDICATE_TRUE = (s) -> true;

	@Test
	public void testConjunction() {
		assertFalse(new PredicateCollection<>(PREDICATE_FALSE, PREDICATE_FALSE).conjunction().test("test"));
		assertFalse(new PredicateCollection<>(PREDICATE_FALSE, PREDICATE_TRUE).conjunction().test("test"));
		assertFalse(new PredicateCollection<>(PREDICATE_TRUE, PREDICATE_FALSE).conjunction().test("test"));
		assertTrue(new PredicateCollection<>(PREDICATE_TRUE, PREDICATE_TRUE).conjunction().test("test"));
	}

	@Test
	public void testDisjunction() {
		assertFalse(new PredicateCollection<>(PREDICATE_FALSE, PREDICATE_FALSE).disjunction().test("test"));
		assertTrue(new PredicateCollection<>(PREDICATE_FALSE, PREDICATE_TRUE).disjunction().test("test"));
		assertTrue(new PredicateCollection<>(PREDICATE_TRUE, PREDICATE_FALSE).disjunction().test("test"));
		assertTrue(new PredicateCollection<>(PREDICATE_TRUE, PREDICATE_TRUE).disjunction().test("test"));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testNonSuccessful() {
		assertThrows(AssertionError.class, () -> new PredicateCollection<>(new Predicate[0]));
		assertThrows(AssertionError.class, () -> new PredicateCollection<>(List.of()));

		assertThrows(AssertionError.class,
				() -> new PredicateCollection<>(PREDICATE_TRUE, null).conjunction().test("test"));
		assertThrows(AssertionError.class,
				() -> new PredicateCollection<>(PREDICATE_FALSE, null).disjunction().test("test"));
	}
}
