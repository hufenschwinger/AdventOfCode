package org.hrw.stud.pomawies.aoc21.daythree;

import static java.util.function.Predicate.not;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.hrw.stud.pomawies.aoc21.Util;
import org.jetbrains.annotations.NotNull;

public class DayThree {

	public static class BinaryRate {
		private final StringBuilder epsilon = new StringBuilder();
		private final StringBuilder gamma = new StringBuilder();

		public void add(@NotNull Collection<@NotNull Character> column) {
			final var charCounts = column.stream()
								 .collect(Collectors.groupingBy(
									 Function.identity(),
									 Collectors.counting()
								 ));

			final int comparison = Long.compare(charCounts.get(Util.ZERO), charCounts.get(Util.ONE));
			if(comparison == 0) {
				throw new IllegalArgumentException("Equal count for ones and zeros!");
			}

			if(comparison < 0) { //more ZEROs
				gamma.append(Util.ZERO);
				epsilon.append(Util.ONE);
			} else { //more ONEs
				gamma.append(Util.ONE);
				epsilon.append(Util.ZERO);
			}

		}

		public long powerConsumption() {
			return Util.binaryToLong(epsilon.toString()) * Util.binaryToLong(gamma.toString());
		}
	}

	public static void main(String[] args) throws IOException {
		final List<String> lines = Util.readFile("src/main/resources/dayThreeInput");
		final int lineLen = lines.get(0).length();
		if( lines.stream()
				.parallel()
				.anyMatch(line -> line.length() != lineLen)
				||
				lines.stream()
					.parallel()
					.flatMapToInt(String::chars)
					.mapToObj(cp -> (char) cp)
					.anyMatch(not(Set.of(Util.ZERO, Util.ONE)::contains))) {
			throw new IllegalArgumentException("Inconsistent input!");
		}

		BinaryRate binaryRate = new BinaryRate();

		IntStream.range(0, lineLen)
			.mapToObj(columnIndex -> lines.stream()
				.map(line -> line.charAt(columnIndex))
				.toList())
			.forEach(binaryRate::add);

		System.out.printf("Power consumption is: %d%n",
			binaryRate.powerConsumption());

	}

}
