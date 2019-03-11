package com.flaxel.parser.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GenerationUtilsTest {

	@Test
	public void testPackageName() {
		assertEquals("com.parser.test", GenerationUtils.packageName("com/parser\\test"));
	}

	@Test
	public void testNonPackageName() {
		assertThrows(AssertionError.class, () -> GenerationUtils.packageName(null));
		assertThrows(AssertionError.class, () -> GenerationUtils.packageName(""));
	}

	@Test
	public void testCapitalizeCamelCase() {
		String expected = "TestClass";

		assertEquals(expected, GenerationUtils.capitalizeCamelCase("testClass"));
		assertEquals(expected, GenerationUtils.capitalizeCamelCase("_test_class_"));
	}

	@Test
	public void testNonCapitalizeCamelCase() {
		assertThrows(AssertionError.class, () -> GenerationUtils.capitalizeCamelCase(null));
		assertThrows(AssertionError.class, () -> GenerationUtils.capitalizeCamelCase(""));
	}

	@Test
	public void testDecapitalizeCamelCase() {
		String expected = "testClass";

		assertEquals(expected, GenerationUtils.decapitalizeCamelCase("TestClass"));
		assertEquals(expected, GenerationUtils.decapitalizeCamelCase("_test_class_"));
	}

	@Test
	public void testNonDecapitalizeCamelCase() {
		assertThrows(AssertionError.class, () -> GenerationUtils.decapitalizeCamelCase(null));
		assertThrows(AssertionError.class, () -> GenerationUtils.decapitalizeCamelCase(""));
	}
}
