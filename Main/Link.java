package Main;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Link {
    

    // where every node gets its information from
    @JsonIgnore
    private Node source;

    // where every node sends its information to
    @JsonIgnore
    private Node target;
    
    // JSON-friendly fields for serialization
    private int sourceColId;
    private int sourceRowId;
    private int targetColId;
    private int targetRowId;

    public Link(Node source, Node target) {
        this.source = source;
        this.target = target;
        this.sourceColId = source.getColId();
        this.sourceRowId = source.getRowId();
        this.targetColId = target.getColId();
        this.targetRowId = target.getRowId();
    }

    @JsonIgnore
    public Node getSource() {
        return source;
    }

    @JsonIgnore
    public Node getTarget() {
        return target;
    }
    
    public int getSourceColId() {
        return sourceColId;
    }
    
    public int getSourceRowId() {
        return sourceRowId;
    }
    
    public int getTargetColId() {
        return targetColId;
    }
    
    public int getTargetRowId() {
        return targetRowId;
    }

    @Override
    public String toString() {
        return "Link{source=[" + sourceColId + "," + sourceRowId + "], target=[" + targetColId + "," + targetRowId + "]}";
    }
}
