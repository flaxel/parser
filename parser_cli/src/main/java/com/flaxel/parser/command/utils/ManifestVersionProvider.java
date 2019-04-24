/**		
 *		Copyright [2019] [flaxel]
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *		 
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package com.flaxel.parser.command.utils;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import picocli.CommandLine.IVersionProvider;

/**
 * This class provides version information for the program.
 * 
 * @author flaxel
 * @since 1.0.0
 */
public class ManifestVersionProvider implements IVersionProvider {

	/**
	 * Get the version information from the manifest.
	 * 
	 * @return version information
	 * @since 1.0.0
	 */
	@Override
	public String[] getVersion() throws Exception {
		Enumeration<URL> resources = getClass().getClassLoader().getResources("META-INF/MANIFEST.MF");

		while (resources.hasMoreElements()) {
			URL url = resources.nextElement();

			try {
				Manifest manifest = new Manifest(url.openStream());

				if (isApplicableManifest(manifest)) {
					Attributes attr = manifest.getMainAttributes();
					return new String[] { get(attr, "Implementation-Version") };
				}
			} catch (IOException ex) {
				return new String[] { "Unable to read from " + url + ": " + ex };
			}
		}

		return new String[0];
	}

	/**
	 * Check if the manifest file can be used to generate version information.
	 * 
	 * @param manifest
	 *            instance of a manifest
	 * @return true if the manifest file can be used to get the version information,
	 *         otherwise false
	 * @since 1.0.0
	 */
	private boolean isApplicableManifest(Manifest manifest) {
		Attributes attributes = manifest.getMainAttributes();
		return "parser_cli".equals(get(attributes, "Implementation-Title"));
	}

	/**
	 * Get the attribute value for a specific attribute key.
	 * 
	 * @param attributes
	 *            all attributes of of the manifest file
	 * @param key
	 *            key for an attribute value
	 * @return value of the attribute name
	 * @since 1.0.0
	 */
	private String get(Attributes attributes, String key) {
		return Objects.toString(attributes.get(new Attributes.Name(key)));
	}
}