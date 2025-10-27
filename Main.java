import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        Solution solution = new Solution();
        InputParser parser = new InputParser();
        Circuit circuit = parser.parseJSON("Circuits/TestCircuits/circuit4.json");
        ArrayList<Circuit> pieces = parser.parseCircuitIntoPiecesByDepth(circuit, 5);
        circuit.convertToJson("Circuits/OutputCircuits/");
        circuit.printCircuit();

        for (Circuit piece : pieces) {
            piece.convertToJson("Circuits/OutputCircuits/");
            piece.printCircuit();
            solution.findBestFit(piece);
            System.out.println("-----");
        }


        //solution.findBestFit(circuit);  
    }
    
}
