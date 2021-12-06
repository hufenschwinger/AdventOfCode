package org.hrw.stud.pomawies.aoc21;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DayFive {
	public static final String START_END_SEPARATOR = " -> ";
	public static final String COORDINATE_SEPARATOR = ",";

	public record Point(int x, int y) {
		public boolean isLeftOf(Point other) {
			return this.x() < other.x();
		}

		public boolean isUnderneath(Point other) {
			return this.y() < other.y();
		}

		public static Point parseCoordinates(String coords) {
			String[] components = coords.split(COORDINATE_SEPARATOR);
			return new Point(Integer.parseInt(components[0]), Integer.parseInt(components[1]));

		}

		@Override
		public boolean equals(final Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			final Point point = (Point) o;
			return x == point.x && y == point.y;
		}

		@Override
		public int hashCode() {
			return Objects.hash(x, y);
		}
	}

	public record Line(Point start, Point end) {
		public static Line parseLine(String inputLine) {
			String[] ends = inputLine.split(START_END_SEPARATOR);
			return new Line(Point.parseCoordinates(ends[0]), Point.parseCoordinates(ends[1]));
		}

		public List<Point> pointsInLine() {
			if (isVertical()) {
				return IntStream.rangeClosed(Math.min(start.y(), end.y()), Math.max(start.y(), end.y()))
					.mapToObj(y -> new Point(start.x(), y))
					.toList();
			}
			if (isHorizontal()) {
				return IntStream.rangeClosed(Math.min(start.x(), end.x()), Math.max(start.x(), end.x()))
					.mapToObj(x -> new Point(x, start.y()))
					.toList();
			}
			IntBinaryOperator xShifter = start.isLeftOf(end) ? (x, n) -> x + n : (x, n) -> x - n;
			IntBinaryOperator yShifter = start.isUnderneath(end) ? (y, n) -> y + n : (y, n) -> y - n;

			BiFunction<Point, Integer, Point> pointMapper = (point, shift) ->
																new Point(
																	xShifter.applyAsInt(point.x(), shift),
																	yShifter.applyAsInt(point.y(), shift)
																);

			return IntStream.rangeClosed(0, Math.abs(start.x() - end.x()))
				.mapToObj(shift -> pointMapper.apply(start, shift))
				.toList();
		}

		public boolean isHorizontal() {
			return start.y() == end.y();
		}

		public boolean isVertical() {
			return start.x() == end.x();
		}

		public boolean isStraight() {
			return isHorizontal() || isVertical();
		}
	}

	public static void main(String[] args) throws IOException {
		final var allLines = Util.streamFile("src/main/resources/dayFiveInput", Line::parseLine)
			.toList();

		final long straightLinePointsMetTwiceOrMore = getPointsWithOverlap(allLines.stream()
			.filter(Line::isStraight));

		final long anyPointsMetTwiceOrMore = getPointsWithOverlap(allLines.stream());

		System.out.printf("%d points on straight lines are met at least twice%n", straightLinePointsMetTwiceOrMore);
		System.out.printf("%d points on all lines are met at least twice%n", anyPointsMetTwiceOrMore);
	}

	public static long getPointsWithOverlap(Stream<Line> lineStream) {
		return lineStream
			.map(Line::pointsInLine)
			.flatMap(Collection::stream)
			.collect(Collectors.groupingBy(
					Function.identity(),
					Collectors.counting()
				)
			)
			.entrySet()
			.stream()
			.filter(entry -> entry.getValue() > 1L)
			.count();
	}
}
