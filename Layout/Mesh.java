package Layout;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<int[]> generateCouplingEdges(int numQubits) {
        List<int[]> edges = new ArrayList<>();
        int cols = (int) Math.ceil(Math.sqrt(numQubits));
        int rows = (int) Math.ceil((double) numQubits / cols);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int idx = r * cols + c;
                if (idx >= numQubits) break;
                // Right neighbor
                if (c + 1 < cols && idx + 1 < numQubits) {
                    edges.add(new int[]{idx, idx + 1});
                }
                // Down neighbor
                int down = idx + cols;
                if (down < numQubits) {
                    edges.add(new int[]{idx, down});
                }
            }
        }
        return edges;
    }
    
}
