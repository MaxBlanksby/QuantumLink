package Main;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Util {


    // does nothing rn (need to work on it)
    public Circuit generateCircuit() {
        return new Circuit();
    }
    
    public Circuit parseJSON(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Column> columns = new ArrayList<>();
        ArrayList<Integer> initializedQubitValues = new ArrayList<>();
        try {
            //old root area
            Circuit circuit = new Circuit();
            circuit.circuitId = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf(".json"));
            System.out.println("Parsing Circuit ID: " + circuit.circuitId);
            String newFilePath = removeAbstraction(filePath);
            // new root area
            JsonNode newRoot = mapper.readTree(new File(newFilePath));
            //regular circuit parsing
            int holder = Integer.MIN_VALUE;
            for (int depth = 0; depth < newRoot.get("cols").size(); depth++) {
                JsonNode colNode = newRoot.get("cols").get(depth);
                Column col = convertJsonNodeToColumn(colNode, depth); 
                columns.add(col);
                int numQubits = col.getCells().size();
                if (numQubits > holder) {
                    holder = numQubits;
                }
                circuit.numQubits = holder;
            }
            if (newRoot.get("init") != null) {
                JsonNode initQubitValuesNode = newRoot.get("init");
                initializedQubitValues = convertJsonNodeToInitializedQubitValues(initQubitValuesNode);
            }
            circuit.columns = columns;
            circuit.initializedQubitValues = initializedQubitValues;
            return circuit;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String removeAbstraction(String filePath){
        String newFilePath = new String();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(new File(filePath));
            
            // Check if there are custom gates
            if(root.get("gates") != null){
                System.out.println("Custom Gates Detected - Expanding...");
                
                // Build a map of custom gate IDs to their circuit definitions
                Map<String, JsonNode> customGateMap = new HashMap<>();
                for (int i = 0; i < root.get("gates").size(); i++) {
                    JsonNode customGate = root.get("gates").get(i);
                    String gateId = customGate.get("id").asText();
                    customGateMap.put(gateId, customGate.get("circuit").get("cols"));
                    System.out.println("  Registered custom gate: " + gateId);
                }
                // Create expanded circuit
                ArrayNode expandedCols = mapper.createArrayNode();
                JsonNode originalCols = root.get("cols");
                // Iterate through original columns
                for (int i = 0; i < originalCols.size(); i++) {
                    JsonNode col = originalCols.get(i);
                    // Check if this column contains custom gates
                    boolean hasCustomGate = false;
                    String customGateId = null;
                    
                    for (int j = 0; j < col.size(); j++) {
                        String gateStr = col.get(j).asText();
                        if (customGateMap.containsKey(gateStr)) {
                            hasCustomGate = true;
                            customGateId = gateStr;
                            break;
                        }
                    }
                    if (hasCustomGate) {
                        // Expand the custom gate by adding all its columns
                        JsonNode customCols = customGateMap.get(customGateId);
                        for (int k = 0; k < customCols.size(); k++) {
                            expandedCols.add(customCols.get(k));
                        }
                        System.out.println("  Expanded custom gate: " + customGateId);
                    } else {
                        // Regular column, just add it
                        expandedCols.add(col);
                    }
                }
                // Create the new circuit JSON structure
                ObjectNode expandedCircuit = mapper.createObjectNode();
                expandedCircuit.set("cols", expandedCols);
                
                // Write to file
                String baseFileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf(".json"));
                newFilePath = "Circuits/InputCircuits/" + baseFileName + "_expanded.json";
                mapper.writerWithDefaultPrettyPrinter().writeValue(new File(newFilePath), expandedCircuit);
                System.out.println("  Expanded circuit saved to: " + newFilePath);
                
            } else {
                // No custom gates, just copy the file
                System.out.println("No custom gates detected - using original circuit");
                newFilePath = filePath;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newFilePath;
    }

    

    public Column convertJsonNodeToColumn(JsonNode colNode , int depthX) {
        Column col = new Column();
        for(int l = 0; l < colNode.size(); l++){
            Gate gate = convertJsonNodeToGate(colNode.get(l));
            int depthY = l;
            Cell cell = new Cell(gate, depthX, depthY);
            if(gate.getGateType().equals("•")) {
                col.controlBitsDepth.add(depthY);
            }
            col.addCell(cell);
        }
        return col;
    }

    public Gate convertJsonNodeToGate(JsonNode gateNode) {
        return new Gate(gateNode.asText());
    }

    public ArrayList<Integer> convertJsonNodeToInitializedQubitValues(JsonNode initQubitValuesNode) {
        ArrayList<Integer> initializedQubitValues = new ArrayList<>();
        for (int i = 0; i < initQubitValuesNode.size(); i++) {
            initializedQubitValues.add(initQubitValuesNode.get(i).asInt());
        }
        return initializedQubitValues;
    }


    public ArrayList<Circuit> parseCircuitIntoPiecesByDepth(Circuit circuit, int numPieces) {
        int sizeOfPiece = circuit.columns.size() / numPieces;
        ArrayList<Circuit> pieces = new ArrayList<>();
        for (int i = 0; i < numPieces; i++){
            Circuit piece = new Circuit();
            piece.columns = new ArrayList<>(circuit.columns.subList(i * sizeOfPiece, (i + 1) * sizeOfPiece));
            piece.circuitId = circuit.circuitId + "_part" + (i + 1);
            pieces.add(piece);
        }
        return pieces;
    }
    

    public Graph createGraphFromCircuit(Circuit circuit) {
        Graph graph = new Graph();

        // First pass: Create all nodes and add them to the graph
        for (int colIdx = 0; colIdx < circuit.columns.size(); colIdx++) {
            Column col = circuit.columns.get(colIdx);
            for (Cell cell : col.getCells()) {
                graph.addNode(cell);
            }
        }

        // Second pass: Create links
        for (int colIdx = 0; colIdx < circuit.columns.size(); colIdx++) {
            Column col = circuit.columns.get(colIdx);
            
            // If this column has control bits
            if (col.controlBitsDepth.size() > 0) {
                // Link control bits to all other nodes in the same column
                for (int controlBitDepth : col.controlBitsDepth) {
                    Node controlNode = graph.getNodeByPosition(colIdx, controlBitDepth);
                    if (controlNode != null) {
                        // Link this control node to all non-control nodes in the same column
                        for (Cell cell : col.getCells()) {
                            if (!col.controlBitsDepth.contains(cell.depthy)) {
                                Node targetNode = graph.getNodeByPosition(colIdx, cell.depthy);
                                if (targetNode != null) {
                                    graph.addLink(controlNode, targetNode);
                                }
                            }
                        }
                    }
                }
            }
            
            // For any non-control bit, look at the next column at the same depth
            // If the next node is a control bit, add it as a target link
            if (colIdx < circuit.columns.size() - 1) {
                Column nextCol = circuit.columns.get(colIdx + 1);
                
                for (Cell cell : col.getCells()) {
                    // If this cell is not a control bit
                    if (!col.controlBitsDepth.contains(cell.depthy)) {
                        Node currentNode = graph.getNodeByPosition(colIdx, cell.depthy);
                        
                        // Check if the next column has a control bit at the same depth
                        if (nextCol.controlBitsDepth.contains(cell.depthy)) {
                            Node nextControlNode = graph.getNodeByPosition(colIdx + 1, cell.depthy);
                            if (currentNode != null && nextControlNode != null) {
                                graph.addLink(currentNode, nextControlNode);
                            }
                        }
                    }
                }
            }
        }
        
        graph.setOriginCircuit(circuit);
        return graph;
    }

    
    public void convertGraphToJSON(Graph graph, String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            //graph.getNodes().get(0).
            mapper.writeValue(new File(filePath), graph.getNodes());   
         } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void convertCircuitToJSON(Circuit circuit, String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(filePath + circuit.getCircuitId() + "_output.json"), circuit.columns);
            System.out.println("Circuit successfully converted to JSON.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
