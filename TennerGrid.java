import java.util.Random;

public class TennerGrid {

    static Cell[][] grid = new Cell[3][10];
    
    public static void main(String[] args) {

        long startTime, endTime;
        for(int i = 0;i < 3;i++)
            for(int j = 0;j < 10;j++)
                grid[i][j] = new Cell(-1,false);
        do
        { 
            for(int i = 0;i < 10;i++)
            {
                int val = (int)((Math.random() * (18 - 0)) + 0);
                grid[2][i].setValue(val);
                grid[2][i].setGivenInInitialState(true);
            }
        }while(!Backtrack.backtrack());
        Random random = new Random();
        for(int i = 0;i < 2;i++)
            for(int j = 0;j < 10;j++)
            {
                if(random.nextBoolean())
                    grid[i][j].setValue(-1);
                else
                    grid[i][j].setGivenInInitialState(true);
            }  
        int[][] grid2 = new int[3][10];
        boolean[][] initialS = new boolean[3][10];
        for(int i =0;i < 3;i++)
            for(int j = 0;j < 10;j++)
            {
                grid2[i][j] = grid[i][j].getValue();
                initialS[i][j] = grid[i][j].isInitialState();
            }
            ///////////////////////////////////////////////////////
        System.out.println("\nInitial state: ");
        printGrid(grid);

        System.out.println("\nSolve using Backtrack: ");
        startTime = System.nanoTime();; // Record start time
        Backtrack.backtrack();
        endTime = System.nanoTime();; // Record end time
        long elapsedTime = endTime - startTime; // Calculate elapsed time
        System.out.println("Time taken: " + elapsedTime + " ns");

        printGrid(grid);
        for(int i =0;i < 3;i++)
            for(int j = 0;j < 10;j++)
            {
                grid[i][j].setValue(grid2[i][j]);
                grid[i][j].setGivenInInitialState(initialS[i][j]);
            }
        System.out.println(" \nInitial state: ");
        printGrid(grid);

        System.out.println("\nSolve using ForwardChecking: ");
        startTime = System.nanoTime();;
        ForwardChecking.backtrackingWithForwardChecking();
        endTime = System.nanoTime();;
        elapsedTime = endTime - startTime;
        System.out.println("Time taken: " + elapsedTime + " ns");

        printGrid(grid);

        for(int i =0;i < 3;i++)
            for(int j = 0;j < 10;j++)
            {
                grid[i][j].setValue(grid2[i][j]);
                grid[i][j].setGivenInInitialState(initialS[i][j]);
                grid[i][j].restartDomain();
            }
            System.out.println(" \nInitial state: ");

        printGrid(grid);

        System.out.println("\n Solve using ForwardCheckingMRV: ");
        startTime = System.nanoTime();;
        ForwardCheckingMRV.forwardCheckingMRV();
        endTime = System.nanoTime();;
        elapsedTime = endTime - startTime;
        System.out.println("Time taken: " + elapsedTime + " ns");
        printGrid(grid);
    }

    public static void printGrid(Cell[][] grid) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print(grid[i][j].getValue()+"\t");
            }
            System.out.println();
        }
    }
    
}