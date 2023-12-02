package org.hrw.stud.pomawies.aoc23;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.hrw.stud.pomawies.aoc21.Util;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@SuppressWarnings("java:S106")
@NonNullByDefault
public class DayOne {
	private static final Map<String, Integer> mappings = Map.ofEntries(
		new ImmutablePair<>("1", 1),
		new ImmutablePair<>("2", 2),
		new ImmutablePair<>("3", 3),
		new ImmutablePair<>("4", 4),
		new ImmutablePair<>("5", 5),
		new ImmutablePair<>("6", 6),
		new ImmutablePair<>("7", 7),
		new ImmutablePair<>("8", 8),
		new ImmutablePair<>("9", 9),
		new ImmutablePair<>("one", 1),
		new ImmutablePair<>("two", 2),
		new ImmutablePair<>("three", 3),
		new ImmutablePair<>("four", 4),
		new ImmutablePair<>("five", 5),
		new ImmutablePair<>("six", 6),
		new ImmutablePair<>("seven", 7),
		new ImmutablePair<>("eight", 8),
		new ImmutablePair<>("nine", 9)
	);

	private static Flowable<Integer> allIndices(String input, String target) {
		return Flowable.create(
			emitter -> {
				int index = input.indexOf(target);
				while (index != -1) {
					emitter.onNext(index);
					index = input.indexOf(target, index + 1);
				}
				emitter.onComplete();
			},
			BackpressureStrategy.BUFFER
		);
	}

	public static int firstPlusLast(String input) {
		Flowable<Integer> digits = Flowable.fromIterable(mappings.entrySet())
									   .flatMap(e -> allIndices(input, e.getKey()).map(index -> Pair.of(index, e.getValue())))
									   .sorted(Comparator.comparing(Pair::getLeft))
									   .map(Pair::getRight)
									   .cache();
		return Single.merge(
				digits.firstElement()
					.map(i -> 10 * i)
					.toSingle(),
				digits.lastElement()
					.toSingle()
			)
				   .reduce(0, Integer::sum)
				   .blockingGet();
	}

	public static void main(String[] args) throws IOException {
		System.out.println(
			Util.getLines("src/main/resources/23/1.txt")
				.map(l -> l.replaceAll("\\D", ""))
				.map(d -> Integer.valueOf(d.charAt(0) + "" + d.charAt(d.length() - 1)))
				.reduce(Integer::sum)
				.blockingGet()
		);

		System.out.println(
			Util.getLines("src/main/resources/23/1.txt")
				.map(DayOne::firstPlusLast)
				.reduce(Integer::sum)
				.blockingGet()
		);
	}
}
