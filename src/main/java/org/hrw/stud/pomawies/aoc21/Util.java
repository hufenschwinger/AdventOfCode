package org.hrw.stud.pomawies.aoc21;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public final class Util {
	private Util() { }

	public static List<String> readFile(String pathFromContentRoot) throws IOException {
		Path path = Path.of(pathFromContentRoot).toAbsolutePath();
		return Files.readAllLines(path, StandardCharsets.UTF_8);
	}

	public static <T> Stream<T> streamFile(String pathFromContentRoot, Function<String, T> mapper) throws IOException {
		return readFile(pathFromContentRoot)
			.stream()
			.parallel()
			.map(mapper);
	}

	public static Stream<String> streamFile(String pathFromContentRoot) throws IOException {
		return readFile(pathFromContentRoot)
			.stream()
			.parallel();
	}
}
