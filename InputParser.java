import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class InputParser {


    // does nothing rn (need to work on it)
    public Circuit generateCircuit() {
        return new Circuit();
    }



    public Circuit parseJSON(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Column> customGateList = new ArrayList<>();
        ArrayList<Column> columns = new ArrayList<>();
        try {
            
            JsonNode root = mapper.readTree(new File(filePath));
            Circuit circuit = new Circuit();
            circuit.circuitId = filePath;
            // needs to work for all custon circuits
            // for (int i = 0; i < root.get("gates").size(); i++) {
            //     for (JsonNode col : root.get("gates").get(i).get("cols")) {
            //         //this now iterates through each col of the custom circuit they have made
            //     }
            // }
            for (int depth = 0; depth < root.get("cols").size(); depth++) {
                JsonNode colNode = root.get("cols").get(depth);
                Column col = convertJsonNodeToColumn(colNode, depth); 
                columns.add(col);

                int holder = Integer.MIN_VALUE;
                int numQubits = col.getCells().size();

                if (numQubits > holder) {
                    holder = numQubits;
                }
                circuit.numQubits = holder;
            }

            circuit.columns = columns;
            return circuit;

            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public Column convertJsonNodeToColumn(JsonNode colNode , int depthX) {
        Column col = new Column();
        for(int l = 0; l < colNode.size(); l++){
            //System.out.println(colNode.get(l));
            Gate gate = convertJsonNodeToGate(colNode.get(l));
            //System.out.println(gate);
            int depthY = l;
            Cell cell = new Cell(gate, depthX, depthY);

            col.addCell(cell);
        }
        return col;
    }

    
    public Gate convertJsonNodeToGate(JsonNode gateNode) {
        return new Gate(gateNode.asText());
    }
}

    





