package Main;
import java.util.ArrayList;

public class Node {

    private int colId;
    
    private int rowId;


    private String label;

    private ArrayList<Link> targetLinks;

    private ArrayList<Link> sourceLinks;


    public Node(int rowId, int colId, String label, ArrayList<Link> targetLinks, ArrayList<Link> sourceLinks) {
        this.rowId = rowId;
        this.colId = colId;
        this.label = label;
        this.targetLinks = targetLinks;
        this.sourceLinks = sourceLinks;
    }

    public int getColId() {
        return colId;
    }

    public int getRowId() {
        return rowId;
    }

    public String getLabel() {
        return label;
    }

    public void addTargetLink(Node targetNode) {
        this.targetLinks.add(new Link(this, targetNode));
    }

    public void addSourceLink(Node sourceNode) {
        this.sourceLinks.add(new Link(sourceNode, this));
    }

    
    

    public ArrayList<Link> getTargetLinks() {
        return targetLinks;
    }

    public ArrayList<Link> getSourceLinks() {
        return sourceLinks;
    }
    @Override
    public String toString() {
        return "Node{" +
                "colId=" + colId +
                ", rowId=" + rowId +
                ", label='" + label + '\'' +
                ", targetLinks=" + targetLinks +
                ", sourceLinks=" + sourceLinks +
                '}';
    }
}
