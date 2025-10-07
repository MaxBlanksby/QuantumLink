public class Gate {

    private String gateType;

    Gate(String gateType) {
        if (gateType == null){
            throw new IllegalArgumentException("Gate type cannot be null");
        }
        this.gateType = gateType;
    }
    public String getGateType() {
        return gateType;
    }

    @Override
    public String toString() {
    return gateType;
    }


    @Override
    public boolean equals(Object obj){
        if (obj == null) return false;
        if (gateType.equals(((Gate) obj).gateType)){
            return true;
        }
        return false;
    }

}
