package com.flaxel.parser.generator;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.flaxel.parser.utils.TestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PojoGeneratorTest {

	private String packageDeclaration;

	private String className;

	private List<String> fieldNames;

	private List<Class<?>> fieldTypes;

	@BeforeEach
	private void setup() {
		this.packageDeclaration = "java/parser\\test";
		this.className = "Test";
		this.fieldTypes = List.of(Object.class, double[].class, Double[][].class, char.class, int.class, List.class);
		this.fieldNames = List.of("test1", "test2", "test3", "test4", "test5", "test6");
	}

	@Test
	public void testNoArgsConstructor() throws IOException {
		String code = new PojoGenerator(packageDeclaration, className, fieldTypes, fieldNames).noArgsConstructor(true)
				.get()
				.toString();

		assertEquals(TestUtils.readInternFile("generator/PojoNoArgsConstructor.txt"), code);
	}

	@Test
	public void testFullArgsConstructor() throws IOException {
		String code = new PojoGenerator(packageDeclaration, className, fieldTypes, fieldNames).fullArgsConstructor(true)
				.get()
				.toString();

		assertEquals(TestUtils.readInternFile("generator/PojoFullArgsConstructor.txt"), code);
	}

	@Test
	public void testEqualsAndHashCode() throws IOException {
		String code = new PojoGenerator(packageDeclaration, className, fieldTypes, fieldNames).equalsAndHashCode(true)
				.get()
				.toString();

		assertEquals(TestUtils.readInternFile("generator/PojoEqualsAndHashCode.txt"), code);
	}

	@Test
	public void testToString() throws IOException {
		String code = new PojoGenerator(packageDeclaration, className, fieldTypes, fieldNames).toString(true)
				.get()
				.toString();

		assertEquals(TestUtils.readInternFile("generator/PojoToString.txt"), code);
	}

	@Test
	public void testSeparator() throws IOException {
		String code = new PojoGenerator(packageDeclaration, className, fieldTypes, fieldNames).toString(true)
				.separator('#')
				.get()
				.toString();

		assertEquals(TestUtils.readInternFile("generator/PojoSeparator.txt"), code);
	}

	@Test
	public void testPrimeNumber() throws IOException {
		String code = new PojoGenerator(packageDeclaration, className, fieldTypes, fieldNames).equalsAndHashCode(true)
				.primeNumber(3)
				.get()
				.toString();

		assertEquals(TestUtils.readInternFile("generator/PojoPrimeNumber.txt"), code);
	}

	@Test
	public void testCallSuper() throws IOException {
		String code = new PojoGenerator(packageDeclaration, className, fieldTypes, fieldNames).equalsAndHashCode(true)
				.toString(true)
				.callSuper(true)
				.get()
				.toString();

		assertEquals(TestUtils.readInternFile("generator/PojoCallSuper.txt"), code);
	}

	@Test
	public void testIncludeFields() throws IOException {
		String code = new PojoGenerator(packageDeclaration, className, fieldTypes, fieldNames).includeFieldNames(false)
				.toString(true)
				.get()
				.toString();

		assertEquals(TestUtils.readInternFile("generator/PojoIncludeFields.txt"), code);
	}

	@Test
	public void test() throws IOException {
		String code = new PojoGenerator(packageDeclaration, className, fieldTypes, fieldNames).get().toString();

		assertEquals(TestUtils.readInternFile("generator/Pojo.txt"), code);
	}

	@Test
	public void testNonSuccessful() {
		assertThrows(AssertionError.class, () -> new PojoGenerator("", className, fieldTypes, fieldNames));
		assertThrows(AssertionError.class, () -> new PojoGenerator("", fieldTypes, fieldNames));
		assertThrows(AssertionError.class, () -> new PojoGenerator(className, List.of(), fieldNames));
		assertThrows(AssertionError.class, () -> new PojoGenerator(className, fieldTypes, List.of()));
		assertThrows(AssertionError.class, () -> new PojoGenerator(className, fieldTypes, fieldNames).separator(""));
		assertThrows(AssertionError.class, () -> new PojoGenerator(className, fieldTypes, fieldNames).primeNumber(-1));
		assertThrows(IllegalArgumentException.class, () -> new PojoGenerator(className, fieldTypes, List.of("a")));
	}
}
