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
        //qubit.getNativeGatesSet();

        int cost = 0;
        int largestNumNeighbor = layout.getMostNumOfNeighbors();


        for (Column col : columns) {
            for (Cell cell : col.getCells()) {
                Gate gate = cell.getGate();
                //System.out.println(cell.getGate().getGateType());
                if (gate.getGateType().equals("•") || gate.getGateType().equals("◦")) {
                    //System.out.println("Found a CNOT");
                    if (col.getCells().size() > largestNumNeighbor) {
                        System.out.println(col.toString());
                        System.out.println("Found a gate with more neighbors");
                        cost += 6 * (col.getCells().size() - largestNumNeighbor);
                    }
                }
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

