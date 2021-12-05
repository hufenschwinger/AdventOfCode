package org.hrw.stud.pomawies.aoc21;

import static java.util.function.Predicate.not;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.jetbrains.annotations.NotNull;

public final class DayFour {

	public static final int BOARD_DIMENSION = 5;

	public static IntStream rangeStream() {
		return IntStream.range(0, BOARD_DIMENSION);
	}

	public record Position(int row, int column) {
	}

	public static final Set<Position> allPositions =
		rangeStream()
			.mapToObj(row -> rangeStream()
				.mapToObj(col -> new Position(row, col))
			)
			.flatMap(Function.identity())
			.collect(Collectors.toUnmodifiableSet());

	public static class BingoBoard {
		private final int boardNumber;

		int[][] nums = new int[BOARD_DIMENSION][BOARD_DIMENSION];
		Boolean[][] checked = new Boolean[BOARD_DIMENSION][BOARD_DIMENSION];

		public BingoBoard(int boardNumber, @NotNull List<@NotNull String> rows) {
			this.boardNumber = boardNumber;
			if (rows.size() != BOARD_DIMENSION) {
				throw new IllegalArgumentException("Number of rows is not same as Board dimension");
			}
			List<List<Integer>> parsed = rows.stream()
				.map(row -> Arrays.stream(row.split(" "))
					.map(String::trim)
					.filter(not(""::equals))
					.map(Integer::valueOf)
					.toList())
				.toList();

			if (parsed.stream()
				.anyMatch(
					line -> line.size() != BOARD_DIMENSION
				)) {
				throw new IllegalArgumentException("Row of incorrect size");
			}

			allPositions
				.forEach(pos -> nums[pos.row()][pos.column()] = parsed.get(pos.row)
					.get(pos.column()));
			setCheckedConditioned(i -> true, false);
		}

		private int getNumAt(Position pos) {
			return nums[pos.row()][pos.column()];
		}

		private boolean isCheckedAt(Position pos) {
			return checked[pos.row()][pos.column()];
		}

		public void setCheckedConditioned(IntPredicate condition, boolean newValue) {
			allPositions.stream()
				.filter(pos -> condition.test(nums[pos.row()][pos.column()]))
				.forEach(pos -> checked[pos.row()][pos.column()] = newValue);
		}

		public boolean addDrawnNumber(int drawnNumber) {
			setCheckedConditioned(i -> i == drawnNumber, true);
			return checkWin();
		}

		private boolean isRowComplete(int rowIndex) {
			return Arrays.stream(checked[rowIndex])
				.allMatch(isChecked -> isChecked);
		}

		private boolean isColumnComplete(int columnIndex) {
			return rangeStream()
				.mapToObj(row -> checked[row][columnIndex])
				.allMatch(isChecked -> isChecked);
		}

		private boolean checkWin() {
			return rangeStream()
				.anyMatch(index -> isColumnComplete(index) || isRowComplete(index));
		}

		public long sumOfUnmarkedFields() {
			return allPositions.stream()
				.filter(not(this::isCheckedAt))
				.mapToInt(this::getNumAt)
				.mapToLong(i -> i)
				.sum();
		}

		public int getBoardNumber() {
			return boardNumber;
		}

		@Override
		public String toString() {
			return "Board number " + boardNumber + "\n" +
					   Arrays.stream(nums)
						   .map(Arrays::stream)
						   .map(ints -> ints.boxed()
							   .map(i -> String.format("%02d", i))
							   .collect(Collectors.joining(" ")))
						   .collect(Collectors.joining("\n"))
				+"\n";
		}
	}

	public static void main(String[] args) throws IOException {
		List<String> lines = Util.readFile("src/main/resources/dayFourInput");
		List<Integer> drawnNumbers = Arrays.stream(lines.get(0)
				.split(","))
			.map(Integer::valueOf)
			.toList();

		List<String> boardLines = lines.subList(1, lines.size());
		boardLines.removeIf(""::equals);

		if (boardLines.size() % BOARD_DIMENSION != 0) {
			throw new IllegalArgumentException("Number of board lines not multiple of Board size!");
		}

		final int boardCount = boardLines.size() / BOARD_DIMENSION;

		List<BingoBoard> boards = IntStream.range(0, boardCount)
			.mapToObj(ind -> new BingoBoard(
				ind + 1,
				IntStream
					.iterate(ind * BOARD_DIMENSION, i -> i + 1)
					.limit(BOARD_DIMENSION)
					.mapToObj(boardLines::get)
					.toList()
			))
			.toList();

		System.out.println(partOne(drawnNumbers, boards));
		System.out.println(partTwo(drawnNumbers, boards));
	}

	private static String partOne(List<Integer> drawnNumbers, List<BingoBoard> boards) {
		for (int draw : drawnNumbers) {
			Optional<BingoBoard> winningBoard = boards
				.stream()
				.filter(board -> board.addDrawnNumber(draw))
				.findFirst();

			if (winningBoard.isPresent()) {
				return String.format("Winning board %d with winValue %d%n",
					winningBoard.get()
						.getBoardNumber(),
					draw * winningBoard.get()
						.sumOfUnmarkedFields()
				);
			}
		}
		return null;
	}

	private static String partTwo(List<Integer> drawnNumbers, List<BingoBoard> boards) {
		List<BingoBoard> reductionList = new LinkedList<>(boards);

		for(int draw : drawnNumbers) {
			List<BingoBoard> winningBoards = reductionList.stream()
												 .filter(board -> board.addDrawnNumber(draw))
												 .toList();
			if(reductionList.size() == 1 && winningBoards.size() == 1) {
				BingoBoard loserBoard = reductionList.get(0);

				return String.format("Last board to win is board %d with winValue of %d",
					loserBoard.getBoardNumber(),
					draw * loserBoard.sumOfUnmarkedFields());
			}
			reductionList.removeAll(winningBoards);
		}
		return null;
	}
}
