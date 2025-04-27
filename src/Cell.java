public class Cell{
    private int value;
    private boolean initialState;
    private int[] domain; 

    public Cell(int value, boolean initialState) {
        this.value = value;
        this.initialState = initialState;
        this.domain = new int[10]; 

        if (initialState && value != -1) { 
            for (int i = 0; i < 10; i++) {
                domain[i] = (i == value) ? 1 : 0;
            }
        } else {

             this.initialState = false; 
             this.value = -1; 
            for (int i = 0; i < 10; i++) {
                domain[i] = 1;
            }
        }
    }

    public void restartDomain()
    {
        if (this.initialState && this.value != -1) {
            for (int i = 0; i < 10; i++) {
                domain[i] = (i == this.value) ? 1 : 0;
            }
        } else {

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

    public void setInitialState(boolean initialState) {
        this.initialState = initialState;

        if (initialState && this.value != -1) {
             restartDomain(); 
        }
    }

    public void setAsAssignedValue() {
        this.initialState = false;
    }


    public int[] getDomain() {
        return domain;
    }

    public void setDomain(int[] domain) {
        this.domain = domain;
    }

    public void updateDomain(int value, boolean possible)
    {
        if (value >= 0 && value < 10) {
             domain[value] = possible ? 1 : 0;
        }
    }

    public boolean isDomainEmpty() {
        for (int i = 0; i < 10; i++) {
            if (domain[i] == 1) {
                return false;
            }
        }
        return true;
    }

     public boolean isValueInDomain(int value) {
        if (value >= 0 && value < 10) {
             return domain[value] == 1;
        }
        return false;
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
}
