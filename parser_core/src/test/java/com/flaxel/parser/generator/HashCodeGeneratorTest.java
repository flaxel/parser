package com.flaxel.parser.generator;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.flaxel.parser.utils.TestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HashCodeGeneratorTest {

	private List<String> fieldNames;

	private List<Class<?>> fieldTypes;

	@BeforeEach
	private void setup() {
		this.fieldTypes = List.of(Object.class, double[].class, Double[][].class, char.class, int.class, List.class);
		this.fieldNames = List.of("test1", "test2", "test3", "test4", "test5", "test6");
	}

	@Test
	public void testCallSuper() throws IOException {
		String code = new HashCodeGenerator(fieldTypes, fieldNames).callSuper(true).get().toString();

		assertEquals(TestUtils.readInternFile("generator/HashCodeCallSuper.txt"), code);
	}

	@Test
	public void testPrimeNumber() throws IOException {
		String code = new HashCodeGenerator(fieldTypes, fieldNames).primeNumber(3).get().toString();

		assertEquals(TestUtils.readInternFile("generator/HashCodePrimeNumber.txt"), code);
	}

	@Test
	public void test() throws IOException {
		String code = new HashCodeGenerator(fieldTypes, fieldNames).get().toString();

		assertEquals(TestUtils.readInternFile("generator/HashCode.txt"), code);
	}

	@Test
	public void testNonSuccessful() {
		assertThrows(AssertionError.class, () -> new HashCodeGenerator(List.of(), fieldNames));
		assertThrows(AssertionError.class, () -> new HashCodeGenerator(fieldTypes, List.of()));
		assertThrows(AssertionError.class, () -> new HashCodeGenerator(fieldTypes, fieldNames).primeNumber(-1));
		assertThrows(IllegalArgumentException.class, () -> new HashCodeGenerator(fieldTypes, List.of("a")));
	}
}
