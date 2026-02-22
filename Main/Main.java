package Main;
public class Main {
    public static void main(String[] args) {

        //Solution solution = new Solution();

        Util util = new Util();
        Circuit circuit = util.parseJSON("Circuits/RawInputCircuits/circuitDebug.json");
        circuit.printCircuit();
        // circuit.initializedQubitValues.forEach(value -> System.out.print(value + " "));
        // System.out.println();
        Graph graph = util.createGraphFromCircuit(circuit);
        graph.displayGraph();
        util.convertGraphToJSON(graph, "Graphs/DebugGraph.json");


        // Sudo code
        //ArrayList<Graph> connectedComponents = graph.connectedComponents();
        // for (Graph component : connectedComponents) {
        //     component.displayGraph();
        //     System.out.println("-----");
        //     component.determinebestLayout();
        //     System.out.println("-----");
        //     component.SABRE
        //     System.out.println("-----");
        //     component.determinebestModality();
        // }
    }

}
    
