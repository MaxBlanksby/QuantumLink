public class Main {
    public static void main(String[] args) {

        Solution solution = new Solution();
        InputParser parser = new InputParser();
        Circuit circuit = parser.parseJSON("Circuits/circuit1.json");
        solution.findBestFit(circuit);

        
    }
    
}
