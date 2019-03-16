package com.flaxel.parser.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestUtils {

	public static File getInternFile(String path) {
		return new File(TestUtils.class.getClassLoader().getResource("com/flaxel/parser/" + path).getFile());
	}

	public static Path getInternPath(String path) throws URISyntaxException {
		return Paths.get(TestUtils.class.getClassLoader().getResource("com/flaxel/parser/" + path).toURI());
	}

	public static String readInternFile(String path) throws IOException {
		return Files.readString(getInternFile(path).toPath());
	}

}
