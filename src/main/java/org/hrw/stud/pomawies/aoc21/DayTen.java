package org.hrw.stud.pomawies.aoc21;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class DayTen {
	private static final Map<Character, Integer> INVALID_TO_POINTS = Map.of(
		')', 3,
		']', 57,
		'}', 1197,
		'>', 25137
	);

	private static final Map<Character, Integer> COMPLETING_TO_POINTS = Map.of(
		')', 1,
		']', 2,
		'}', 3,
		'>', 4
	);

	private static final BiMap<Character, Character> CLOSING_TO_OPENING;
	private static final BiMap<Character, Character> OPENING_TO_CLOSING;

	static {
		CLOSING_TO_OPENING = HashBiMap.create();
		CLOSING_TO_OPENING.putAll(Map.of(
			')', '(',
			']', '[',
			'}', '{',
			'>', '<'
		));

		OPENING_TO_CLOSING = CLOSING_TO_OPENING.inverse();
	}

	public static void main(String[] args) throws IOException {
		final Map<String, Integer> lineToErrScore = Util.streamFile("src/main/resources/dayTenInput")
			.collect(Collectors.toMap(
				Function.identity(),
				DayTen::errorScore
			));

		final long sumOfSyntaxErrors = lineToErrScore.values()
			.stream()
			.parallel()
			.filter(l -> l != 0)
			.mapToLong(Integer::longValue)
			.sum();

		System.out.println(sumOfSyntaxErrors);

		final List<Long> completionScores = lineToErrScore.entrySet()
			.stream()
			.parallel()
			.filter(entry -> entry.getValue() == 0)
			.map(Map.Entry::getKey)
			.map(DayTen::completionScore)
			.sorted()
			.toList();
		int centerIndex = completionScores.size() / 2;
		long middleScore = completionScores.get(centerIndex);

		System.out.println(middleScore);
	}

	private static long completionScore(String incompleteLine) {
		List<Character> lineChars = incompleteLine.chars()
			.mapToObj(ch -> (char) ch)
			.toList();

		Character first = lineChars.get(0);
		LinkedList<Character> openingCharStack = new LinkedList<>();
		openingCharStack.push(first);

		for (int i = 1; i < lineChars.size(); i++) {
			final Character currentChar = lineChars.get(i);
			if (CLOSING_TO_OPENING.containsKey(currentChar)) {
				openingCharStack.pop();
			} else {
				openingCharStack.push(currentChar);
			}
		}
		long result = 0L;
		for (final Character openingChar : openingCharStack) {
			result *= 5;
			result += COMPLETING_TO_POINTS.get(OPENING_TO_CLOSING.get(openingChar));
		}
		return result;
	}

	private static int errorScore(String line) {
		List<Character> lineChars = line.chars()
			.mapToObj(ch -> (char) ch)
			.toList();

		Character first = lineChars.get(0);
		if (CLOSING_TO_OPENING.containsKey(first)) {
			return INVALID_TO_POINTS.get(first);
		}
		Deque<Character> openingCharStack = new LinkedList<>();
		openingCharStack.push(first);

		for (int i = 1; i < lineChars.size(); i++) {
			final Character currentChar = lineChars.get(i);
			if (CLOSING_TO_OPENING.containsKey(currentChar)) {
				if (!CLOSING_TO_OPENING.get(currentChar)
					.equals(openingCharStack.pop())) {
					return INVALID_TO_POINTS.get(currentChar);
				}
			} else {
				openingCharStack.push(currentChar);
			}
		}
		return 0;
	}
}
