import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class InputParser {


    // does nothing rn (need to work on it)
    public Circuit generateCircuit() {
        return new Circuit();
    }



    public Circuit parseJSON(String filePath) {
        ObjectMapper mapper = new ObjectMapper();


        Circuit circuit = new Circuit();
        circuit.circuitId = filePath;
        
        ArrayList<Gate> gates = new ArrayList<>();

        try {
            JsonNode root = mapper.readTree(new File(filePath));
            System.out.println(root.get("cols").get(1).get(0));


            for (int depth = 0; depth < root.get("cols").size(); depth++) {
                for(int iterator = 0; iterator < root.get("cols").get(depth).size(); iterator++){
                    JsonNode gate = root.get("cols").get(depth).get(iterator);
                    System.out.println(gate);
                }
                JsonNode col = root.get("cols").get(depth);
                //System.out.println(col);
            }


             //System.out.println(root.toString());



            return circuit;

            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}

    





        // JSONParser parser = new JSONParser();


        // try {
        //     Object obj = parser.parse(new FileReader("data.json"));
        //     JSONObject jsonObject = (JSONObject) obj;

        //     // Accessing data
        //     String name = (String) jsonObject.get("name");
        //     long age = (long) jsonObject.get("age");
        //     JSONArray hobbies = (JSONArray) jsonObject.get("hobbies");

        //     System.out.println("Name: " + name);
        //     System.out.println("Age: " + age);
        //     System.out.println("Hobbies:");
        //     for (Object hobby : hobbies) {
        //         System.out.println("- " + hobby);
        //     }

        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

        //

