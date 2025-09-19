import java.util.ArrayList;

public class Circuit {

    int numQubits;

    int depth;

    ArrayList<Gate> gates;

    int circuitId;



    public int getCircuitId() {
        return circuitId;
    }


    public int getCost(QubitType qubit, Layout layout) {

        layout.getLayoutName();
        qubit.getTypeName();
        qubit.getNativeGatesSet();

        int cost = 0;
        int gateSize = 0;
        int largestNumNeighbor = layout.getMostNumOfNeighbors();
        for (int i = 0; i < gates.size(); i++) {
           Gate gate = gates.get(i);
            gateSize = gate.inputQubits.size() + gate.outputQubits.size();
           if (gateSize > largestNumNeighbor) {
            cost += 6 * (gateSize - largestNumNeighbor);
           } 

        }

       return cost;





   
    }
}

