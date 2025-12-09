package Layout;

public class Mesh implements Layout{

    @Override
    public String getLayoutName() {
        return "Mesh";
    }

    @Override
    public int getMostNumOfNeighbors() {
        return 4; 
    }

    @Override
    public int getNumQubits() {
        return 54;
    }
    
}
