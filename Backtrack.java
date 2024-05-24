public class Backtrack {

    static Long assignmentCounter=0L;
    static Long consistencyCounter=0L;

    public static boolean backtrack() {
        if (isAssignmentComplete()) {
            System.out.println(" Number of variable assignments: "+assignmentCounter);
            System.out.println(" Number of consistency checks: "+consistencyCounter);

            return true; 
        }

        
        int[] rowAndColumn = selectUnassignedVariable();
        int rowIndex = rowAndColumn[0];
        int columnIndex = rowAndColumn[1];
        for (int value = 0; value < 10; value++) { 
          
            if (valueIsConsistent(value, rowIndex, columnIndex)) {             
                TennerGrid.grid[rowIndex][columnIndex].setValue(value);
                assignmentCounter++;
                TennerGrid.grid[rowIndex][columnIndex].setGivenInInitialState(false); 
                if (backtrack()) {
                    return true; 
                }else
                {
 
                TennerGrid.grid[rowIndex][columnIndex].setValue(-1); 
                }
            }
        }


        return false; // Failure
    }

    private static boolean isAssignmentComplete() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j <10 ; j++) {
                if (TennerGrid.grid[i][j].getValue() == -1) {
                    return false; // Found unassigned cell
                }
            }
        }
        return true; 
    }
    private static int[] selectUnassignedVariable() {
        int[] rowAndColumn=new int[2];

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 10; j++) {
                if (!TennerGrid.grid[i][j].isInitialState() && TennerGrid.grid[i][j].getValue()==-1) {
                    rowAndColumn[0]=i;
                    rowAndColumn[1]=j;
                    return rowAndColumn;
                }
            }
        }
        return null; 

    }

    private static boolean valueIsConsistent(int value, int rowIndex, int columnIndex ) {
        if (isRowConsistent(value, rowIndex,columnIndex )
        &&  isDiagonalConsistent(value, rowIndex, columnIndex)
        &&  isSumConsistent(value, rowIndex, columnIndex)) {
            return true;
        }
        return false;
    }
    private static boolean isRowConsistent(int value, int rowIndex, int columnIndex ) {

        for (int column = 0; column < 10; column++) {
            consistencyCounter++;
            if (column != columnIndex && TennerGrid.grid[rowIndex][column].getValue() == value) {
                return false; 
            }
        }
        return true;
    }



    private static boolean isDiagonalConsistent(int value, int rowIndex, int columnIndex ) {
        int temp1 = columnIndex-1;
        int temp2 = columnIndex+1;
        for(int k = rowIndex-1;k >= 0;k--)
        {
            if(temp1 <= 9 && temp1 >= 0)
            {
                consistencyCounter++;
                if(value == TennerGrid.grid[k][temp1].getValue())
                    return false;
            }      
            if(temp2 <= 9 && temp2 >= 0)
            {
                consistencyCounter++;
                if(value == TennerGrid.grid[k][temp2].getValue())
                    return false;
            } 
            temp1--;
            temp2++;
        }
        temp1 = columnIndex-1;
        temp2 = columnIndex+1;
        for(int k = rowIndex+1;k < 2;k++)
        {
            if(temp1 <= 9 && temp1 >= 0)
            {
                consistencyCounter++;
                if(value == TennerGrid.grid[k][temp1].getValue())
                    return false;
            }    
            if(temp2 <= 9 && temp2 >= 0)
            {
                consistencyCounter++;
                if(value == TennerGrid.grid[k][temp2].getValue())
                    return false;
            }
            temp1--;
            temp2++;
        }
        return true; // Placeholder for diagonal consistency check
    }
 

    private static boolean isSumConsistent(int value, int rowIndex, int columnIndex) {
        // Check if the value is not larger than the sum value
        if (value > TennerGrid.grid[2][columnIndex].getValue()) {
            consistencyCounter++;
            return false;
        }
    
        // Check the sum consistency based on the row index
        else if (rowIndex == 0) {
            // Check if the cell below is initialized
            if (TennerGrid.grid[1][columnIndex].isInitialState() || TennerGrid.grid[1][columnIndex].getValue()!=-1) {
                consistencyCounter++;
                int sum = TennerGrid.grid[1][columnIndex].getValue() + value;
                return sum == TennerGrid.grid[2][columnIndex].getValue();
            }
        } else if (rowIndex == 1) {
            // Check if the cell above is initialized
            if (TennerGrid.grid[0][columnIndex].isInitialState()|| TennerGrid.grid[0][columnIndex].getValue()!=-1) {
                consistencyCounter++;
                int sum = TennerGrid.grid[0][columnIndex].getValue() + value;
                return sum == TennerGrid.grid[2][columnIndex].getValue();
            }
        }
    
        return true;
    }
    

    public static void printGrid() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 10; j++) {
                    System.out.print(TennerGrid.grid[i][j].getValue() + "\t");
                }
             
            System.out.println();
        }
    }

    


}//BackTrackClass