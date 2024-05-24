public class ForwardChecking {

    static Long assignmentCounter=0L;
    static Long consistencyCounter=0L;

    static boolean possibleInDomain=true; 


    public static boolean backtrackingWithForwardChecking() {
        if (isAssignmentComplete()) {
            System.out.println(" Number of variable assignments: "+assignmentCounter);
            System.out.println(" Number of consistency checks: "+consistencyCounter);


            return true; 
        }

        int[] rowAndColumn = selectUnassignedVariable();
        int rowIndex = rowAndColumn[0];
        int columnIndex = rowAndColumn[1];

        for (int value = 0; value < 10; value++) {        
            if (TennerGrid.grid[rowIndex][columnIndex].getDomain()[value] == 1)
            {            
            if (valueIsConsistent(value, rowIndex, columnIndex) && forwardChecking(value, rowIndex, columnIndex)) 
            {
                TennerGrid.grid[rowIndex][columnIndex].setValue(value);
                TennerGrid.grid[rowIndex][columnIndex].setGivenInInitialState(false); 
                assignmentCounter++;

                if (backtrackingWithForwardChecking()) {
                    return true; 
                }else
                {
                TennerGrid.grid[rowIndex][columnIndex].setValue(-1); 
                updateDomainForAllCells(value, rowIndex, columnIndex, possibleInDomain);

                }
            }
        }
        }


        return false; // Failure
    }

    private static boolean isAssignmentComplete() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j <10 ; j++) {
                if (TennerGrid.grid[i][j].getValue() == -1) {
                    return false; 
                }
            }
        }
        return true; 
    }
    private static int[] selectUnassignedVariable() {
        int[] rowAndColumn=new int[2];

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 10; j++) {
                if (TennerGrid.grid[i][j].getValue()==-1) {
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
        &&  isSumConsistent(value, rowIndex, columnIndex)
        ) {
            return true;
        }
        return false;
    }

    private static boolean isRowConsistent(int value, int rowIndex, int columnIndex ) {
        consistencyCounter++;

        for (int column = 0; column < 10; column++) {
            if (column != columnIndex && TennerGrid.grid[rowIndex][column].getValue() == value) {
                consistencyCounter++;
                return false; 
            }
        }
        return true;
    }

    private static boolean isDiagonalConsistent(int value, int rowIndex, int columnIndex ) {
        consistencyCounter++;

        int temp1 = columnIndex-1;
        int temp2 = columnIndex+1;
        for(int k = rowIndex-1;k >= 0;k--)//diagonal check
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
        for(int k = rowIndex+1;k < 2;k++)//diagonal check
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
        consistencyCounter++;

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
    
        // If the conditions are not met, return true
        return true;
    }   

    
    private static boolean forwardChecking(int value, int rowIndex, int columnIndex) {

        updateDomainForAllCells(value, rowIndex, columnIndex, !possibleInDomain);

        if(allCellsHavePosibleDomains(value, rowIndex, columnIndex))
        return true;
        return false;
    }
   

    private static void updateDomainForAllCells(int value, int rowIndex, int columnIndex, boolean possibleInDomainOrNot)
    {
        //1)update domain for cells in the same row
        for (int column = 0; column < 10; column++) //go through the row column by
        TennerGrid.grid[rowIndex][column].updateDomain(value,possibleInDomainOrNot);


      
        
        if(possibleInDomainOrNot == false)
        {
            if ((columnIndex == 0) && (rowIndex == 1)) 
            TennerGrid.grid[0][1].updateDomain(value,possibleInDomainOrNot);

            else if ((columnIndex == 9) && (rowIndex == 0)) 
            TennerGrid.grid[1][8].updateDomain(value,possibleInDomainOrNot);

            else if ((columnIndex == 0) && (rowIndex == 0)) 
            TennerGrid.grid[1][1].updateDomain(value,possibleInDomainOrNot);

            else if ((columnIndex == 9) && (rowIndex == 1)) 
            TennerGrid.grid[0][8].updateDomain(value,possibleInDomainOrNot);
    
           else if (rowIndex == 0) {
                 TennerGrid.grid[1][columnIndex-1].updateDomain(value,possibleInDomainOrNot);
                 TennerGrid.grid[1][columnIndex+1].updateDomain(value,possibleInDomainOrNot); 
                }
            else if (rowIndex == 1) {
                TennerGrid.grid[0][columnIndex-1].updateDomain(value,possibleInDomainOrNot);
                TennerGrid.grid[0][columnIndex+1].updateDomain(value,possibleInDomainOrNot);
        }
           }
        // //3)update domain for cells in the same diagonal
        else if (possibleInDomainOrNot == true)
        {
            if ((columnIndex == 0) && (rowIndex == 1)                          
            && isRowConsistent(value,0 ,1 )) 

            TennerGrid.grid[0][1].updateDomain(value,possibleInDomainOrNot);

            else if ((columnIndex == 9) && (rowIndex == 0)  && (rowIndex == 1) 
            && isRowConsistent(value,1 ,8)) 

            TennerGrid.grid[1][8].updateDomain(value,possibleInDomainOrNot);
            
            else if ((columnIndex == 9) && (rowIndex == 1) && (rowIndex == 1)  
            && isRowConsistent(value,0 ,8 )) 
            TennerGrid.grid[0][8].updateDomain(value,possibleInDomainOrNot);
    
           else if (rowIndex == 0  && (rowIndex == 1)                          
           && isRowConsistent(value,1 ,columnIndex-1 )&& isRowConsistent(value,1 ,columnIndex+1 )) 
           {
                 TennerGrid.grid[1][columnIndex-1].updateDomain(value,possibleInDomainOrNot);
                 TennerGrid.grid[1][columnIndex+1].updateDomain(value,possibleInDomainOrNot); 
             }

            else if (rowIndex == 1                                            
            && isRowConsistent(value,0 ,columnIndex-1 ) && isRowConsistent(value,0 ,columnIndex+1 )) {
                TennerGrid.grid[0][columnIndex-1].updateDomain(value,possibleInDomainOrNot);
                TennerGrid.grid[0][columnIndex+1].updateDomain(value,possibleInDomainOrNot);
        }
        }
    }//updateDomainForAllCells

    private static boolean allCellsHavePosibleDomains(int value, int rowIndex, int columnIndex)
    {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 10; j++) 
            {
                if(  TennerGrid.grid[i][j].isDomainEmpty())
                return false;
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