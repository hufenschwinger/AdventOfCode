package org.hrw.stud.pomawies.aoc23;

import java.io.IOException;
import java.util.Comparator;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.hrw.stud.pomawies.aoc21.Util;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

@NonNullByDefault
public class DayOne {
	private enum DIGITS {
		ONE,
		TWO,
		THREE,
		FOUR,
		FIVE,
		SIX,
		SEVEN,
		EIGHT,
		NINE,
		;
		private static Flowable<DIGITS> entries = Flowable.fromArray(values()).cache();
		private final String lcn = name().toLowerCase();
		private final String num = String.valueOf(ordinal() + 1);

		public static String replaceAll(String input) {
			String result = input;
			while (true) {
				final String copy = result;
				final String replaced = entries
											.flatMapMaybe(d -> Maybe.just(copy.indexOf(d.lcn))
															  .filter(i -> i >= 0)
															  .map(i -> Pair.of(d, i))
											)
											.sorted(Comparator.comparing(Pair::getRight))
											.map(Pair::getLeft)
											.firstElement()
											.map(d -> copy.replaceAll(d.lcn, d.num))
											.blockingGet();
				if (replaced == null) {
					break;
				}
				result = replaced;
			}
			return result;
		}
	}

	public static void main(String[] args) throws IOException {
		Flowable.just(
				"two1nine",
				"eightwothree",
				"abcone2threexyz",
				"xtwone3four",
				"4nineeightseven2",
				"zoneight234",
				"7pqrstsixteen"
			)
			.doOnNext(System.out::println)
			.map(DIGITS::replaceAll)
			.doOnNext(System.out::println)
			.map(l -> l.replaceAll("\\D", ""))
			.doOnNext(System.out::println)
			.map(l -> l.charAt(0) + "" + l.charAt(l.length() - 1))
			.doOnNext(System.out::println)
			.map(d -> Integer.valueOf(d.charAt(0) + "" + d.charAt(d.length() - 1)))
			.reduce(Integer::sum)
			.doOnSuccess(System.out::println)
			.blockingGet();


		System.out.println(
			Util.getLines("src/main/resources/23/1.txt")
			.map(l -> l.replaceAll("\\D", ""))
			.map(d -> Integer.valueOf(d.charAt(0) + "" + d.charAt(d.length()-1)))
			.reduce(Integer::sum)
			.blockingGet()
		);

		System.out.println(
			Util.getLines("src/main/resources/23/1.txt")
				.map(DIGITS::replaceAll)
				.map(l -> l.replaceAll("\\D", ""))
				.map(d -> Integer.valueOf(d.charAt(0) + "" + d.charAt(d.length()-1)))
				.reduce(Integer::sum)
				.blockingGet()
		);
	}
}
