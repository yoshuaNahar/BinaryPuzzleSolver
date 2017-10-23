package nl.yoshua.binarypuzzlesolver;


import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;

public class BinaryPuzzleTest {

  private BinaryPuzzle binaryPuzzle;

  @Before
  public void setup() {
    binaryPuzzle = new BinaryPuzzle(6);
  }

  @Test
  public void binaryPuzzleCreatedWithEmptySpots() {
    // NOTE: empty values means -1
    Pattern p = Pattern.compile("(-1 )*");
    Matcher m = p.matcher(binaryPuzzle.toString());

    assertThat(m.matches(), is(true));
  }

  @Test
  public void setCustomValuesShouldFillBinaryPuzzle() {
    // NOTE: empty values means -1
    binaryPuzzle.setValue(0, 0, 1);

    assertThat("", is(true));
  }

}
