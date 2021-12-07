package org.hrw.stud.pomawies.aoc21;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class DaySix {
	private static final int MAX_COUNTER = 6;
	private static final int NEWBORN_COUNTER = 8;


	public static void main(String[] args) throws IOException {
		List<Integer> startVals = Util.streamFile("src/main/resources/daySixInput")
			.map(line -> line.split(","))
			.flatMap(Arrays::stream)
			.map(Integer::valueOf)
			.toList();
		final int partOneDays = 80;
		final int partTwoDays = 256;

		System.out.printf("%d fish after %d days%n", fishAfterDays(startVals, partOneDays), partOneDays);
		System.out.printf("%d fish after %d days%n", fishAfterDays(startVals, partTwoDays), partTwoDays);
	}

	private static long fishAfterDays(Collection<Integer> startVals, int iterationDays) {
		long[] nums = new long[NEWBORN_COUNTER + 1];
		for (Integer startVal : startVals) {
			nums[startVal]++;
		}
		for(int i = 0; i < iterationDays; i++) {
			long fertile = nums[0];

			System.arraycopy(nums, 1, nums, 0, nums.length - 1);
			nums[MAX_COUNTER] += fertile;
			nums[NEWBORN_COUNTER] = fertile;
		}

		return Arrays.stream(nums)
				   .sum();
	}
}
