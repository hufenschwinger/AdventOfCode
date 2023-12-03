package org.hrw.stud.pomawies.aoc23;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.hrw.stud.pomawies.aoc21.Util;

@NonNullByDefault
public class DayThree {

	private record Coordinates(int x, int y) implements Comparable<Coordinates> {
		private static final Comparator<Coordinates> naturalOrder = Comparator.comparing(Coordinates::y)
																		.thenComparing(Coordinates::x);

		@Override
		public int compareTo(final Coordinates o) {
			return naturalOrder.compare(this, o);
		}

		public Stream<Coordinates> toNeighbours() {
			return IntStream.rangeClosed(x - 1, x + 1)
					   .boxed()
					   .flatMap(x -> IntStream.rangeClosed(y - 1, y + 1)
										 .mapToObj(y -> new Coordinates(x, y))
					   );
		}
	}

	private record DetectedNumber(int beginColumn, int value, int length, Collection<Integer> indices) {
		private static final Pattern pattern = Pattern.compile("\\b\\d+");

		public static List<DetectedNumber> allIn(String input) {
			List<DetectedNumber> numbers = new LinkedList<>();

			Matcher matcher = pattern.matcher(input);
			while (matcher.find()) {
				String val = matcher.group();
				int begin = matcher.start();
				int len = val.length();
				List<Integer> indices = IntStream.range(begin, begin + len)
											.boxed()
											.toList();
				numbers.add(
					new DetectedNumber(
						matcher.start(),
						Integer.parseInt(val),
						len,
						indices
					)
				);
			}
			return numbers;
		}
	}

	private record InputLine(Collection<DetectedNumber> numbers, Collection<Integer> signIndices) {
		public static InputLine from(String line) {
			return new InputLine(
				DetectedNumber.allIn(line),
				IntStream.range(0, line.length())
					.filter(i -> Optional.of(line.charAt(i))
									 .filter(Predicate.not(Character::isDigit))
									 .filter(c -> c != '.')
									 .isPresent())
					.boxed()
					.toList()
			);
		}
	}

	private static Stream<Coordinates> coordinatesForNumber(int y, DetectedNumber number) {
		return number.indices()
				   .stream()
				   .map(x -> new Coordinates(x, y))
				   .flatMap(Coordinates::toNeighbours)
				   .distinct();
	}

	public static void main(String[] args) throws IOException {
		List<InputLine> parsed = Util.streamFile("src/main/resources/23/3.txt")
									 .map(InputLine::from)
									 .collect(Collectors.toCollection(ArrayList::new));
		final int size = parsed.size();

		Collection<Coordinates> allSigns = IntStream.range(0, size).boxed()
											   .flatMap(y -> parsed.get(y).signIndices().stream()
																	.map(x -> new Coordinates(x, y)))
											   .collect(Collectors.toCollection(TreeSet::new));

		final long counter = IntStream.range(0, size).boxed()
			.flatMap(y -> parsed.get(y).numbers.stream()
							  .filter(num -> coordinatesForNumber(y, num).anyMatch(allSigns::contains)))
			.mapToInt(DetectedNumber::value)
			.sum();
		System.out.println("p1: " + counter);
	}
}
