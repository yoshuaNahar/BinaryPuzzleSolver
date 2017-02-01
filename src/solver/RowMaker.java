package solver;

import java.util.ArrayList;
import java.util.List;

public final class RowMaker {

	public static void main(String[] args) {
		RowMaker rowCalculator = new RowMaker(6);
		rowCalculator.printAllBinaryNumbers();
		System.out.println();
		rowCalculator.printAllRowPossibilities();
	}

	private final int length;
	private final List<int[]> binaryNumbers = new ArrayList<>();
	private final List<int[]> allRowPossibilities = new ArrayList<>();

	public RowMaker(int rowLength) {
		this.length = rowLength;
		calculateAllRowPossibilities();
	}

	private void calculateAllRowPossibilities() {
		int amountOfBinaryNumbers = (int) Math.pow(2, length);
		for (int i = 0; i < amountOfBinaryNumbers; i++) {
			int[] row = new int[length];
			int currentBinaryNumber = i;
			for (int j = 1; currentBinaryNumber != 0; j++) {
				row[length - j] = currentBinaryNumber % 2;
				currentBinaryNumber /= 2;
			}
			binaryNumbers.add(row);
			if (isRowValid(row)) {
				allRowPossibilities.add(row);
			}
		}
	}

	public static boolean isRowValid(int[] row) {
		if (moreThan2SimilarBitsAfterEachOther(row)) {
			return false;
		}
		if (unequalAmountOfOnesAndZeros(row)) {
			return false;
		}
		return true;
	}

	public void printAllBinaryNumbers() {
		System.out.println("All binary numbers with " + length + " digits:");
		printList(binaryNumbers);
		System.out.println("Total amount of numbers: " + binaryNumbers.size());
	}

	public void printAllRowPossibilities() {
		System.out.println("All binary numbers, given the row rules:");
		printList(allRowPossibilities);
		System.out.println("Total amount of possibilities: " + allRowPossibilities.size());
	}

	public List<int[]> getAllRowPossibilities() {
		return allRowPossibilities;
	}

	private static boolean moreThan2SimilarBitsAfterEachOther(int[] row) {
		for (int i = 0; i < row.length; i++) {
			int current = row[i];
			int counter_3InARow = 1;
			for (int jj = -1; jj < 2; jj += 2) {
				int previousAndNextIndex = i + jj;
				if (previousAndNextIndex < 0 || previousAndNextIndex > (row.length - 1)) {
					break;
				}
				if (current == row[previousAndNextIndex]) {
					counter_3InARow++;
				}
				if (counter_3InARow == 3) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean unequalAmountOfOnesAndZeros(int[] row) {
		int amountOfOnes = 0;
		for (int i : row) {
			if (i == 1) {
				amountOfOnes++;
			}
		}
		if (amountOfOnes != row.length / 2) {
			return true;
		}
		return false;
	}

	public static void printList(List<int[]> list) {
		for (int[] row : list) {
			for (int i : row) {
				System.out.print(i + " ");
			}
			System.out.println();
		}
	}

}
