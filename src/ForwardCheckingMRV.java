import java.util.*;

public class ForwardCheckingMRV {

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

        for (int i = 0; i < 2; i++) {
             for(int j = 0; j < 10; j++) {
                 grid[i][j].restartDomain();
             }
        }
        return forwardCheckingMRVRecursive();
    }

    private boolean forwardCheckingMRVRecursive() {
        if (isAssignmentComplete()) {
            return true;
        }

        int[] rowAndColumn = selectMRVVariable();
        if (rowAndColumn == null) return isAssignmentComplete();

        int r = rowAndColumn[0];
        int c = rowAndColumn[1];

        for (int value = 0; value < 10; value++) {
             if (grid[r][c].isValueInDomain(value)) {

                grid[r][c].setValue(value);
                assignmentCounter++;

                List<int[]> prunedDomains = pruneDomains(r, c, value);

                if (prunedDomains != null) {

                    if (forwardCheckingMRVRecursive()) {
                        return true;
                    }
                }

                restoreDomains(prunedDomains);
                grid[r][c].setValue(-1);
            }
        }

        return false; 
    }


    private int[] selectMRVVariable() {
        int minDomainSize = 11; 
        int[] mrvCell = null;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 10; j++) {
                if (grid[i][j].getValue() == -1) { 
                    int currentDomainSize = grid[i][j].getSizeOfAvailableDomain();
                    if (currentDomainSize < minDomainSize) {
                        minDomainSize = currentDomainSize;
                        mrvCell = new int[]{i, j};
                        if (minDomainSize <= 1) { 
                             return mrvCell;
                        }
                    }
                }
            }
        }
        return mrvCell;
    }


    private List<int[]> pruneDomains(int r, int c, int value) {
        List<int[]> prunedList = new ArrayList<>();
        // 1- Row Constraint
        for (int col = 0; col < 10; col++) {
             if (col != c && grid[r][col].getValue() == -1) {
                 if (grid[r][col].isValueInDomain(value)) {
                     grid[r][col].updateDomain(value, false); prunedList.add(new int[]{r, col, value});
                     consistencyCounter++; if (grid[r][col].isDomainEmpty()) return null;
                 }
             }
        }
        // 2- Diagonal Constraint
        int[] dRow = {-1, -1, 1, 1}; int[] dCol = {-1, 1, -1, 1};
        for (int i = 0; i < dRow.length; i++) {
            int nr = r + dRow[i]; int nc = c + dCol[i];
            if (nr >= 0 && nr < 2 && nc >= 0 && nc < 10 && grid[nr][nc].getValue() == -1) {
                 if (grid[nr][nc].isValueInDomain(value)) {
                     grid[nr][nc].updateDomain(value, false); prunedList.add(new int[]{nr, nc, value});
                     consistencyCounter++; if (grid[nr][nc].isDomainEmpty()) return null;
                 }
            }
        }
        // 3- Sum Constraint
        int otherRow = 1 - r;
        if (grid[otherRow][c].getValue() == -1) {
             int requiredSum = grid[2][c].getValue(); int neededValue = requiredSum - value;
             if (neededValue >= 0 && neededValue <= 9) {
                 for (int v = 0; v < 10; v++) {
                     if (v != neededValue && grid[otherRow][c].isValueInDomain(v)) {
                         grid[otherRow][c].updateDomain(v, false); prunedList.add(new int[]{otherRow, c, v});
                         consistencyCounter++;
                     }
                 }
                  if (grid[otherRow][c].isDomainEmpty()) return null;
             } else {
                 return null; 
             }

              if (grid[otherRow][c].isValueInDomain(value)) {
                  grid[otherRow][c].updateDomain(value, false); prunedList.add(new int[]{otherRow, c, value});
                  consistencyCounter++; if (grid[otherRow][c].isDomainEmpty()) return null;
             }
        }
        return prunedList;
    }

    private void restoreDomains(List<int[]> prunedList) {
         if (prunedList == null) return;
         for (int[] prune : prunedList) {
             int r = prune[0]; int c = prune[1]; int restoredValue = prune[2];
             if (grid[r][c].getValue() == -1) { 
                 grid[r][c].updateDomain(restoredValue, true);
             }
         }
    }

    private boolean isAssignmentComplete() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 10; j++) {
                if (grid[i][j].getValue() == -1) return false;
            }
        }
        return true;
    }

}
