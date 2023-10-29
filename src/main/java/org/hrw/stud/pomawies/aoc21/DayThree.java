package org.hrw.stud.pomawies.aoc21;

import static java.util.function.Predicate.not;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.eclipse.jdt.annotation.NonNull;

public class DayThree {

	public static class BinaryPowerRate {
		private final StringBuilder epsilon = new StringBuilder();
		private final StringBuilder gamma = new StringBuilder();

		public void add(@NonNull Collection<@NonNull Character> column) {
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

	/**
	 * Reduces the list to a sigle line by repeated application of the criterion
	 * @param fullInput all inputs
	 * @param oneZeroDecider compares number of ONEs found to number of ZEROs found in a column and determines the relevant char
	 * @return long-value of last remaining line ({@link Util#binaryToLong(String)})
	 */
	private static long reduceListToLong(@NonNull List<@NonNull String> fullInput, @NonNull BiFunction<Long, Long, Character> oneZeroDecider) {
		final List<String> reductionList = new LinkedList<>(fullInput);
		int index = 0;

		while(reductionList.size() > 1) {
			final int currentIndex = index;
			var charCountsForColumn = reductionList.stream()
				.map(line -> line.charAt(currentIndex))
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

			final char relevantChar = oneZeroDecider.apply(charCountsForColumn.getOrDefault(Util.ONE, 0L), charCountsForColumn.getOrDefault(Util.ZERO, 0L));
			reductionList.removeIf(line -> line.charAt(currentIndex) != relevantChar);

			index++;
		}

		return Util.binaryToLong(reductionList.get(0));
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

		BinaryPowerRate binaryPowerRate = new BinaryPowerRate();

		IntStream.range(0, lineLen)
			.mapToObj(columnIndex -> lines.stream()
				.map(line -> line.charAt(columnIndex))
				.toList())
			.forEach(binaryPowerRate::add);

		final long oxygenGeneratorRating = reduceListToLong(lines, (oneCount, zeroCount) -> oneCount >= zeroCount ? Util.ONE : Util.ZERO);
		final long coTwoScrubberRating = reduceListToLong(lines, (oneCount, zeroCount) -> oneCount < zeroCount ? Util.ONE : Util.ZERO);

		System.out.printf("Power consumption is: %d%n",
			binaryPowerRate.powerConsumption());

		System.out.printf("Oxygen rating %d%nCO2-Scrubber rating %d%nLife support rating %d",
			oxygenGeneratorRating, coTwoScrubberRating, oxygenGeneratorRating * coTwoScrubberRating
		);
	}
}
