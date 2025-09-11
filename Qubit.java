import java.util.List;

public interface Qubit {

    String getTypeName();

    List<Qubit> getNeighbors();

    List<String> getNativeGatesSet();

}
