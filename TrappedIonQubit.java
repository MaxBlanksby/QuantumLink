import java.util.List;

public class TrappedIonQubit implements QubitType{

    @Override
    public String getTypeName() {
        return "Trapped Ion Qubit";
    }

    @Override
    public List<String> getNativeGatesSet() {
        return List.of("X", "Y", "Z", "H", "CX");
    }
    
}
