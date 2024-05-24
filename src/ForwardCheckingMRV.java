public class ForwardCheckingMRV {

    // static Long assignmentCounter=0L;
     // static Long consistencyCounter=0L;
 
     static double assignmentCounter=0;
     static double consistencyCounter=0;
     static boolean possibleInDomain=true; 
 
    
     public static boolean forwardCheckingMRV() {
         if (isAssignmentComplete()) {
             System.out.println(" Number of variable assignments: "+assignmentCounter);
             System.out.println(" Number of consistency checks: "+consistencyCounter);
             
             return true; 
         }
  
         int[] rowAndColumn = selectMRVVariable();
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
 
                 if (forwardCheckingMRV()) {
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
                     return false; // Found unassigned cell
                 }
             }
         }
         return true; // All cells are assigned
     }
 
     private static int[] selectMRVVariable() {
         int[] rowAndColumn=new int[2];
 
         //find cell that either above it or bellow it has another cell so we change the doamin to be 
         for (int i = 0; i < 2; i++) {
             for (int j = 0; j < 10; j++) {
                 if (TennerGrid.grid[i][j].getValue() == -1 )
                 {
                    if (i==0 && TennerGrid.grid[i+1][j].isInitialState() )
                     {
                    rowAndColumn[0]=i;
                    rowAndColumn[1]=j;
                    return rowAndColumn;
                    }else if(i==1 && TennerGrid.grid[i-1][j].isInitialState())
                    {                 
                     rowAndColumn[0]=i;
                     rowAndColumn[1]=j;
                     return rowAndColumn;                  
                    }
                 }
             }
         }
         //choose varible with smallest domain
         
         int sizeOfDomain=10;
 
         for (int i = 0; i < 2; i++) {
             for (int j = 0; j < 10; j++) {
                 if (TennerGrid.grid[i][j].getValue()==-1 && TennerGrid.grid[i][j].getSizeOfAvailableDomain() <sizeOfDomain) {
                     rowAndColumn[0]=i;
                     rowAndColumn[1]=j;
                     sizeOfDomain=TennerGrid.grid[i][j].getSizeOfAvailableDomain();     
                 }
             }
         }
         return rowAndColumn; 
     }//selectMRVVariable
     private static boolean valueIsConsistent(int value, int rowIndex, int columnIndex ) {
 
         if (isRowConsistent(value, rowIndex,columnIndex )
         &&  isDiagonalConsistent(value, rowIndex, columnIndex)
         &&  isSumConsistent(value, rowIndex, columnIndex)) {
             return true;
         }
         return false;
     }
 
     private static boolean isRowConsistent(int value, int rowIndex, int columnIndex ) {
 
         // Check row constraint
         for (int column = 0; column < 10; column++) {
             consistencyCounter++;
             if (column != columnIndex && TennerGrid.grid[rowIndex][column].getValue() == value) {
                 return false; // Value violates row constraint
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
         // consistencyCounter++;
 
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
 
 
         // 2)update domain for cells in the same diagonal to 0
         
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
         // //3)update domain for cells in the same diagonal to 1, must cehck that it's row consitent
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
     }
 
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
 
 
 
 
 }//ForwardCheckingMRV Class