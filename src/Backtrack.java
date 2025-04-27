public class Backtrack {

    private long assignmentCounter = 0L;
    private long consistencyCounter = 0L;
    private Cell[][] grid; 

    public void resetCounters() {
        assignmentCounter = 0L;
        consistencyCounter = 0L;
    }

    public long getAssignmentCount() {
        return assignmentCounter;
    }

    public long getConsistencyCount() {
        return consistencyCounter;
    }

    public boolean solve(Cell[][] grid) {
        this.grid = grid;
        resetCounters();
        return backtrackRecursive();
    }


    private boolean backtrackRecursive() {
        if (isAssignmentComplete()) {
            return true;
        }

        int[] rowAndColumn = selectUnassignedVariable();
        if (rowAndColumn == null) {

             return isAssignmentComplete();
        }

        int rowIndex = rowAndColumn[0];
        int columnIndex = rowAndColumn[1];

        for (int value = 0; value < 10; value++) {
            if (valueIsConsistent(value, rowIndex, columnIndex)) {
                grid[rowIndex][columnIndex].setValue(value);
                assignmentCounter++;

                if (backtrackRecursive()) {
                    return true;
                } else {

                    grid[rowIndex][columnIndex].setValue(-1);
                }
            }
        }

        return false;
    }

    private boolean isAssignmentComplete() {
        for (int i = 0; i < 2; i++) { 
            for (int j = 0; j < 10; j++) { 
                if (grid[i][j].getValue() == -1) {
                    return false;
                }
            }
        }
        return true;
    }

    private int[] selectUnassignedVariable() {
        for (int i = 0; i < 2; i++) { 
            for (int j = 0; j < 10; j++) { 
                 if (grid[i][j].getValue() == -1) {
                     return new int[]{i, j};
                 }
            }
        }
        return null; 
    }

    private boolean valueIsConsistent(int value, int rowIndex, int columnIndex) {
        return isRowConsistent(value, rowIndex, columnIndex) &&
               isDiagonalConsistent(value, rowIndex, columnIndex) &&
               isSumConsistent(value, rowIndex, columnIndex);
    }

    private boolean isRowConsistent(int value, int rowIndex, int columnIndex) {
        for (int col = 0; col < 10; col++) { 
            consistencyCounter++;
            if (col != columnIndex && grid[rowIndex][col].getValue() == value) {
                return false;
            }
        }
        return true;
    }

    private boolean isDiagonalConsistent(int value, int rowIndex, int columnIndex) {
        int[] dRow = {-1, -1, 1, 1}; 
        int[] dCol = {-1, 1, -1, 1}; 

        for (int i = 0; i < dRow.length; i++) {
            int adjacentRow = rowIndex + dRow[i];
            int adjacentCol = columnIndex + dCol[i];

            if (adjacentRow >= 0 && adjacentRow < 2 && adjacentCol >= 0 && adjacentCol < 10) {
                 consistencyCounter++;
                 if (grid[adjacentRow][adjacentCol].getValue() != -1 && grid[adjacentRow][adjacentCol].getValue() == value) {
                     return false; 
                 }
            }
        }
        return true;
    }


     private boolean isSumConsistent(int value, int rowIndex, int columnIndex) {
        consistencyCounter++; 

        int requiredSum = grid[2][columnIndex].getValue();
        int otherRow = 1 - rowIndex; 
        int otherCellValue = grid[otherRow][columnIndex].getValue();

        if (otherCellValue != -1) {
            return (value + otherCellValue) == requiredSum;
        }

        else {

            if (value > requiredSum) return false;

             int neededValue = requiredSum - value;
             if (neededValue >= 0 && neededValue <= 9) {

                 return true;
             } else {
                 return false;
             }
        }
    }

}
