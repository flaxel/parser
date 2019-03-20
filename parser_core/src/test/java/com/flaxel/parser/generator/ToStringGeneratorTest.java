package com.flaxel.parser.generator;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.flaxel.parser.utils.TestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ToStringGeneratorTest {

	private String className;

	private List<String> fieldNames;

	private List<Class<?>> fieldTypes;

	@BeforeEach
	private void setup() {
		this.className = "Test";
		this.fieldTypes = List.of(Object.class, double[].class, Double[][].class);
		this.fieldNames = List.of("test1", "test2", "test3");
	}

	@Test
	public void testNoFieldNames() throws IOException {
		String code = new ToStringGenerator(className, fieldTypes, fieldNames).includeFieldNames(false)
				.get()
				.toString();

		assertEquals(TestUtils.readInternFile("generator/ToStringNoFields.txt"), code);
	}

	@Test
	public void testCallSuper() throws IOException {
		String code = new ToStringGenerator(className, fieldTypes, fieldNames).callSuper(true).get().toString();

		assertEquals(TestUtils.readInternFile("generator/ToStringCallSuper.txt"), code);
	}

	@Test
	public void testSeparator() throws IOException {
		String code = new ToStringGenerator(className, fieldTypes, fieldNames).separator('#').get().toString();

		assertEquals(TestUtils.readInternFile("generator/ToStringSeparator.txt"), code);
	}

	@Test
	public void test() throws IOException {
		String code = new ToStringGenerator(className, fieldTypes, fieldNames).get().toString();

		assertEquals(TestUtils.readInternFile("generator/ToString.txt"), code);
	}

	@Test
	public void testNonSuccessful() {
		assertThrows(AssertionError.class, () -> new ToStringGenerator("", fieldTypes, fieldNames));
		assertThrows(AssertionError.class, () -> new ToStringGenerator(className, List.of(), fieldNames));
		assertThrows(AssertionError.class, () -> new ToStringGenerator(className, fieldTypes, List.of()));
		assertThrows(AssertionError.class,
				() -> new ToStringGenerator(className, fieldTypes, fieldNames).separator(""));
		assertThrows(AssertionError.class,
				() -> new ToStringGenerator(className, fieldTypes, fieldNames).separator(null));
		assertThrows(IllegalArgumentException.class, () -> new ToStringGenerator(className, fieldTypes, List.of("a")));
	}
}
