package org.hrw.stud.pomawies.aoc21;

import static java.util.function.Predicate.not;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class DayEight {

	public static void main(String[] args) throws IOException {
		final var lines = Util.readFile("src/main/resources/dayEightInput");

		final var sides = lines.parallelStream()
			.map(line -> line.split(" \\| "))
			.toList();

		final var amountOf1478onRightSide = sides.parallelStream()
			.map(parts -> parts[1])
			.map(rightSide -> rightSide.split(" "))
			.flatMap(Arrays::stream)
			.parallel()
			.mapToInt(String::length)
			.filter(num -> num < 5 || num == 7)
			.count();
		System.out.println(amountOf1478onRightSide);

		final var sumOfRightSides = sides.parallelStream()
			.mapToLong(DayEight::lineToValue)
			.sum();

		System.out.println(sumOfRightSides);
	}

	private static int lineToValue(String[] line) {
		final BiMap<Set<Character>, Integer> setToDigit = HashBiMap.create();
		final BiMap<Integer, Set<Character>> digitToSet = setToDigit.inverse();

		final var leftSide = Arrays.stream(line[0].split(" "))
			.map(inp -> inp.chars()
				.mapToObj(ch -> (char) ch)
				.collect(Collectors.toUnmodifiableSet()))
			.collect(Collectors.toUnmodifiableSet());

		final var rightSide = Arrays.stream(line[1].split(" "))
			.map(inp -> inp.chars()
				.mapToObj(ch -> (char) ch)
				.collect(Collectors.toUnmodifiableSet()))
			.toList();

		leftSide.stream()
			.filter(set -> set.size() == 2)
			.findAny()
			.ifPresent(oneSet -> setToDigit.put(oneSet, 1));

		leftSide.stream()
			.filter(set -> set.size() == 3)
			.findAny()
			.ifPresent(sevenSet -> setToDigit.put(sevenSet, 7));

		leftSide.stream()
			.filter(set -> set.size() == 4)
			.findAny()
			.ifPresent(fourSet -> setToDigit.put(fourSet, 4));

		leftSide.stream()
			.filter(set -> set.size() == 7)
			.findAny()
			.ifPresent(eightSet -> setToDigit.put(eightSet, 8));

		final var ambiguousSetsBySize = leftSide.stream()
			.filter(set -> !Set.of(2, 3, 4, 7)
				.contains(set.size()))
			.collect(Collectors.groupingBy(Set::size));
		//1 4 7 8 done
		ambiguousSetsBySize.get(6) //0 6 9
			.forEach(set -> {
				if (setOverlap(set, digitToSet.get(1)) == 1) {
					setToDigit.put(set, 6);
				} else if (setOverlap(set, digitToSet.get(4)) == 4) {
					setToDigit.put(set, 9);
				} else {
					setToDigit.put(set, 0);
				}
			});

		//0 1 4 6 7 8 9 done
		ambiguousSetsBySize.get(5) //2 3 5
			.forEach(set -> {
				if(setOverlap(set, digitToSet.get(1)) == 2) {
					setToDigit.put(set, 3);
				} else if (setOverlap(set, digitToSet.get(4)) == 2) {
					setToDigit.put(set, 2);
				} else {
					setToDigit.put(set, 5);
				}
			}
		);

		//all done
		StringBuilder sb = new StringBuilder();
		rightSide.forEach(rightSet -> sb.append(setToDigit.get(rightSet)));
		return Integer.parseInt(sb.toString());
	}

	private static <T> int setOverlap(Set<T> a, Set<T> b) {
		Set<T> start = new HashSet<>(a);
		start.removeIf(not(b::contains));
		return start.size();
	}

/*
 aaaa
b    c
b    c
 dddd
e    f
e    f
 gggg
*/
}
