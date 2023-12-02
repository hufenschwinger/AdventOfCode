package org.hrw.stud.pomawies.aoc23;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.hrw.stud.pomawies.aoc21.Util;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@SuppressWarnings({"java:S106", "java:S115"})
@NonNullByDefault
public class DayTwo {
	private enum Color {
		red,
		blue,
		green,
		;
		public static final Flowable<Color> colors = Flowable.fromArray(values()).cache();

		public boolean isContained(String input) {
			return input.contains(this.name());
		}
	}

	private record ResultSet(long amount, Color color) {
		public static ResultSet fromGroup(String group) {
			return Color.colors
					   .filter(c -> c.isContained(group))
					   .singleElement()
					   .toSingle()
					   .map(c -> new ResultSet(
						   Long.parseLong(group.trim().split(" ")[0]),
						   c
					   ))
					   .blockingGet();
		}
	}

	private record Game(int id, Collection<ResultSet> items) {
		public static Game fromLine(String line) {
			String[] meta = line.split(":");
			int id = Integer.parseInt(meta[0].split(" ")[1]);
			Collection<ResultSet> resultSets = new LinkedList<>();
			for (String g : meta[1].split(";")) {
				for (String s : g.split(",")) {
					resultSets.add(ResultSet.fromGroup(s));
				}
			}
			return new Game(id, resultSets);
		}

		public boolean allPossible(Map<Color, Long> requiredNumbers) {
			Map<Color, Long> maxAmountByColor = items.stream()
													.collect(Collectors
																 .groupingBy(
																	 ResultSet::color,
																	 Collectors.collectingAndThen(
																		 Collectors.mapping(
																			 ResultSet::amount,
																			 Collectors.maxBy(Comparator.naturalOrder())
																		 ),
																		 o -> o.orElse(0L)
																	 )
																 ));


			return requiredNumbers.entrySet()
					   .stream()
					   .allMatch(e -> maxAmountByColor.getOrDefault(e.getKey(), 0L) <= e.getValue());

		}
	}

	public static void main(String[] args) throws IOException {
		var partOne = Map.of(Color.red, 12L, Color.green, 13L, Color.blue, 14L);

		Util.getLines("src/main/resources/23/2.txt")
			.subscribeOn(Schedulers.computation())
			.map(Game::fromLine)
			.filter(g -> g.allPossible(partOne))
			.map(Game::id)
			.reduce(0, Integer::sum)
			.doOnSuccess(System.out::println)
			.blockingGet();
	}
}
