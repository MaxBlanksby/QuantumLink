package QubitModalities;
import java.util.List;

public class NeutralAtomQubit implements QubitType {

    @Override
    public String getTypeName() {
        return "Neutral Atom Qubit";
    }

    @Override
    public List<String> getNativeGatesSet() {
        return List.of("X", "Y", "Z", "H", "CX");
    }
}
