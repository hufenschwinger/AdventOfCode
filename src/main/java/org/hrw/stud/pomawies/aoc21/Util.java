package org.hrw.stud.pomawies.aoc21;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import io.reactivex.rxjava3.core.Flowable;

@NonNullByDefault
public final class Util {
	public static final char ZERO = '0';
	public static final char ONE = '1';

	private Util() {
	}

	/**
	 * Gets the long value of a String consisting of ONEs and ZEROs
	 *
	 * @param msbBinaryString input in MostSignificantBit-first notation
	 * @return long corresponding to input-String
	 */
	public static long binaryToLong(@NonNull String msbBinaryString) {
		final int inputLen = msbBinaryString.length();

		return IntStream.range(0, inputLen)
				   .filter(index -> msbBinaryString.charAt(index) == ONE)
				   .map(index -> inputLen - (index + 1))
				   .mapToLong(oneIndex -> 1L << oneIndex)
				   .sum();
	}

	public static @NonNull List<@NonNull String> readFile(@NonNull String pathFromContentRoot) throws IOException {
		Path path = Path.of(pathFromContentRoot)
						.toAbsolutePath();
		return Files.readAllLines(path, StandardCharsets.UTF_8);
	}

	public static <T> @NonNull Stream<@NonNull T> streamFile(@NonNull String pathFromContentRoot, @NonNull Function<@NonNull String, @NonNull T> mapper) throws IOException {
		return readFile(pathFromContentRoot)
				   .stream()
				   .parallel()
				   .map(mapper);
	}

	public static @NonNull Stream<@NonNull String> streamFile(@NonNull String pathFromContentRoot) throws IOException {
		return readFile(pathFromContentRoot)
				   .stream()
				   .parallel();
	}


	/**
	 * Applies all classifiers to the inputs, creating a set of sets, where in each inner set all items return the same values for all classifiers.
	 */
	public static <T> Set<Set<T>> equivalencePartitioning(Set<T> inputs, Set<Function<T, ?>> classifiers) {
		Set<Set<T>> classes = Collections.singleton(inputs);
		for (Function<T, ?> classifier : classifiers) {
			classes = classes.stream()
						  .flatMap(//split class to subclasses, using the current classifier
							  c -> c.stream()
									   .collect(Collectors.groupingBy(classifier, Collectors.toSet())) //group by output of current classifier
									   .values()//pass on the subclasses
									   .stream())
						  .collect(Collectors.toSet());
		}
		return classes;
	}

	public static Flowable<String> getLines(String pathToFile) throws IOException {
		return Flowable.fromIterable(readFile(pathToFile));
	}
}
