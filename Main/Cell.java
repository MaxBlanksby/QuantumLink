package Main;
public class Cell {

    Gate gate;

    int depthx;

    int depthy;

    public Cell(Gate gate, int depthx, int depthy) {
        this.gate = gate;
        this.depthx = depthx;
        this.depthy = depthy;
    }


    public Gate getGate() {
        return gate;
    }

    public int getDepthx() {
        return depthx;
    }

    public int getDepthy() {
        return depthy;
    }




}
