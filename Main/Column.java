package Main;
import java.util.ArrayList;
import java.util.List;

public class Column {


    List<Cell> cells = new ArrayList<>();

    boolean containsMultiQubitGate;

    ArrayList<Integer> multiQubitGateDepth;

    Column() {
        this.cells = new ArrayList<>();
        this.containsMultiQubitGate = false;
        this.multiQubitGateDepth = new ArrayList<>();
    }

    public void addCell(Cell cell) {
        cells.add(cell);
    }

    public List<Cell> getCells() {
        return cells;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[ ");
        for (Cell c : cells) {
            sb.append(c.getGate().toString()).append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

    public boolean containsMultiQubitGate() {
        return containsMultiQubitGate;
    }
}