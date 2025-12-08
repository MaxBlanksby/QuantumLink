import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Graph {
    private List<Node> nodes;

    Circuit originCircuit;

    public Graph() {
        this.nodes = new ArrayList<>();
        this.originCircuit = new Circuit();
    }

    public Circuit getOriginCircuit() {
        return originCircuit;
    }

    public void addNode(int id, String label, ArrayList<Link> targetLinks, ArrayList<Link> sourceLinks) {
        Node node = new Node(id, label, targetLinks, sourceLinks);
        nodes.add(node);
    }

    public void addLink(Node source, Node target) {
        Link link = new Link(source, target);
        source.getTargetLinks().add(link);
        target.getSourceLinks().add(link);
    }

    public List<Node> getNodes() {
        return nodes;
    }
}
