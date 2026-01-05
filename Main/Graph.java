package Main;
import java.util.ArrayList;
import java.util.List;

public class Graph {
    private List<Node> nodes;

    Circuit originCircuit;

    public Graph() {
        this.nodes = new ArrayList<>();
        this.originCircuit = new Circuit();
    }

    public void addNode(int colId, int rowId, String label, ArrayList<Link> targetLinks, ArrayList<Link> sourceLinks) {
        Node node = new Node(colId, rowId, label, targetLinks, sourceLinks);
        nodes.add(node);
    }

    public void addNode(Node node) {
        this.nodes.add(node);
    }

    public void addLink(Node source, Node target) {
        Link link = new Link(source, target);
        source.getTargetLinks().add(link);
        target.getSourceLinks().add(link);
    }

    public void addCellToGraph(Cell cell) {
        Node node = new Node(cell.depthx,cell.depthy,cell.getGate().toString(), new ArrayList<>(), new ArrayList<>());
        nodes.add(node);
    }

    public List<Node> getNodes() {
        return nodes;
    }
    public Node getNodeByPosition(int colId, int rowId) {
        for (Node node : nodes) {
            if (node.getColId() == colId && node.getRowId() == rowId) {
                return node;
            }
        }
        return null;
    }
    public void clearGraph() {
        this.nodes.clear();
    }

    public Circuit getOriginCircuit() {
        return originCircuit;
    }

    public void setOriginCircuit(Circuit originCircuit) {
        this.originCircuit = originCircuit;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Node node : nodes) {
            sb.append(node.toString()).append("\n");
        }
        return sb.toString();
    }
}
