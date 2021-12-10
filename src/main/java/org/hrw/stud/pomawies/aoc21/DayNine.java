package org.hrw.stud.pomawies.aoc21;

import static java.util.function.Predicate.not;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DayNine {

	public record Point(int x, int y) {
		public Point adjustDownRight() {
			return new Point(x + 1, y + 1);
		}

		public Stream<Point> neighbours() {
			return Stream.of(
				new Point(x + 1, y),
				new Point(x - 1, y),
				new Point(x, y + 1),
				new Point(x, y - 1)
			);
		}
	}

	public static class Basin {
		private final int[][] arr;
		private final Point lowPoint;
		private final Set<Point> pointsInBasin;

		public Basin(final int[][] arr, final Point lowPoint) {
			this.arr = arr;
			this.lowPoint = lowPoint;
			pointsInBasin = expand();
		}

		public int size() {
			return pointsInBasin.size();
		}

		private Set<Point> expand() {
			Set<Point> basinPoints = new HashSet<>();
			basinPoints.add(lowPoint);
			while (true) {
				Set<Point> addedPoints = basinPoints.stream()
					.flatMap(Point::neighbours)
					.filter(not(basinPoints::contains))
					.filter(point -> readAt(point) != 9)
					.collect(Collectors.toSet());
				if (addedPoints.isEmpty()) {
					break;
				}
				basinPoints.addAll(addedPoints);
			}
			return basinPoints;
		}

		private int readAt(Point point) {
			return arr[point.x()][point.y()];
		}
	}

	public static void main(String[] args) throws IOException {
		final List<String> input = Util.readFile("src/main/resources/dayNineInput");

		int[][] arr = linesTo2DArray(input);

		final int rowCount = arr.length;
		final int colCount = arr[0].length;

		long dangerLevelSum = 0L;

		Set<Point> lowPoints = new HashSet<>();

		for (int row = 0; row < rowCount; row++) {
			for (int col = 0; col < colCount; col++) {
				int val = arr[row][col];
				if ((row == 0			 || val < arr[row - 1][col])   //up
				 && (row == rowCount - 1 || val < arr[row + 1][col])   //down
				 && (col == 0			 || val < arr[row][col - 1])   //left
				 && (col == colCount - 1 || val < arr[row][col + 1])) {//right
				 	lowPoints.add(new Point(row, col));
					dangerLevelSum += val + 1;
				}
			}
		}
		System.out.println(dangerLevelSum);

		final List<String> paddedLines = new ArrayList<>(rowCount + 2);
		paddedLines.add("9".repeat(colCount + 2));
		input.forEach(line -> paddedLines.add("9" + line + "9"));
		paddedLines.add("9".repeat(colCount + 2));

		final int[][] paddedArr = linesTo2DArray(paddedLines);

		final var productOfLargestThreeBasinSizes = lowPoints.parallelStream()
			.map(Point::adjustDownRight)
			.map(lowPoint -> new Basin(paddedArr, lowPoint))
			.map(Basin::size)
			.sorted((a, b) -> Integer.compare(b, a))
			.limit(3)
			.reduce(1, (a, b) -> a * b);

		System.out.println(productOfLargestThreeBasinSizes);
	}

	private static int[][] linesTo2DArray(List<String> lines) {
		return lines.parallelStream()
			.map(line -> line.chars()
				.mapToObj(ch -> String.valueOf((char) ch))
				.mapToInt(Integer::parseInt)
				.toArray())
			.toArray(int[][]::new);
	}
}
