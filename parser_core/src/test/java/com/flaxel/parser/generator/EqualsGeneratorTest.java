package com.flaxel.parser.generator;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.flaxel.parser.utils.TestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EqualsGeneratorTest {

	private String className;

	private List<String> fieldNames;

	private List<Class<?>> fieldTypes;

	@BeforeEach
	private void setup() {
		this.className = "Test";
		this.fieldTypes = List.of(Object.class, double[].class, Double[][].class, char.class, int.class, List.class);
		this.fieldNames = List.of("test1", "test2", "test3", "test4", "test5", "test6");
	}

	@Test
	public void testCallSuper() throws IOException {
		String code = new EqualsGenerator(className, fieldTypes, fieldNames).callSuper(true).get().toString();

		assertEquals(TestUtils.readInternFile("generator/EqualsCallSuper.txt"), code);
	}

	@Test
	public void test() throws IOException {
		String code = new EqualsGenerator(className, fieldTypes, fieldNames).get().toString();

		assertEquals(TestUtils.readInternFile("generator/Equals.txt"), code);
	}

	@Test
	public void testNonSuccessful() {
		assertThrows(AssertionError.class, () -> new EqualsGenerator("", fieldTypes, fieldNames));
		assertThrows(AssertionError.class, () -> new EqualsGenerator(className, List.of(), fieldNames));
		assertThrows(AssertionError.class, () -> new EqualsGenerator(className, fieldTypes, List.of()));
		assertThrows(IllegalArgumentException.class, () -> new EqualsGenerator(className, fieldTypes, List.of("a")));
	}
}
