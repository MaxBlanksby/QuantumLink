import java.lang.reflect.Array;
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

        
        // qubit.getNativeGatesSet();

        // int cost = 0;
        // int gateSize = 0;
        // int largestNumNeighbor = layout.getMostNumOfNeighbors();
        // for (int i = 0; i < gates.size(); i++) {
        //    Gate gate = gates.get(i);
        //     gateSize = gate.inputQubits.size() + gate.outputQubits.size();
        //    if (gateSize > largestNumNeighbor) {
        //     cost += 6 * (gateSize - largestNumNeighbor);
        //    } 

        // }

       return 0;

    }
}

