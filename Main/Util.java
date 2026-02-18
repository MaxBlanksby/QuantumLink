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
import java.util.List;
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
                        // Find the position (offset) of the custom gate in the column
                        int gateOffset = 0;
                        for (int j = 0; j < col.size(); j++) {
                            String gateStr = col.get(j).asText();
                            if (customGateMap.containsKey(gateStr)) {
                                gateOffset = j;
                                break;
                            }
                        }
                        
                        // Expand the custom gate by adding all its columns with proper offset
                        JsonNode customCols = customGateMap.get(customGateId);
                        for (int k = 0; k < customCols.size(); k++) {
                            JsonNode customCol = customCols.get(k);
                            ArrayNode offsetCol = mapper.createArrayNode();
                            
                            // Add 1s for the offset positions before the custom gate
                            for (int p = 0; p < gateOffset; p++) {
                                offsetCol.add(1);
                            }
                            
                            // Add the custom gate's column elements
                            for (int p = 0; p < customCol.size(); p++) {
                                offsetCol.add(customCol.get(p));
                            }
                            
                            expandedCols.add(offsetCol);
                        }
                        System.out.println("  Expanded custom gate: " + customGateId + " at offset " + gateOffset);
                    } else {
                        // Regular column, just add it
                        expandedCols.add(col);
                    }
                }
                // Create the new circuit JSON structure
                ObjectNode expandedCircuit = mapper.createObjectNode();
                expandedCircuit.set("cols", expandedCols);
                
                // Copy over the init array if it exists
                if (root.get("init") != null) {
                    expandedCircuit.set("init", root.get("init"));
                }
                
                // Write to file
                String baseFileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf(".json"));
                newFilePath = "Circuits/InputCircuits/" + baseFileName + "_expanded.json";
                mapper.writerWithDefaultPrettyPrinter().writeValue(new File(newFilePath), expandedCircuit);
                System.out.println("  Expanded circuit saved to: " + newFilePath);
                
            } else {
                // No custom gates, copy to InputCircuits and continue
                System.out.println("No custom gates detected - copying to InputCircuits");
                String baseFileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf(".json"));
                newFilePath = "Circuits/InputCircuits/" + baseFileName + "_final.json";
                mapper.writerWithDefaultPrettyPrinter().writeValue(new File(newFilePath), root);
                System.out.println("  Circuit copied to: " + newFilePath);
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

        // Pad columns so every column has numQubits cells (fill gaps with identity "1" gates)
        for (int colIdx = 0; colIdx < circuit.columns.size(); colIdx++) {
            Column col = circuit.columns.get(colIdx);
            while (col.getCells().size() < circuit.numQubits) {
                int nextDepthy = col.getCells().size();
                Cell identityCell = new Cell(new Gate("1"), colIdx, nextDepthy);
                col.addCell(identityCell);
            }
        }

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
            
            // If this column (time slice) has control bits
            if (col.controlBitsDepth.size() > 0) {
                // Link control bits to target gates (non-control, non-identity) in the same column
                for (int controlBitDepth : col.controlBitsDepth) {
                    Node controlNode = graph.getNodeByPosition(colIdx, controlBitDepth);
                    if (controlNode != null) {
                        // Link this control node only to actual target gates (not 1s, not other controls)
                        for (Cell cell : col.getCells()) {
                            // Skip if it's a control bit or an identity gate
                            if (col.controlBitsDepth.contains(cell.depthy)) {
                                continue;
                            }
                            if (cell.getGate().getGateType().equals("1")) {
                                continue;
                            }
                            Node targetNode = graph.getNodeByPosition(colIdx, cell.depthy);
                            if (targetNode != null) {
                                graph.addLink(controlNode, targetNode);
                            }
                        }
                    }
                }
            }
            
            // For any active gate (non-identity), look at the next column (time slice) at the same row (qubit)
            // If the next node is also a non-identity gate, add a forward link
            if (colIdx < circuit.columns.size() - 1) {
                Column nextCol = circuit.columns.get(colIdx + 1);
                
                for (Cell cell : col.getCells()) {
                    // Skip identity gates - they don't connect to anything
                    if (cell.getGate().getGateType().equals("1")) {
                        continue;
                    }
                    
                    Node currentNode = graph.getNodeByPosition(colIdx, cell.depthy);
                    
                    // Find the corresponding cell in the next column at the same row
                    Cell nextCell = null;
                    for (Cell nc : nextCol.getCells()) {
                        if (nc.depthy == cell.depthy) {
                            nextCell = nc;
                            break;
                        }
                    }
                    
                    // If next cell exists and is not an identity gate, connect forward
                    if (nextCell != null && !nextCell.getGate().getGateType().equals("1")) {
                        Node nextNode = graph.getNodeByPosition(colIdx + 1, cell.depthy);
                        if (currentNode != null && nextNode != null) {
                            graph.addLink(currentNode, nextNode);
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
            // Sort nodes by column (time slice) first, then by row (qubit)
            List<Node> sortedNodes = new ArrayList<>(graph.getNodes());
            sortedNodes.sort((a, b) -> {
                if (a.getColId() != b.getColId()) {
                    return Integer.compare(a.getColId(), b.getColId());
                }
                return Integer.compare(a.getRowId(), b.getRowId());
            });
            mapper.writeValue(new File(filePath), sortedNodes);   
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
