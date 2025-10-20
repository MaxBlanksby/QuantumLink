public class HeavyHex implements Layout{

    @Override
    public String getLayoutName() {
        return "HeavyHex";
    }

    @Override
    public int getMostNumOfNeighbors() {
        return 3; 
    }

    @Override
    public int getNumQubits() {
        return 127;
    }

}


    

