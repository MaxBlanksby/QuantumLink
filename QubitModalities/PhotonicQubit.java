package QubitModalities;
import java.util.List;

public class PhotonicQubit implements QubitType {

    @Override
    public String getTypeName() {
        return "Photonic Qubit";
    }

    @Override
    public List<String> getNativeGatesSet() {
        return List.of("X", "Y", "Z", "H", "CX");
    }

}
