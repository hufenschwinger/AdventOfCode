package org.hrw.stud.pomawies.aoc21;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public final class Util {
	public static final char ZERO = '0';
	public static final char ONE = '1';

	private Util() { }

	/**
	 * Gets the long value of a String consisting of ONEs and ZEROs
	 * @param msbBinaryString input ins MostSignificantBit-first notation
	 * @return long corresponding to input-String
	 */
	public static long binaryToLong(@NotNull String msbBinaryString) {
		final int inputLen = msbBinaryString.length();

		return IntStream.range(0, inputLen)
			.filter(index -> msbBinaryString.charAt(index) == ONE)
			.map(index -> inputLen - (index + 1))
			.mapToLong(oneIndex -> 1L << oneIndex)
			.sum();
	}

	public static @NotNull List<@NotNull String> readFile(@NotNull String pathFromContentRoot) throws IOException {
		Path path = Path.of(pathFromContentRoot).toAbsolutePath();
		return Files.readAllLines(path, StandardCharsets.UTF_8);
	}

	public static <T> @NotNull Stream<@NotNull T> streamFile(@NotNull String pathFromContentRoot, @NotNull Function<@NotNull String, @NotNull T> mapper) throws IOException {
		return readFile(pathFromContentRoot)
			.stream()
			.parallel()
			.map(mapper);
	}

	public static @NotNull Stream<@NotNull String> streamFile(@NotNull String pathFromContentRoot) throws IOException {
		return readFile(pathFromContentRoot)
			.stream()
			.parallel();
	}
}
