import java.util.ArrayList;

public class Circuit {

    int numQubits;

    ArrayList<Column> columns;

    String circuitId;



    public String getCircuitId() {
        return circuitId;
    }


    Circuit() {
        this.numQubits = 0;
        this.circuitId = "";
        this.columns = new ArrayList<>();
    }


    public int getCost(QubitType qubit, Layout layout) {
        int cost = 0;
        int largestNumNeighbor = layout.getMostNumOfNeighbors();

        for (int colIndex = 0; colIndex < columns.size(); colIndex++) {
            Column col = columns.get(colIndex);
            for (int cellIndex = 0; cellIndex < col.getCells().size(); cellIndex++) {
                Cell cell = col.getCells().get(cellIndex);
                Gate gate = cell.getGate();
                // if (!qubit.getNativeGatesSet().contains(gate.getGateType())) {
                //     cost += 10;
                // }
                if (gate.getGateType().equals("•") || gate.getGateType().equals("◦")) {
                    col.containsMultiQubitGate = true;
                }
            }      
            if (col.containsMultiQubitGate && col.getCells().size() > largestNumNeighbor) {
                cost += 6 * (col.getCells().size() - largestNumNeighbor);
            }    
        }
        return cost;
    }

    public void printCircuit() {
        System.out.println("Circuit ID: " + circuitId);
        System.out.println("Number of Qubits: " + numQubits);
        System.out.println("Columns:");
        for (int i = 0; i < columns.size(); i++) {
            Column col = columns.get(i);
            System.out.print("Depth " + i + ": ");
            for (int j = 0; j < col.getCells().size(); j++) {
                Gate gate = col.getCells().get(j).getGate();
                System.out.print(gate.getGateType() + " ");
            }
            System.out.println();
        }
    }



}

