package Main;
public class Main {
    public static void main(String[] args) {

        //Solution solution = new Solution();

        Util util = new Util();
        Circuit circuit = util.parseJSON("Circuits/RawInputCircuits/circuitTest.json");
        circuit.printCircuit();
        // circuit.initializedQubitValues.forEach(value -> System.out.print(value + " "));
        // System.out.println();
        Graph graph = util.createGraphFromCircuit(circuit);
        graph.displayGraph();
        util.convertGraphToJSON(graph, "Graphs/TestGraph.json");
        
        
        
        //System.out.println(basicGraph.getNodeByPosition(0,5));
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
    
