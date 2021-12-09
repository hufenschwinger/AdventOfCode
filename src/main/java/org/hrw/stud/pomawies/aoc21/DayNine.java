package org.hrw.stud.pomawies.aoc21;

import java.io.IOException;

public class DayNine {

	public static void main(String[] args) throws IOException {
		int[][] arr = Util.streamFile("src/main/resources/dayNineInput")
			.map(line -> line.chars()
				.mapToObj(ch -> String.valueOf((char) ch))
				.mapToInt(Integer::parseInt)
				.toArray())
			.toArray(int[][]::new);

		final int rowCount = arr.length;
		final int colCount = arr[0].length;

		long dangerLevelSum = 0L;

		for(int row = 0; row < rowCount; row++) {
			for(int col = 0; col < colCount; col++) {
				int val = arr[row][col];
				if ((row == 0			 || val < arr[row - 1][col])   //up
				 && (row == rowCount - 1 || val < arr[row + 1][col])   //down
				 && (col == 0			 || val < arr[row][col - 1])   //left
				 && (col == colCount - 1 || val < arr[row][col + 1])) {//right
				 	dangerLevelSum += val + 1;
				}
			}
		}
		System.out.println(dangerLevelSum);
	}
}
