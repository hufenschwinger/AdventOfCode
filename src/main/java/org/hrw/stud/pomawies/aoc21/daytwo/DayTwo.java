package org.hrw.stud.pomawies.aoc21.daytwo;

import java.io.IOException;
import org.hrw.stud.pomawies.aoc21.Util;

public class DayTwo {

	public enum MODE {
		FORWARD,
		UP,
		DOWN;

		public static MODE map(String mode) {
			return switch (mode) {
				case "forward" -> FORWARD;
				case "down" -> DOWN;
				case "up" -> UP;
				default -> throw new IllegalArgumentException("Unknown mode: " + mode);
			};
		}
	}

	public record Input(MODE mode, long amount) {
		public Input(String[] line) {
			this(MODE.map(line[0]), Long.parseLong(line[1]));
		}
	}

	public static class SubCoordinatesTypeA {
		protected long depth = 0L;
		protected long distance = 0L;

		public SubCoordinatesTypeA() { }

		public void parseInput(Input input) {
			switch (input.mode()) {
				case FORWARD -> distance += input.amount();
				case DOWN -> depth += input.amount();
				case UP -> depth -= input.amount();
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
				case FORWARD -> {
					distance += input.amount();
					depth += aim * input.amount();
				}
				case DOWN -> aim += input.amount();
				case UP -> aim -= input.amount();
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
		System.out.printf("Product of depth and distance after parsing by MODE A: %d%n", subCoordinatesTypeA.depthTimesDistance());

		final SubCoordinatesTypeB subCoordinatesTypeB = new SubCoordinatesTypeB();
		inputs.forEach(subCoordinatesTypeB::parseInput);
		System.out.printf("Product of depth and distance after parsing by MODE B: %d%n", subCoordinatesTypeB.depthTimesDistance());
	}
}
