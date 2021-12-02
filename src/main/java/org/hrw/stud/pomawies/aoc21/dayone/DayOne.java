package org.hrw.stud.pomawies.aoc21.dayone;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;
import org.hrw.stud.pomawies.aoc21.Util;
import org.jetbrains.annotations.NotNull;

public class DayOne {
	public static void main(String[] args) throws IOException {
		final List<Integer> inputList = Util.streamFile("src/main/resources/dayOneInput", Integer::parseInt)
			.toList();
		final List<Integer> rollingAverages = IntStream.range(1, inputList.size() - 1)
			.parallel()
			.map(i -> IntStream.rangeClosed(i - 1, i + 1)
				.parallel()
				.map(inputList::get)
				.sum()
			).boxed()
			.toList();
		System.out.printf(
			"Increases in depth for single measurements: %d%n" +
			"Increases in depth for 3-measurement rolling averages: %d%n",
			increasesInList(inputList),
			increasesInList(rollingAverages)
		);
	}

	private static int increasesInList(@NotNull List<@NotNull Integer> list) {
		return IntStream.range(1, list.size())
			.parallel()
			.map(index -> list.get(index) > list.get(index - 1) ? 1 : 0)
			.sum();
	}
}
