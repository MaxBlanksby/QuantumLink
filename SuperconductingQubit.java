import java.util.List;

public class SuperconductingQubit implements Qubit {

    @Override
    public String getTypeName() {
        return "Superconducting";
    }

    @Override
    public List<Qubit> getNeighbors() {
        throw new UnsupportedOperationException("Unimplemented method 'getNeighbors'");
    }

    @Override
    public List<String> getNativeGatesSet() {
        throw new UnsupportedOperationException("Unimplemented method 'getNativeGatesSet'");
    }
 }