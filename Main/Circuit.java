package Main;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Layout.Layout;
import QubitModalities.QubitType;

public class Circuit {

    int numQubits;

    ArrayList<Column> columns;

    ArrayList<Integer> initializedQubitValues;

    String circuitId;




    public String getCircuitId() {
        return circuitId;
    }


    Circuit() {
        this.numQubits = 0;
        this.circuitId = "";
        this.columns = new ArrayList<>();
        this.initializedQubitValues = new ArrayList<>();
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

    public int getCost(QubitType qubit, Layout layout) {
        // will be implemented later
        int cost = 0;
        return cost;
    }
}

