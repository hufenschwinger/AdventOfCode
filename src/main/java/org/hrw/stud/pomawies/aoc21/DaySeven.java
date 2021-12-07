package org.hrw.stud.pomawies.aoc21;

import java.io.IOException;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DaySeven {

	public static void main(String[] args) throws IOException {
		List<Integer> startingPositions = Util.streamFile("src/main/resources/daySevenInput")
			.map(line -> line.split(","))
			.flatMap(Arrays::stream)
			.map(Integer::valueOf)
			.toList();

		final IntSummaryStatistics statistics = startingPositions.stream()
			.collect(Collectors.summarizingInt(Integer::intValue));

		final int totalFuelSpentLinear = IntStream.rangeClosed(statistics.getMin(), statistics.getMax())
			.parallel()
			.map(possibleDest -> startingPositions
				.parallelStream()
				.mapToInt(Integer::intValue)
				.map(currentPos -> Math.abs(currentPos - possibleDest))
				.sum())
			.min()
			.getAsInt();

		System.out.println(totalFuelSpentLinear);

		final int totalFuelSpentCrabby = IntStream.rangeClosed(statistics.getMin(), statistics.getMax())
			.parallel()
			.map(possibleDest -> startingPositions
				.parallelStream()
				.mapToInt(Integer::intValue)
				.map(currentPos -> moveCosts(currentPos, possibleDest))
				.sum())
			.min()
			.getAsInt();
		System.out.println(totalFuelSpentCrabby);
	}

	private static int moveCosts(int start, int dest) {
		return IntStream.rangeClosed(1, Math.abs(start - dest))
				   .sum();
	}
}
