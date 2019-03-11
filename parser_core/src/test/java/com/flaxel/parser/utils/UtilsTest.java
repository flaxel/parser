package com.flaxel.parser.utils;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UtilsTest {

	@Test
	public void testListAssertNonEmpty() {
		assertEquals(List.of(1, 2, 3), Utils.assertNonEmpty(List.of(1, 2, 3)));
	}

	@Test
	public void testListAssertEmpty() {
		assertThrows(AssertionError.class, () -> Utils.assertNonEmpty(List.of()));
		assertThrows(AssertionError.class, () -> Utils.assertNonEmpty(null));
	}
}
