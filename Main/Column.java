package Main;
import java.util.ArrayList;
import java.util.List;

public class Column {


    List<Cell> cells = new ArrayList<>();

    ArrayList<Integer> controlBitsDepth;

    Column() {
        this.cells = new ArrayList<>();
        this.controlBitsDepth = new ArrayList<>();
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
    public void addCell(Cell cell) {
        this.cells.add(cell);
    }
}