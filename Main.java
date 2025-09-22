public class Main {
    public static void main(String[] args) {

        Solution solution = new Solution();
        CircuitGenInput gen = new CircuitGenInput();
        Circuit circuit = gen.generateCircuit();
        solution.findBestFit(circuit);

        
    }
    
}
