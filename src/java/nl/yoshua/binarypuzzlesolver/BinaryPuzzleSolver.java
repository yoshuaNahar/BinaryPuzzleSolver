package nl.yoshua.binarypuzzlesolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Questions:
// 1. Should solve be static
// 2. Should I have used fields if I not make it static?
// 3. Should solve clone/copy BinaryPuzzle and return new? Or is the fact that it is boolean enough
//    to imply that the object is modified
// 4. Are my helper methods good, and what can I do to improve them?
// 5. As you can see some methods have the default modifier instead of private, because they are
//    less trivial and I wanted to write tests for them.
//    Performing TDD and testing only public methods (solve in this case) seems awkward, but then again
//    Testing all methods and making everything default seems weird, I have never seen someone do that,
//    and those classes that I left private/didn't test contain trivial code.
// 6. My solve method actually only uses the values array from a binary puzzle, should that be the param instead?
// 7. I had a hard time thinking about a good name for my HashMap inside solve() and the
//    mapAllMatchingBinaryNumbersToEachBinaryNumberInsideBinaryPuzzleBasedOnSetValues method.
public class BinaryPuzzleSolver {
  // NOTE: each binary number is an int[]

  public boolean solve(BinaryPuzzle binaryPuzzle) {
    int[][] binaryPuzzleValues = binaryPuzzle.getValues();

    List<int[]> binaryNumbers = generateAllBinaryNumbersWithBitSizeOf(
        binaryPuzzleValues.length);

    binaryNumbers.removeIf(this::binaryNumberInvalidBecauseOfRules);

    HashMap<Integer, List<int[]>> matchingBinaryNumbersForBinaryPuzzleRows =
        mapAllMatchingBinaryNumbersToEachBinaryNumberInsideBinaryPuzzleBasedOnSetValues(
            binaryNumbers, binaryPuzzleValues);

    return solveByTryingOutAllPossibilitiesAndCheckingAgainstBinaryPuzzleRules(
        matchingBinaryNumbersForBinaryPuzzleRows, binaryPuzzleValues);
  }

  List<int[]> generateAllBinaryNumbersWithBitSizeOf(int numberOfBits) {
    List<int[]> binaryNumbers = new ArrayList<>();
    int largestBinaryNumber = (int) Math.pow(2, numberOfBits);
    for (int i = 0; i < largestBinaryNumber; i++) {
      int[] binaryNumber = convertDecimalToBinary(i, numberOfBits);
      binaryNumbers.add(binaryNumber);
    }
    return binaryNumbers;
  }

  private int[] convertDecimalToBinary(int decimalValue, int numberOfBits) {
    int[] binaryNumber = new int[numberOfBits];
    for (int j = 1; decimalValue != 0; j++) {
      binaryNumber[numberOfBits - j] = decimalValue % 2;
      decimalValue /= 2;
    }
    return binaryNumber;
  }

  boolean binaryNumberInvalidBecauseOfRules(int[] binaryNumber) {
    return numberContainsMoreThan2SimilarBitsAfterEachOther(binaryNumber)
        || numberContainsUnequalAmountOfOnesAndZeros(binaryNumber);
  }

  private HashMap<Integer, List<int[]>> mapAllMatchingBinaryNumbersToEachBinaryNumberInsideBinaryPuzzleBasedOnSetValues(
      List<int[]> binaryNumbers, int[][] binaryPuzzleValues) {
    HashMap<Integer, List<int[]>> matchingBinaryNumbersForBinaryPuzzleRows = new HashMap<>();
    for (int i = 0; i < binaryPuzzleValues.length; i++) {
      int[] binaryPuzzleRow = binaryPuzzleValues[i]; // <-- binaryPuzzleRow is also a binaryNumber.
      // I think it is easier in this method to think about the binaryNumbers as a row inside the binary puzzle
      List<int[]> matchingBinaryNumbersForCurrentBinaryPuzzleRow = findMatchesForBinaryPuzzleRowBasedOnSetValues(
          binaryNumbers,
          binaryPuzzleRow);
      matchingBinaryNumbersForBinaryPuzzleRows
          .put(i, matchingBinaryNumbersForCurrentBinaryPuzzleRow);
    }
    return matchingBinaryNumbersForBinaryPuzzleRows;
  }

