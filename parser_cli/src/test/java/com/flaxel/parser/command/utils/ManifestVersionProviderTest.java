package com.flaxel.parser.command.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ManifestVersionProviderTest {

	@Test
	public void test() throws Exception {
		String[] s = new ManifestVersionProvider().getVersion();
		assertEquals("1.0.0-SNAPSHOT", s[0]);
	}
}
