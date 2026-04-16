package Main;

import Layout.Mesh;

public class Main {
    public static void main(String[] args) {

        //Solution solution = new Solution();

        Util util = new Util();
        Circuit circuit = util.parseJSON("Circuits/RawInputCircuits/circuitDebug.json");
        circuit.printCircuit();
        // circuit.initializedQubitValues.forEach(value -> System.out.print(value + " "));
        // System.out.println();
        Graph graph = util.createGraphFromCircuit(circuit);
        //graph.displayGraph();
        //util.convertGraphToJSON(graph, "Graphs/DebugGraph.json");
        //graph.connectedComponents();

        // SABRE routing
        Transpiler transpiler = new Transpiler();
        Circuit routed = transpiler.route(circuit, new Mesh());
        System.out.println("\n=== Routed Circuit ===");
        routed.printCircuit();

        // Build and display graph from routed circuit
        Graph routedGraph = util.createGraphFromCircuit(routed);
        routedGraph.displayGraph();

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
