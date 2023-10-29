package org.hrw.stud.pomawies.aoc21;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.eclipse.jdt.annotation.NonNull;

public class DayOne {
	public static void main(String[] args) throws IOException {
		final List<Integer> inputList = Util.streamFile("src/main/resources/dayOneInput", Integer::parseInt)
			.collect(Collectors.toCollection(ArrayList::new));
		final List<Integer> rollingAverages = rollingAverage(inputList, 3);
		System.out.printf(
			"Increases in depth for single measurements: %d%n" +
			"Increases in depth for 3-measurement rolling averages: %d%n",
			increasesInList(inputList),
			increasesInList(rollingAverages)
		);
	}

	private static List<Integer> rollingAverage(@NonNull List <@NonNull Integer> integers, int size) {
		return IntStream.rangeClosed(0, integers.size() - size)
			.parallel()
			.map(i -> IntStream.range(i, i + size)
				.map(integers::get)
				.parallel()
				.sum()
			).boxed()
			.toList();
	}

	private static int increasesInList(@NonNull List<@NonNull Integer> list) {
		return IntStream.range(1, list.size())
			.parallel()
			.map(index -> list.get(index) > list.get(index - 1) ? 1 : 0)
			.sum();
	}
}
