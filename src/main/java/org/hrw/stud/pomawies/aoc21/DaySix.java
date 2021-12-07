package org.hrw.stud.pomawies.aoc21;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class DaySix {

	public static class LanternFish {
		private static final long MAX_COUNTER = 6;
		private static final long NEWBORN_ADDITIONAL_COUNTER = 2;
		private long counter;

		public LanternFish(long counter) {
			this.counter = counter;
		}

		public Stream<LanternFish> age() {
			if (counter != 0) {
				counter--;
				return Stream.of(this);
			} else {
				counter = MAX_COUNTER;
				return Stream.of(this, new LanternFish(MAX_COUNTER + NEWBORN_ADDITIONAL_COUNTER));
			}
		}
	}

	public static void main(String[] args) throws IOException {
		List<Long> startVals = Util.streamFile("src/main/resources/daySixInput")
			.map(line -> line.split(","))
			.flatMap(Arrays::stream)
			.map(Long::valueOf)
			.toList();
		final int days = 80;

		System.out.printf("%d fish after %d days%n", fishAfterDays(startVals, days), days);
	}

	private static long fishAfterDays(Collection<Long> startVals, int iterationDays) {
		Stream<LanternFish> fishStream = startVals.stream()
			.map(LanternFish::new);
		for (int i = 0; i < iterationDays; i++) {
			fishStream = fishStream.flatMap(LanternFish::age);
		}
		return fishStream.count();
	}
}
