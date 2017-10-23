### Binary Puzzle Solver

Unintelligent solver for binary puzzles: http://www.binarypuzzle.com/

Extra:<br>
I tried my best to make the `BinaryPuzzleSolver` readable and testable.<br>
For example:
* All my methods are short (max 20 lines)
* None of my methods produce side-effects
* My class is less than 200 lines
* Names are descriptive
* No magic values

Questions I still have:
* If you agree that you should only test public methods,
how would I test only my `solve()` method?
That doesn't seem practical, since 180 lines are run
by only that method. I also can't perform TDD in that case.

* I'm in the only test non-trivial code. But how do I decide,
and creating methods with the `default` modifier is goo?