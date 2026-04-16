package Layout;

import java.util.List;

public interface Layout {

    String getLayoutName();
    
    int getMostNumOfNeighbors();

    int getNumQubits();

    List<int[]> generateCouplingEdges(int numQubits);
}
