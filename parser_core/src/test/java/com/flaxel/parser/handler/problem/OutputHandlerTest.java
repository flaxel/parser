package com.flaxel.parser.handler.problem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.flaxel.parser.utils.TestUtils;
import com.github.javaparser.JavaToken;
import com.github.javaparser.Position;
import com.github.javaparser.Problem;
import com.github.javaparser.Range;
import com.github.javaparser.TokenRange;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OutputHandlerTest {

	private File file;

	private File problemsFile;

	private List<Problem> problems;

	@BeforeEach
	public void setup() throws IOException {
		this.problemsFile = new File("problems.txt");
		this.problemsFile.deleteOnExit();
		Files.deleteIfExists(this.problemsFile.toPath());

		this.file = new File("test.java");

		TokenRange range = new TokenRange(
				new JavaToken(new Range(new Position(1, 1), new Position(2, 2)), 1, "test", null, null),
				new JavaToken(new Range(new Position(1, 1), new Position(2, 2)), 1, "test", null, null));

		this.problems = List.of(new Problem("test message", range, new RuntimeException("test exception")),
				new Problem("test message2", range, new RuntimeException("test exception2")));
	}

	@Test
	@Disabled
	public void testSeparator() throws IOException {
		OutputHandler handler = new OutputHandler(Files.newOutputStream(problemsFile.toPath())).separator('#')
				.fullStacktrace(true);
		handler.accept(file, problems);

		String content = Files.readString(problemsFile.toPath());

		assertEquals(TestUtils.readInternFile("handler/problem/ProblemOutputSeparator.txt"), content);
	}

	@Test
	@Disabled
	public void testFullStacktrace() throws IOException {
		OutputHandler handler = new OutputHandler(Files.newOutputStream(problemsFile.toPath())).fullStacktrace(true);
		handler.accept(file, problems);

		String content = Files.readString(problemsFile.toPath());

		assertEquals(TestUtils.readInternFile("handler/problem/ProblemOutputFullStacktrace.txt"), content);
	}

	@Test
	public void test() throws IOException {
		OutputHandler handler = new OutputHandler(Files.newOutputStream(problemsFile.toPath()));
		handler.accept(file, problems);

		String content = Files.readString(problemsFile.toPath());

		assertEquals(TestUtils.readInternFile("handler/problem/ProblemOutput.txt"), content);
	}

	@Test
	public void testNonSuccessful() {
		assertThrows(AssertionError.class, () -> new OutputHandler(null));
		assertThrows(AssertionError.class, () -> new OutputHandler(System.out).separator(""));
		assertThrows(AssertionError.class, () -> new OutputHandler(System.out).separator(null));
		assertThrows(AssertionError.class, () -> new OutputHandler(System.out).accept(null, problems));
		assertThrows(AssertionError.class, () -> new OutputHandler(System.out).accept(file, List.of()));
	}
}
