package nl.yoshua.binarypuzzlesolver;

// 1. Should blank be private static inside binary puzzle
// 2. Is the name of values [][] good?
// 3. Say I make a jar of this and someone would use this, he will have to extend this class
//    and implement a better way to get the values instead of the toString() method
public class BinaryPuzzle {

  private static final int BLANK = -1;

  private final int size;
  private final int[][] values;

  public BinaryPuzzle(int size) {
    if (size < 6 || size > 14 || size % 2 != 0) {
      throw new IllegalArgumentException("Invalid size. Only size of 6, 8, 10, 12, 14 allowed.");
    }
    this.size = size;
    values = new int[size][size];
    fillValuesWithBlank();
  }

  public BinaryPuzzle setValue(int x, int y, int value) {
    if (value != 0 && value != 1) {
      throw new IllegalArgumentException("Only 0 or 1 allowed.");
    }
    if ((x < 0 || x >= size) || (y < 0 || y >= size)) {
      throw new IllegalArgumentException(
          "x or y is outside the bounds. Only values between 0 and " + (size - 1) + " allowed.");
    }
    values[x][y] = value;
    return this;
  }

  protected int[][] getValues() {
    return values;
  }

  protected void fillValuesWithBlank() {
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        values[i][j] = BLANK;
      }
    }
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        stringBuilder
            .append(values[i][j])
            .append(" ");
      }
      stringBuilder.append("\n"); // newline
    }
    return stringBuilder.toString();
  }

}
