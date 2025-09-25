import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputParser {


    // does nothing rn (need to work on it)
    public Circuit generateCircuit() {
        return new Circuit();
    }



    public Circuit parseJSON(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        Circuit circuit = new Circuit();

        try {
            JsonNode root = mapper.readTree(new File(filePath));
            System.out.println(root.get("cols").get(1));


            for (int i = 0; i < root.get("cols").size(); i++) {
                JsonNode gate = root.get("cols").get(i);
                


         
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

