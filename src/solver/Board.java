package solver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class Board {

	public static void main(String[] args) throws InterruptedException {
		Board b = new Board(6);
		b.setDemoStartingPieces();
		b.calculateBoardValues();
		System.out.println("\n\t http://www.binarypuzzle.com/puzzles.php?size=6&level=4&nr=1 \n");

		Thread.sleep(10000);

		b = new Board(6);
		b.setCustomPieces(1, 0, 0).setCustomPieces(0, 2, 1).setCustomPieces(0, 3, 0).setCustomPieces(4, 3, 0)
				.setCustomPieces(0, 4, 1).setCustomPieces(3, 4, 0).setCustomPieces(1, 5, 0).setCustomPieces(5, 5, 0);
		b.calculateBoardValues();
		System.out.println("\n\t http://www.binarypuzzle.com/puzzles.php?size=6&level=4&nr=2 \n");
	}

	private final int size;
	private final int[][] boardValues; // Holds each individual board value
	private final List<int[]> allRowPossibilities_GivenSize; //	All possibilities that are allowed, given the size and the row rules above
	private final Map<Integer, List<int[]>> allPossibleRowsPerBoardRow_GivenBoardValues; // All possibilities per board row, given the starting board values

	private boolean solutionFound = false;

	public Board(int size) {
		this.size = size;
		boardValues = createBoard();
		allPossibleRowsPerBoardRow_GivenBoardValues = new HashMap<>();
		allRowPossibilities_GivenSize = new RowMaker(size).getAllRowPossibilities();
	}

	public Board setCustomPieces(int x, int y, int value) {
		if (value < 0 || value > 1) {
			throw new RuntimeException();
		}
		boardValues[y][x] = value;
		return this;
	}

	public void setDemoStartingPieces() {
		boardValues[0][2] = 1;
		boardValues[1][0] = 0;
		boardValues[1][1] = 0;
		boardValues[1][3] = 1;
		boardValues[2][0] = 0;
		boardValues[4][3] = 1;
		boardValues[5][4] = 0;
	}

	public void calculateBoardValues() {
		findPossibleRowsPerBoardRow_GivenStartingPieces();
		// printAllMatchingRows(); // Prints this hashmap allPossibleRowsPerBoardRow_GivenBoardValues

		int allPossibilities = 1;
		for (int i = 0; i < size; i++) {
			allPossibilities *= allPossibleRowsPerBoardRow_GivenBoardValues.get(i).size();
		}
		System.out.println("Amount of possible board configurations to be checked: " + allPossibilities + "\n");
		perfectBoardSolutions(allPossibleRowsPerBoardRow_GivenBoardValues, 0);
	}

	private void perfectBoardSolutions(Map<Integer, List<int[]>> lists, int mapIndex) {
		if (mapIndex == lists.size()) {
			if (criteriaMet_Vertically()) {
				printBoard();
				solutionFound = true;
			}
			return;
		}
		for (int i = 0; i < lists.get(mapIndex).size(); i++) {
			boardValues[mapIndex] = lists.get(mapIndex).get(i);
			perfectBoardSolutions(lists, mapIndex + 1);
			if (solutionFound) {
				return;
			}
		}
	}

	private int[][] createBoard() {
		int[][] board = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				board[i][j] = -1; // empty spot
			}
		}
		return board;
	}

	private void findPossibleRowsPerBoardRow_GivenStartingPieces() {
		for (int i = 0; i < size; i++) {
			List<int[]> listOfMatchingRows = new LinkedList<>();
			for (int[] array : allRowPossibilities_GivenSize) {
				boolean isMatch = true;
				for (int j = 0; j < size; j++) {
					if (boardValues[i][j] != -1) {
						if (boardValues[i][j] != array[j]) {
							isMatch = false;
							break;
						}
					}
				}
				if (isMatch) {
					listOfMatchingRows.add(array);
				}
			}
			allPossibleRowsPerBoardRow_GivenBoardValues.put(i, listOfMatchingRows);
		}
	}

	private boolean criteriaMet_Vertically() {
		int[] verticalRow = new int[size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				verticalRow[j] = boardValues[j][i];
			}
			if (!RowMaker.isRowValid(verticalRow)) {
				return false;
			}
		}
		if (!isRowUnique()) {
			return false;
		}
		return true;
	}

	private boolean isRowUnique() {
		// Horizontal
		int[] tempBoardRow = new int[size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				tempBoardRow[j] = boardValues[i][j];
			}
			for (int k = i + 1; k < size; k++) {
				if (Arrays.equals(boardValues[k], tempBoardRow)) {
					return false;
				}
			}
		}
		// Vertical
		for (int i = 0; i < size; i++) {
			int[] tempBoardRowVertical = new int[size];
			for (int j = 0; j < size; j++) {
				tempBoardRow[j] = boardValues[j][i];
			}
			for (int k = i + 1; k < size; k++) {
				for (int l = 0; l < size; l++) {
					tempBoardRowVertical[l] = boardValues[l][k];
				}
				if (Arrays.equals(tempBoardRowVertical, tempBoardRow)) {
					return true;
				}
			}
		}
		return true;
	}

	private void printAllMatchingRows() {
		System.out.println("Given the starting values, these possibilities match each row:");
		for (Map.Entry<Integer, List<int[]>> entryListOfArrays : allPossibleRowsPerBoardRow_GivenBoardValues
				.entrySet()) {
			System.out.println("Row: " + entryListOfArrays.getKey());
			RowMaker.printList(entryListOfArrays.getValue());
			System.out.println();
		}
	}

	private void printBoard() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				System.out.print(boardValues[i][j] + " ");
			}
			System.out.println();
		}
	}

}
