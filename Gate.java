import java.util.ArrayList;
public class Gate {

    private String gateId;
    private String gateType;

    int depth;

    ArrayList<Integer> inputQubits;

    ArrayList<Integer> outputQubits;


    public String getGateId() {
        return gateId;
    }

    public String getGateType() {
        return gateType;
    }

    public int getDepth() {
        return depth;
    }



}
