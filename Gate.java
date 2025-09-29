public class Gate {

    private String gateType;

    Gate(String gateType) {
        this.gateType = gateType;
    }
    public String getGateType() {
        return gateType;
    }

    @Override
    public String toString() {
    return gateType;
    }
}
