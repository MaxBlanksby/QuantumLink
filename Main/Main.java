package Main;
public class Main {
    public static void main(String[] args) {

        Solution solution = new Solution();
        Util parser = new Util();
        Circuit circuit = parser.parseJSON("Circuits/TestCircuits/circuit4.json");
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
    
