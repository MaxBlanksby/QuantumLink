import java.util.ArrayList;
public class Gate {

    private String gateId;
    private String gateType;

    int depth;

    ArrayList<Integer> qubitsInvolved;

    public String getGateId() {
        return gateId;
    }

    public String getGateType() {
        return gateType;
    }

    public int getDepth() {
        return depth;
    }

    public int getStartQubit() {
        return qubitsInvolved.get(0);
    }

    public int getEndQubit() {
        return qubitsInvolved.get(qubitsInvolved.size() - 1);
    }


}
