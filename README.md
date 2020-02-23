<p align="center"><img src="deployment/icon.png" alt="parser" height="150px"></p>

[![Java_CI](https://github.com/flaxel/parser/workflows/Java_CI/badge.svg)](https://github.com/flaxel/parser/actions)
[![GitHub release](https://img.shields.io/github/release/flaxel/parser.svg)](https://github.com/flaxel/parser/releases)
[![Release](https://jitpack.io/v/flaxel/parser.svg)](https://jitpack.io/#flaxel/parser)
[![License Apache-2.0](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](LICENSE)

This project contains java code to analyze, transform and generate code. It can also be used from the console with the command line interface.

## Getting Started

If you follow the next steps you can get the `parser_core` as a jar archive to analyze, transform and generate java code.

### Prerequisites

The project is created with the build tool [Maven](https://maven.apache.org/), the ide [Eclipse](https://www.eclipse.org/) and the [Java Version 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html).

### Setup

It is possible to include the jar archive with the tool [JitPack](https://jitpack.io/#flaxel/parser). Otherwise you can download the archive from the [release page](https://github.com/flaxel/parser/releases) and include the archive manually.

#### Maven

Add the repository to the build file:

```xml
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>

```

Add the dependency:

```xml
<dependency>
	<groupId>com.github.flaxel.parser</groupId>
	<artifactId>parser_core</artifactId>
	<version>1.0.0</version>
</dependency>
```

#### Gradle

Add the repository to the build file:

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

Add the dependency:

```
dependencies {
    implementation 'com.github.flaxel.parser:parser_core:1.0.0'
}
```

#### SBT

Add the repository to the build file:

```
resolvers += "jitpack" at "https://jitpack.io"	
```

Add the dependency:

```
libraryDependencies += "com.github.flaxel.parser" % "parser_core" % "1.0.0"
```

#### Leiningen

Add the repository to the build file:

```
:repositories [["jitpack" "https://jitpack.io"]]
```

Add the dependency:

```
:dependencies [[com.github.flaxel.parser/parser_core "1.0.0"]]
```

### Examples

Analyze java code - list all class elements:

```java
File file = new File("path/to/java_file");
ListClassHandler unitHandler = new ListClassHandler(System.out);
Analyzer.analyzeFile(file, unitHandler);
```

Transform java code - rename class to interface:

```java
File file = new File("path/to/java_file");
RenameHandler unitHandler = new RenameHandler(System.out, "class", (oldString) -> "interface");
Transformer.transformFile(file, unitHandler);
```

Generate java code - create a person class:

```java
File file = new File("path/to/java_file");
PojoGenerator generator = new PojoGenerator("Person", List.of(String.class, String.class), List.of("preName", "name"));
Generator.generate(generator, file);
```

You can find all [generators](https://flaxel.github.io/parser/v1/com/flaxel/parser/generator/package-summary.html), [filters](https://flaxel.github.io/parser/v1/com/flaxel/parser/filter/package-summary.html) and handlers for [analyzing](https://flaxel.github.io/parser/v1/com/flaxel/parser/handler/analyze/package-summary.html), [transforming](https://flaxel.github.io/parser/v1/com/flaxel/parser/handler/transform/package-summary.html) and [problems](https://flaxel.github.io/parser/v1/com/flaxel/parser/handler/problem/package-summary.html) in the documentation.

### Documentation

There exists a java documentation for the module `parser_core`. It is published on the branch gh-pages and can be accessed through this link https://flaxel.github.io/parser.

## Compiling and Testing

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. First it is necessary to clone the project from the master branch.

```bash
git clone https://github.com/flaxel/parser.git
```

After that you can create all jars for the project.

```bash
mvn clean install
```

It is also possible to run all tests of the project. 

```bash
mvn test
```

## Deployment

If you want to use the command line interface named `parser_cli`, please download the zip archive from the [release page](https://github.com/flaxel/parser/releases). The next steps depend on your operating system.

### Linux

There are two possibilities to add the parser. But first you must install the parser:

```bash
sudo mkdir /opt/parser

cd ~/Downloads
sudo unzip parser.zip -d /opt/parser
```

If you want to use the parser from the command line you can execute the following command:

```bash
echo "alias parser='java -jar /opt/parser/parser_cli-1.0.0-jar-with-dependencies.jar'" >> ~/.bash_aliases
```

Or you can add the parser as application:

```bash
cd /opt/parser
sudo mv parser.desktop /usr/share/applications/
```

### Windows

It is very simple to execute the parser. You have to execute the *parser.bat* and the command line is started.
