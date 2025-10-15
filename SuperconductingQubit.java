import java.util.List;

public class SuperconductingQubit implements QubitType {

    @Override
    public String getTypeName() {
        return "Superconducting";
    }

    @Override
    public List<String> getNativeGatesSet() {
        return List.of("H", "X", "Y", "Z", "S", "T", "CX", "CNOT", "•", "◦");
    }
 }