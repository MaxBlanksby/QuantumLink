public class Main {
    public static void main(String[] args) {

        Solution solution = new Solution();
        Gen gen = new Gen();
        Circuit circuit = gen.generateCircuit();
        solution.findBestFit(circuit);

        
    }
    
}
