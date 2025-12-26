package Main;
public class Main {
    public static void main(String[] args) {

        //Solution solution = new Solution();

        Util util = new Util();
        Circuit circuit = util.parseJSON("Circuits/TestCircuits/circuit4.json");
        Graph basicGraph = util.createBasicGraphFromCircuit(circuit);
        //Graph optimizedGraph = util.createGraphFromCircuit(circuit);
        

        util.convertGraphToJSON(basicGraph, "Graphs/basicGraph.json");
        System.out.println(basicGraph.getNodeByPosition(0,5));
        //util.convertGraphToJSON(optimizedGraph, "Graphs/optimizedGraph.json");




        //ArrayList<Circuit> pieces = util.parseCircuitIntoPiecesByDepth(circuit, 5);


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
    
