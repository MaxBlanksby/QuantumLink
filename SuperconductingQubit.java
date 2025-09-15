import java.util.List;

public class SuperconductingQubit implements QubitType {

    @Override
    public String getTypeName() {
        return "Superconducting";
    }

    @Override
    public List<String> getNativeGatesSet() {
        throw new UnsupportedOperationException("Unimplemented method 'getNativeGatesSet'");
    }
 }