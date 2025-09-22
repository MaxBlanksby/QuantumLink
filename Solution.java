import java.util.ArrayList;
import java.util.Collections;

 

public class Solution {




    // needs to take in the circuit too
    public void findBestFit(Circuit circuit) {

        ArrayList<Integer> ListOfCosts = new ArrayList<>();



        
        int costHeavy = circuit.getCost(new SuperconductingQubit(), new HeavyHex());
        int costMesh = circuit.getCost(new SuperconductingQubit(), new Mesh());



        ListOfCosts.add(costHeavy);
        ListOfCosts.add(costMesh);

    


        int cost = Collections.min(ListOfCosts);



        System.out.println("Estimated cost for circuit " + circuit.getCircuitId() + " with layout " + (cost == costHeavy ? new HeavyHex().getLayoutName() : new Mesh().getLayoutName()) + " with qubit " + new SuperconductingQubit().getTypeName() + " is: " + cost);
    }
}
