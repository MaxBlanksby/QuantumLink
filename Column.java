import java.util.ArrayList;
import java.util.List;

public class Column {




    List<Cell> cells = new ArrayList<>();


    Column() {
        this.cells = new ArrayList<>();
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
            sb.append(c.toString()).append(" ");
        }
        sb.append("]");
        return sb.toString();
    }
}