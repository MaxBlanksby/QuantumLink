import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        Solution solution = new Solution();
        InputParser parser = new InputParser();
        Circuit circuit = parser.parseJSON("Circuits/TestCircuits/circuit4.json");
        ArrayList<Circuit> pieces = parser.parseCircuitIntoPeicesByDepth(circuit, 5);
        //circuit.printCircuit();

        for (Circuit piece : pieces) {
            piece.printCircuit();
            solution.findBestFit(piece);
            System.out.println("-----");
        }


        //solution.findBestFit(circuit);  
    }
    
}
