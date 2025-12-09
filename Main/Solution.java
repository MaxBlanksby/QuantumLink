package Main;
import java.util.ArrayList;
import Layout.HeavyHex;
import Layout.Layout;
import Layout.Mesh;
import QubitModalities.SuperconductingQubit;

 

public class Solution {




    // needs to take in the circuit too
    public void findBestFit(Circuit circuit) {

        ArrayList<Layout> layouts = new ArrayList<>();
        layouts.add(new HeavyHex());
        layouts.add(new Mesh());

        int bestCost = Integer.MAX_VALUE;
        Layout bestLayout = null;

        for (Layout layout : layouts) {
            int cost = circuit.getCost(new SuperconductingQubit(), layout);
            if (cost < bestCost) {
            bestCost = cost;
            bestLayout = layout;
            }
        }

        System.out.println("The best cost is: " + bestCost);
        if (bestLayout != null) {
            System.out.println("The best layout is: " + bestLayout.getClass().getSimpleName());
        } else {
            System.out.println("The best layout is: None");
        }   
    }
}
