public class Link {
    
    private Node source;
    private Node target;

    public Link(Node source, Node target) {
        this.source = source;
        this.target = target;
    }

    public Node getSource() {
        return source;
    }

    public Node getTarget() {
        return target;
    }
}
