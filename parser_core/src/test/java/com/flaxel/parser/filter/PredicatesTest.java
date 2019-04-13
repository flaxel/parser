package com.flaxel.parser.filter;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

import com.flaxel.parser.utils.TestUtils;
import com.github.javaparser.StaticJavaParser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PredicatesTest {

	@Test
	public void testFileHidden() {
		File file = TestUtils.getInternFile("filter/fileRead.txt");
		assertFalse(Predicates.FILE_HIDDEN.test(file));
	}

	@Test
	public void testFileReadWrite() {
		File file = TestUtils.getInternFile("filter/unitConstants.txt");
		assertTrue(Predicates.FILE_READ_WRITE.test(file));

		file = TestUtils.getInternFile("filter/fileRead.txt");
		file.setWritable(false);
		assertFalse(Predicates.FILE_READ_WRITE.test(file));
	}

	@Test
	public void testUnitConstants() throws FileNotFoundException {
		File file = TestUtils.getInternFile("filter/unitConstants.txt");
		assertTrue(Predicates.UNIT_CONSTANTS.test(StaticJavaParser.parse(file)));

		file = TestUtils.getInternFile("filter/fileRead.txt");
		assertFalse(Predicates.UNIT_CONSTANTS.test(StaticJavaParser.parse(file)));
	}

	@Test
	public void testNonSuccessful() {
		assertThrows(AssertionError.class, () -> Predicates.FILE_HIDDEN.test(null));
		assertThrows(AssertionError.class, () -> Predicates.FILE_READ_WRITE.test(null));
		assertThrows(AssertionError.class, () -> Predicates.UNIT_CONSTANTS.test(null));
	}
}