  private List<int[]> findMatchesForBinaryPuzzleRowBasedOnSetValues(List<int[]> binaryNumbers,
      int[] binaryPuzzleRow) {
    List<int[]> matchingBinaryNumber = new ArrayList<>();
    for (int[] array : binaryNumbers) {
      boolean isMatch = true;
      for (int i = 0; i < binaryPuzzleRow.length; i++) {
        if (binaryPuzzleRow[i] != -1) {
          if (binaryPuzzleRow[i] != array[i]) {
            isMatch = false;
            break;
          }
        }
      }
      if (isMatch) {
        matchingBinaryNumber.add(array);
      }
    }
    return matchingBinaryNumber;
  }

  private boolean solveByTryingOutAllPossibilitiesAndCheckingAgainstBinaryPuzzleRules(
      HashMap<Integer, List<int[]>> matchingBinaryNumbersForBinaryPuzzleRows,
      int[][] binaryPuzzleValues) {
    int allPossibilities = 1;
    for (int i = 0; i < binaryPuzzleValues.length; i++) {
      allPossibilities *= matchingBinaryNumbersForBinaryPuzzleRows.get(i).size();
    }
    System.out.println(
        "Amount of possible board configurations to be checked: " + allPossibilities + "\n");
    return solve(matchingBinaryNumbersForBinaryPuzzleRows, 0, binaryPuzzleValues);
  }

  // 8 months ago I was kissing my fingers like an Italian chef, but now I'm sitting here
  // thinking wtf is this...
  private boolean solve(Map<Integer, List<int[]>> matchingBinaryNumbersForBinaryPuzzleRows,
      int mapKey,
      int[][] binaryPuzzleValues) {
    if (mapKey == matchingBinaryNumbersForBinaryPuzzleRows.size()) {
      return verticalBinariesNumberValid(binaryPuzzleValues) && binaryNumbersUnique(
          binaryPuzzleValues); // base
    }
    for (int i = 0; i < matchingBinaryNumbersForBinaryPuzzleRows.get(mapKey).size(); i++) {
      binaryPuzzleValues[mapKey] = matchingBinaryNumbersForBinaryPuzzleRows.get(mapKey).get(i);
      if (solve(matchingBinaryNumbersForBinaryPuzzleRows, mapKey + 1,
          binaryPuzzleValues)) { // recurse
        return true;
      }
    }
    return false;
  }

  private boolean verticalBinariesNumberValid(int[][] values) {
    int size = values.length;
    int[] verticalBinaryNumber = new int[size];
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        verticalBinaryNumber[j] = values[j][i];
      }
      if (binaryNumberInvalidBecauseOfRules(verticalBinaryNumber)) {
        return false;
      }
    }
    return true;
  }

  private boolean binaryNumbersUnique(int[][] values) {
    return horizontalBinaryNumbersUnique(values) && verticalBinaryNumbersUnique(values);
  }

  private boolean horizontalBinaryNumbersUnique(int[][] values) {
    int size = values.length;
    int[] tempBinaryNumber = new int[size];
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        tempBinaryNumber[j] = values[i][j];
      }
      for (int k = i + 1; k < size; k++) {
        if (Arrays.equals(values[k], tempBinaryNumber)) {
          return false;
        }
      }
    }
    return true;
  }

  private boolean verticalBinaryNumbersUnique(int[][] values) {
    int size = values.length;
    int[] tempBinaryNumber = new int[size];
    for (int i = 0; i < size; i++) {
      int[] tempBinaryNumberVertical = new int[size];
      for (int j = 0; j < size; j++) {
        tempBinaryNumber[j] = values[j][i];
      }
      for (int k = i + 1; k < size; k++) {
        for (int l = 0; l < size; l++) {
          tempBinaryNumberVertical[l] = values[l][k];
        }
        if (Arrays.equals(tempBinaryNumberVertical, tempBinaryNumber)) {
          return false;
        }
      }
    }
    return true;
  }

  boolean numberContainsMoreThan2SimilarBitsAfterEachOther(int[] binaryNumber) {
    for (int i = 0; i < binaryNumber.length; i++) {
      int currentBit = binaryNumber[i];
      int threeInARowCounter = 1;
      for (int j = -1; j < 2; j += 2) {
        int previousAndNextIndex = i + j;
        if (previousAndNextIndex < 0 || previousAndNextIndex > (binaryNumber.length - 1)) {
          break;
        }
        if (currentBit == binaryNumber[previousAndNextIndex]) {
          threeInARowCounter++;
        }
        if (threeInARowCounter == 3) {
          return true;
        }
      }
    }
    return false;
  }

  boolean numberContainsUnequalAmountOfOnesAndZeros(int[] binaryNumber) {
    int amountOfOnes = 0;
    for (int i : binaryNumber) {
      if (i == 1) {
        amountOfOnes++;
      }
    }
    return amountOfOnes != (binaryNumber.length / 2);
  }

}
