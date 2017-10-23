package nl.yoshua.binarypuzzlesolver;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import org.junit.Test;

public class BinaryPuzzleSolverTest {

  private BinaryPuzzleSolver binaryPuzzleSolver = new BinaryPuzzleSolver();

  @Test
  public void test() {
    BinaryPuzzle binaryPuzzle = new BinaryPuzzle(6);
    binaryPuzzle
        .setValue(0, 1, 0)
        .setValue(2, 0, 1)
        .setValue(3, 1, 0)
        .setValue(3, 4, 0)
        .setValue(4, 0, 1)
        .setValue(4, 3, 0)
        .setValue(5, 1, 0)
        .setValue(5, 5, 0);
    System.out.println(binaryPuzzle.toString());

    System.out.println(new BinaryPuzzleSolver().solve(binaryPuzzle));
    System.out.println(binaryPuzzle.toString());
  }

  @Test
  public void generateAllBinaryNumbersWithLengthOf6ShouldReturnCorrectBinaryNumbers() {
    List<int[]> binaryNumbers = binaryPuzzleSolver.generateAllBinaryNumbersWithBitSizeOf(6);

    int[] first = new int[] {0, 0, 0, 0, 0, 0};
    int[] last = new int[] {1, 1, 1, 1, 1, 1};

    assertThat(binaryNumbers.size(), is(64));
    assertThat(binaryNumbers.get(0), is(first));
    assertThat(binaryNumbers.get(63), is(last));
  }

  @Test
  public void generateAllBinaryNumbersWithLengthOf14ShouldReturnCorrectBinaryNumbers() {
    List<int[]> allBinaryNumbers = binaryPuzzleSolver.generateAllBinaryNumbersWithBitSizeOf(14);

    int[] first = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    int[] last = new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

    assertThat(allBinaryNumbers.size(), is(16384));
    assertThat(allBinaryNumbers.get(0), is(first));
    assertThat(allBinaryNumbers.get(16383), is(last));
  }

  @Test
  public void rowInvalidWithInvalidRowsShouldReturnFalse() {
    int[] invalidRow1 = new int[] {0, 0, 0, 0, 0, 0};
    int[] invalidRow2 = new int[] {1, 1, 1, 1, 1, 1};
    int[] invalidRow3 = new int[] {0, 0, 0, 1, 1, 1};
    int[] invalidRow4 = new int[] {0, 0, 1, 1, 0, 0};
    int[] invalidRow5 = new int[] {0, 0, 1, 1, 1, 0};

    assertThat(binaryPuzzleSolver.binaryNumberInvalidBecauseOfRules(invalidRow1), is(true));
    assertThat(binaryPuzzleSolver.binaryNumberInvalidBecauseOfRules(invalidRow2), is(true));
    assertThat(binaryPuzzleSolver.binaryNumberInvalidBecauseOfRules(invalidRow3), is(true));
    assertThat(binaryPuzzleSolver.binaryNumberInvalidBecauseOfRules(invalidRow4), is(true));
    assertThat(binaryPuzzleSolver.binaryNumberInvalidBecauseOfRules(invalidRow5), is(true));
  }

}
