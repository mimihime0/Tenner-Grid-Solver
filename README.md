# Tenner Grid CSP Solver

## Description

This project implements a solver for the "Tenner Grid" logic puzzle using constraint satisfaction problem (CSP) techniques in Java. The Tenner Grid puzzle involves filling a grid (typically 2 rows by 10 columns, plus a sum row) with digits 0-9 subject to several constraints:

1.  **Row Uniqueness:** Digits in each row must be unique.
2.  **Adjacent Cell Uniqueness:** Cells that touch (including diagonally) must contain different digits.
3.  **Column Sum:** The sum of the digits in each column must equal a pre-defined value specified in a sum row.

This program models the Tenner Grid as a CSP and implements three different solving algorithms:
* Simple Backtracking Search
* Backtracking with Forward Checking
* Backtracking with Forward Checking and the Minimum Remaining Values (MRV) heuristic

The program generates a random Tenner Grid puzzle instance, solves it using each algorithm, and reports performance metrics (variable assignments, consistency checks, and execution time).

## Features

* Solves 2x10 Tenner Grid puzzles.
* Implements three CSP solving algorithms:
    * Backtracking (`Backtrack.java`)
    * Forward Checking (`ForwardChecking.java`)
    * Forward Checking + MRV (`ForwardCheckingMRV.java`)
* Enforces row uniqueness, adjacent cell uniqueness (including diagonals), and column sum constraints.
* Includes internal puzzle generation (creates a solved grid then removes cells).
* Compares algorithm performance by measuring execution time (nanoseconds) and counting variable assignments and consistency checks for each solver.
* Prints the initial puzzle and the solution found by each algorithm.

## Requirements

* Java Development Kit (JDK) 8 or higher (needed for compilation and execution).
* No external libraries are required.

## Input

This version of the program **generates its own puzzle internally** when run. It does not read puzzle definitions from an external file. The generation involves:
1.  Creating random column sums.
2.  Finding a valid, complete grid solution matching those sums using backtracking.
3.  Randomly removing approximately 50% of the solved cells (excluding the sum row) to create the puzzle instance.

*Note: The puzzle generation ensures a solution exists but may not always produce puzzles with unique solutions or varying difficulty levels.*

## Usage

1.  **Save Files:** Ensure all Java source files (`TennerGrid.java`, `Cell.java`, `Backtrack.java`, `ForwardChecking.java`, `ForwardCheckingMRV.java`) are in the same directory.
2.  **Compile:** Open a terminal or command prompt, navigate to that directory, and compile all source files:
    ```
    javac *.java
    ```
    *(Use a semicolon `;` instead of `&&` if using PowerShell to chain commands, e.g., `cd your_dir; javac *.java`)*
3.  **Run:** Execute the main class:
    ```
    java TennerGrid
    ```

## Output Interpretation

The program will print the following to the console:

1.  **Initial Puzzle State:** The generated Tenner Grid puzzle with unsolved cells represented by `.` (dots).
2.  **Solver Sections:** For each algorithm (Backtrack, Forward Checking, Forward Checking + MRV):
    * A header indicating which solver is running.
    * The solution grid found (if successful).
    * The total number of variable assignments made during the search.
    * The total number of consistency checks performed.
    * The time taken for the solver to run in nanoseconds.
    * A message indicating if no solution was found (which shouldn't happen with the current generation method, but is possible if the code has bugs).

## Code Structure

* **`TennerGrid.java`:** Contains the `main` method, puzzle generation logic, grid printing, deep copying, and orchestrates the running and timing of the different solvers. Defines grid size constants.
* **`Cell.java`:** Represents a single cell in the grid. Stores its current value (`-1` if unassigned), whether it was part of the initial puzzle (`initialState`), and its current domain of possible values (used by Forward Checking algorithms).
* **`Backtrack.java`:** Implements the simple backtracking search algorithm.
* **`ForwardChecking.java`:** Implements backtracking search enhanced with forward checking to prune domains of future variables.
* **`ForwardCheckingMRV.java`:** Extends Forward Checking by adding the Minimum Remaining Values (MRV) heuristic for selecting the next variable to assign.

## Notes & Limitations

* **Hardcoded Size:** The grid size is currently fixed at 2 value rows and 10 columns within the code. Modifying requires changing constants and potentially loop bounds/logic in multiple files.
* **Puzzle Generation:** The current puzzle generation logic is basic and may not produce consistently challenging or uniquely solvable puzzles.
* **Constraint Definition:** This implementation enforces row uniqueness, adjacent (including diagonal) uniqueness, and column sums. It does *not* enforce uniqueness within columns (which isn't a standard Tenner Grid rule but sometimes causes confusion). Vertically adjacent cells are implicitly handled by the diagonal constraint check.
