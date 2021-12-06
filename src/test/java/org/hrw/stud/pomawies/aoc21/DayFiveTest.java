package org.hrw.stud.pomawies.aoc21;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.util.List;
import org.junit.jupiter.api.Test;

class DayFiveTest {

	private static final DayFive.Point bottomLeft = new DayFive.Point(1, 1);
	private static final DayFive.Point topLeft = new DayFive.Point(1, 3);
	private static final DayFive.Point bottomRight = new DayFive.Point(3, 1);
	private static final DayFive.Point topRight = new DayFive.Point(3, 3);
	private static final DayFive.Point centre = new DayFive.Point(2,2);

	private static final DayFive.Line downRight = new DayFive.Line(topLeft, bottomRight);
	private static final DayFive.Line upRight = new DayFive.Line(bottomLeft, topRight);
	private static final DayFive.Line downLeft = new DayFive.Line(topRight, bottomLeft);
	private static final DayFive.Line upLeft = new DayFive.Line(bottomRight, topLeft);
	@Test
	void diagonalWorks() {
		assertDiagonally(upLeft);
		assertDiagonally(downLeft);
		assertDiagonally(upRight);
		assertDiagonally(downRight);
	}

	static void assertDiagonally(DayFive.Line line) {
		assertThat(line.pointsInLine()).isEqualTo(List.of(line.start(), centre, line.end()));
	}

}