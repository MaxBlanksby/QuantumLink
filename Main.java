import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        Solution solution = new Solution();
        InputParser parser = new InputParser();
        DisplayUtil displayUtil = new DisplayUtil();
        Circuit circuit = parser.parseJSON("Circuits/TestCircuits/circuit4.json");
        

        
        displayUtil.displayGraph(parser.createGraphFromCircuit(circuit));

        //ArrayList<Circuit> pieces = parser.parseCircuitIntoPiecesByDepth(circuit, 5);


        //circuit.convertToJson("Circuits/OutputCircuits/");
        //circuit.printCircuit();

        //solution.findBestFit(circuit);

        // for (Circuit piece : pieces) {
        //     //piece.convertToJson("Circuits/OutputCircuits/");
        //     piece.printCircuit();
        //     solution.findBestFit(piece);
        //     System.out.println("-----");
        // }
    }

}
    
