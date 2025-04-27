import java.util.Random;
import java.util.Arrays; 

public class TennerGrid {

    static final int VALUE_ROWS = 2;
    static final int COLS = 10;
    static final int TOTAL_ROWS = VALUE_ROWS + 1; 

    public static void main(String[] args) {

        long startTime, endTime, elapsedTime;

        Cell[][] solvedGrid = generateInitialSolvedGrid();
        if (solvedGrid == null) {
            System.out.println("Failed to generate a valid base grid.");
            return;
        }

        Cell[][] puzzleGrid = createPuzzleFromSolved(solvedGrid, 0.5);

        System.out.println("\nInitial Puzzle State:");
        printGrid(puzzleGrid);

        Backtrack backtrackSolver = new Backtrack();
        ForwardChecking fcSolver = new ForwardChecking();
        ForwardCheckingMRV fcMRVSolver = new ForwardCheckingMRV();

        System.out.println("\nSolving using Backtrack:");
        Cell[][] gridCopy1 = deepCopyGrid(puzzleGrid); 
        startTime = System.nanoTime();
        boolean success1 = backtrackSolver.solve(gridCopy1);
        endTime = System.nanoTime();
        elapsedTime = endTime - startTime;

        if (success1) {
            System.out.println(" Solution Found:");
            printGrid(gridCopy1);
            System.out.println(" Number of variable assignments: " + backtrackSolver.getAssignmentCount());
            System.out.println(" Number of consistency checks: " + backtrackSolver.getConsistencyCount());
        } else {
            System.out.println(" No solution found by Backtrack.");
        }
        System.out.println(" Time taken: " + elapsedTime + " ns");


        System.out.println("\nSolving using Forward Checking:");
        Cell[][] gridCopy2 = deepCopyGrid(puzzleGrid); 
         startTime = System.nanoTime();
        boolean success2 = fcSolver.solve(gridCopy2);
        endTime = System.nanoTime();
        elapsedTime = endTime - startTime;

        if (success2) {
            System.out.println(" Solution Found:");
            printGrid(gridCopy2);
             System.out.println(" Number of variable assignments: " + fcSolver.getAssignmentCount());
             System.out.println(" Number of consistency checks: " + fcSolver.getConsistencyCount());
        } else {
            System.out.println(" No solution found by Forward Checking.");
        }
        System.out.println(" Time taken: " + elapsedTime + " ns");


        System.out.println("\nSolving using Forward Checking + MRV:");
        Cell[][] gridCopy3 = deepCopyGrid(puzzleGrid); 
        startTime = System.nanoTime();
        boolean success3 = fcMRVSolver.solve(gridCopy3);
        endTime = System.nanoTime();
        elapsedTime = endTime - startTime;

        if (success3) {
            System.out.println(" Solution Found:");
            printGrid(gridCopy3);
             System.out.println(" Number of variable assignments: " + fcMRVSolver.getAssignmentCount());
             System.out.println(" Number of consistency checks: " + fcMRVSolver.getConsistencyCount());
        } else {
            System.out.println(" No solution found by Forward Checking + MRV.");
        }
        System.out.println(" Time taken: " + elapsedTime + " ns");
    }

    private static Cell[][] generateInitialSolvedGrid() {
         Cell[][] baseGrid = new Cell[TOTAL_ROWS][COLS];
         Backtrack generatorSolver = new Backtrack();
         int attempts = 0;
         final int MAX_ATTEMPTS = 100;

         do {

             for (int i = 0; i < VALUE_ROWS; i++) {
                 for (int j = 0; j < COLS; j++) {
                     baseGrid[i][j] = new Cell(-1, false);
                 }
             }

             for (int j = 0; j < COLS; j++) {

                 int sum = (int) (Math.random() * 19); 
                 baseGrid[TOTAL_ROWS - 1][j] = new Cell(sum, true); 
             }
             attempts++;
              if (attempts > MAX_ATTEMPTS) {
                   System.err.println("Warning: Max attempts reached generating base grid.");
                   return null; 
              }

         } while (!generatorSolver.solve(baseGrid)); 

         return baseGrid;
    }

     private static Cell[][] createPuzzleFromSolved(Cell[][] solvedGrid, double keepProbability) {
         Cell[][] puzzle = deepCopyGrid(solvedGrid);
         Random random = new Random();

         for (int i = 0; i < VALUE_ROWS; i++) {
             for (int j = 0; j < COLS; j++) {
                 if (random.nextDouble() > keepProbability) {

                     puzzle[i][j].setValue(-1);
                     puzzle[i][j].setInitialState(false); 
                     puzzle[i][j].restartDomain();
                 } else {

                      puzzle[i][j].setInitialState(true);
                 }
             }
         }

         for (int j = 0; j < COLS; j++) {
             puzzle[TOTAL_ROWS - 1][j].setInitialState(true);
         }
         return puzzle;
     }

    public static void printGrid(Cell[][] gridToPrint) {
         if (gridToPrint == null) {
             System.out.println("Grid is null.");
             return;
         }
        System.out.println("-----------------------------------------"); 
        for (int i = 0; i < TOTAL_ROWS; i++) {
            System.out.print("| ");
            for (int j = 0; j < COLS; j++) {
                 if (gridToPrint[i][j] == null) {
                     System.out.print("N\t| "); 
                 } else {
                    int val = gridToPrint[i][j].getValue();

                    System.out.print((val == -1 ? "." : val) + "\t| ");
                 }
            }
            System.out.println();
            if (i == VALUE_ROWS - 1) { 
                 System.out.println("-----------------------------------------"); 
            }
        }
         System.out.println("-----------------------------------------");
    }


    public static Cell[][] deepCopyGrid(Cell[][] original) {
        if (original == null) return null;
        Cell[][] copy = new Cell[TOTAL_ROWS][COLS];
        for (int i = 0; i < TOTAL_ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (original[i][j] != null) {

                    copy[i][j] = new Cell(original[i][j].getValue(), original[i][j].isInitialState());

                    copy[i][j].setDomain(Arrays.copyOf(original[i][j].getDomain(), original[i][j].getDomain().length));
                } else {
                    copy[i][j] = null;
                }
            }
        }
        return copy;
    }
}
