package Main;
public class Link {
    

    // where every node gets its information from
    private Node source;

    // where every node sends its information to
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

    @Override
    public String toString() {
        return "Link{" +
                //"source=" + source +
                //", target=" + target +
                '}';
    }
}
