package org.hrw.stud.pomawies.aoc21.daytwo;

import java.io.IOException;
import org.hrw.stud.pomawies.aoc21.Util;

public class DayTwo {

	public record Input(String mode, long amount) {
		public Input(String[] line) {
			this(line[0], Long.parseLong(line[1]));
		}
	}

	public static class SubCoordinatesTypeA {
		protected long depth = 0L;
		protected long distance = 0L;

		public SubCoordinatesTypeA() { }

		public void parseInput(Input input) {
			switch (input.mode()) {
				case "forward" -> distance += input.amount();
				case "down" -> depth += input.amount();
				case "up" -> depth -= input.amount();
				default -> throw new IllegalArgumentException("Unknown mode: " + input.mode());
			}
		}

		public long depthTimesDistance() {
			return depth * distance;
		}
	}

	public static class SubCoordinatesTypeB extends SubCoordinatesTypeA {
		protected long aim = 0L;

		public SubCoordinatesTypeB() { }

		@Override
		public void parseInput(Input input) {
			switch (input.mode()) {
				case "forward" -> {
					distance += input.amount();
					depth += aim * input.amount();
				}
				case "down" -> aim += input.amount();
				case "up" -> aim -= input.amount();
				default -> throw new IllegalArgumentException("Unknown mode: " + input.mode());
			}
		}
	}

	public static void main(String[] args) throws IOException {
		final var inputs = Util.streamFile(
				"src/main/resources/dayTwoInput",
				line -> line.split(" ")
			)
			.map(Input::new)
			.toList();

		final SubCoordinatesTypeA subCoordinatesTypeA = new SubCoordinatesTypeA();
		inputs.forEach(subCoordinatesTypeA::parseInput);
		System.out.printf("Product of depth and distance after parsing by Mode A: %d%n", subCoordinatesTypeA.depthTimesDistance());

		final SubCoordinatesTypeB subCoordinatesTypeB = new SubCoordinatesTypeB();
		inputs.forEach(subCoordinatesTypeB::parseInput);
		System.out.printf("Product of depth and distance after parsing by Mode B: %d%n", subCoordinatesTypeB.depthTimesDistance());
	}
}
