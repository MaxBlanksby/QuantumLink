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
    public int getNeighborsAtPlace(int id) {

        if( id % 4 == 0 ) {
            return 3;
        } else {
            return 2;
        }
    }

    @Override
    public int getNumQubits() {
        // Heavy-hex layouts commonly reference 127-qubit devices (e.g., IBM Eagle)
        // Return a constant for speed; adjust as needed for other sizes.
        return 127;
    }

}


    

