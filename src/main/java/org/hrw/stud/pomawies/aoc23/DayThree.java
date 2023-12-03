package org.hrw.stud.pomawies.aoc23;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.hrw.stud.pomawies.aoc21.Util;

@SuppressWarnings("java:S106")
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

	private record DetectedNumber(int y, int beginColumn, int value, int length, Collection<Integer> indices) {
		private static final Pattern pattern = Pattern.compile("\\b\\d+");

		public Stream<Coordinates> toNeighbours() {
			return indices()
					   .stream()
					   .map(x -> new Coordinates(x, y))
					   .flatMap(Coordinates::toNeighbours);
		}

		public static List<DetectedNumber> allIn(int y, String input) {
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
						y,
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

	private record InputLine(int y, Collection<DetectedNumber> numbers, Collection<Integer> signIndices, String line) {

		public Stream<Coordinates> forChar(char target) {
			return signIndices().stream()
					   .filter(i -> line.charAt(i) == target)
					   .map(x -> new Coordinates(x, y));
		}

		public Stream<Coordinates> allSigns() {
			return signIndices().stream()
					   .map(x -> new Coordinates(x, y));
		}

		public static InputLine from(int y, String line) {
			return new InputLine(
				y,
				DetectedNumber.allIn(y, line),
				IntStream.range(0, line.length())
					.filter(i -> Optional.of(line.charAt(i))
									 .filter(Predicate.not(Character::isDigit))
									 .filter(c -> c != '.')
									 .isPresent())
					.boxed()
					.toList(),
				line
			);
		}
	}

	public static void main(String[] args) throws IOException {
		final var raw = Util.readFile("src/main/resources/23/3.txt");
		final int size = raw.size();

		final var parsed = IntStream.range(0, size)
							   .mapToObj(y -> InputLine.from(y, raw.get(y)))
							   .toList();

		final var allSigns = parsed.stream()
								 .flatMap(InputLine::allSigns)
								 .collect(Collectors.toCollection(TreeSet::new));

		final var allNumbers = parsed.stream()
								   .flatMap(il -> il.numbers()
													  .stream())
								   .collect(Collectors.toSet());

		final long counter = allNumbers.stream()
								 .filter(num -> num.toNeighbours()
													.anyMatch(allSigns::contains))
								 .mapToInt(DetectedNumber::value)
								 .sum();

		System.out.println("p1: " + counter);

		final var allStarSigns = parsed.stream()
									 .flatMap(inputLine -> inputLine.forChar('*'))
									 .collect(Collectors.toCollection(TreeSet::new));

		Map<Coordinates, List<DetectedNumber>> signsWithNeighbors = allStarSigns.stream()
																		.collect(Collectors.toMap(
																			Function.identity(),
																			sc -> allNumbers.stream()
																					  .filter(num ->
																								  num.toNeighbours()
																									  .toList()
																									  .contains(sc))
																					  .toList()
																		));

		final long sumOfGears = signsWithNeighbors.values()
									.stream()
									.filter(l -> l.size() == 2)
									.mapToLong(l -> (long) l.get(0).value() * l.get(1).value())
									.sum();

		System.out.println("p2: " + sumOfGears);
	}
}
