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
    public int getNeighborsAtPlace(int id) {

        if( id == 0 ) {
            return 2;
        } else if ( id % 5 == 0 || id % 5 == 4 ) {
            return 3;
        } else {
            return 4;
        }
    }

    @Override
    public int getNumQubits() {
        return 54;
    }
    
}
