import java.util.ArrayList;
import java.util.Collections;

 

public class Solution {




    // needs to take in the circuit too
    public void findBestFit(Circuit circuit) {

        ArrayList<Integer> ListOfCosts = new ArrayList<>();




        ListOfCosts.add(circuit.getCost(new SuperconductingQubit(), new HeavyHex()));
        ListOfCosts.add(circuit.getCost(new SuperconductingQubit(), new Mesh()));



        int placeOfCost = Collections.min(ListOfCosts);
        int cost = ListOfCosts.get(placeOfCost);


        System.out.println("The best cost is: " + cost);
        //System.out.println("The best layout is: " + ListOfCosts.get(placeOfCost));
    }
}
