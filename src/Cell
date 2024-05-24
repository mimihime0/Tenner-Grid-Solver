public class Cell{
    private int value;
    private boolean initialState;
    private int[] domain;

    public Cell(int value, boolean initialState) {
        this.value = value;
        this.initialState = initialState;
        this.domain = new int[10]; // The domain is 0 to 9
        // Initializing the domain values
        if (initialState) {
            // If the cell is initialState in the initial state, then its domain will ONLY be that value
            for (int i = 0; i < 10; i++) {
                domain[i] = (i == value) ? 1 : 0;
            }
        } else {
            // If the cell is not initialState, then its domain is all possible values
            for (int i = 0; i < 10; i++) {
                domain[i] = 1;
            }
        }
    }
    
    public void restartDomain()
    {
        if (initialState) {
            // If the cell is initialState in the initial state, then its domain will ONLY be that value
            for (int i = 0; i < 10; i++) {
                domain[i] = (i == value) ? 1 : 0;
            }
        } else {
            // If the cell is not initialState, then its domain is all possible values
            for (int i = 0; i < 10; i++) {
                domain[i] = 1;
            }
        }      
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isInitialState() {
        return initialState;
    }

    public void setGivenInInitialState(boolean initialState) {
        this.initialState = initialState;
    }

    public int[] getDomain() {
        return domain;
    }

    public void setDomain(int[] domain) {
        this.domain = domain;
    }

    public void updateDomain(int value, boolean possible)
    {
        // Update the domain values after assigning a value be row
        if(possible)
        {
        for (int i = 0; i < 10; i++) {
            if (i == value) 
                domain[i] = 1; 
        }
        }
        else// this value is not possible in domain
        {
        for (int i = 0; i < 10; i++) 
           {
            if (i == value) 
                domain[i] = 0; 
            }
        }
    }
   
    public boolean isDomainEmpty() {
        // Check if the domain is empty
        for (int i = 0; i < 10; i++) {
            if (domain[i] == 1) {
                return false; 
            }
        }
        return true; 
    }

    public int getSizeOfAvailableDomain()
    {
        int availableDomain=0;
        for (int i = 0; i < 10; i++) {
                if (domain[i]==1) {
                    availableDomain++;          
            }
        }
        return availableDomain;
    }


}//Celle Class
