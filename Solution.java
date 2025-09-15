public class Solution {


    // needs to take in the circuit too
    public void findBestFit(Circuit circuit) {
        int cost = circuit.getCost(new SuperconductingQubit(), new HeavyHex());



        System.out.println("Estimated cost for circuit " + circuit.getCircuitId() + " with layout " + new HeavyHex().getLayoutName() + " with qubit " + new SuperconductingQubit().getTypeName() + " is: " + cost);
    }
}
