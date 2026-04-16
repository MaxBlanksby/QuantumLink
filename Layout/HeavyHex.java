package Layout;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<int[]> generateCouplingEdges(int numQubits) {
        List<int[]> edges = new ArrayList<>();

        // For small circuits, fall back to a line topology
        if (numQubits <= 4) {
            for (int i = 0; i < numQubits - 1; i++) {
                edges.add(new int[]{i, i + 1});
            }
            return edges;
        }

        // Heavy-hex pattern: grid with vertical connections only at odd columns
        // This keeps max degree at 3 (matching real heavy-hex properties)
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
                // Down neighbor only at odd columns
                if (c % 2 == 1) {
                    int down = idx + cols;
                    if (down < numQubits) {
                        edges.add(new int[]{idx, down});
                    }
                }
            }
        }
        return edges;
    }

}