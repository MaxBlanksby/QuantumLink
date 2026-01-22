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

    public List<Node> getNodes() {
        return nodes;
    }

    public void addNode(int colId, int rowId, String label, ArrayList<Link> targetLinks, ArrayList<Link> sourceLinks) {
        Node node = new Node(colId, rowId, label, targetLinks, sourceLinks);
        nodes.add(node);
    }

    public void addNode(Node node) {
        this.nodes.add(node);
    }

    public void addNode(Cell cell) {
        Node node = new Node(cell.depthx,cell.depthy,cell.getGate().toString(), new ArrayList<>(), new ArrayList<>());
        nodes.add(node);
    }

    public void addLink(Node source, Node target) {
        Link link = new Link(source, target);
        source.getTargetLinks().add(link);
        target.getSourceLinks().add(link);
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
    
    public void displayGraph() {
        System.out.println("=== GRAPH DISPLAY ===");
        System.out.println("Total Nodes: " + nodes.size());
        System.out.println();
        
        for (Node node : nodes) {
            System.out.println("Node [Col=" + node.getColId() + ", Row=" + node.getRowId() + ", Label='" + node.getLabel() + "']");
            
            // Display source links (incoming connections)
            if (node.getSourceLinks().size() > 0) {
                System.out.println("  Source Links (from):");
                for (Link link : node.getSourceLinks()) {
                    Node source = link.getSource();
                    System.out.println("    <- [Col=" + source.getColId() + ", Row=" + source.getRowId() + ", Label='" + source.getLabel() + "']");
                }
            } else {
                System.out.println("  Source Links: (none)");
            }
            
            // Display target links (outgoing connections)
            if (node.getTargetLinks().size() > 0) {
                System.out.println("  Target Links (to):");
                for (Link link : node.getTargetLinks()) {
                    Node target = link.getTarget();
                    System.out.println("    -> [Col=" + target.getColId() + ", Row=" + target.getRowId() + ", Label='" + target.getLabel() + "']");
                }
            } else {
                System.out.println("  Target Links: (none)");
            }
            System.out.println();
        }
        System.out.println("=== END GRAPH ===");
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Node node : nodes) {
            sb.append(node.toString()).append("\n");
        }
        return sb.toString();
    }

    public Graph connectedComponents() {
        // work on later
        return this; // Placeholder return
    }
}
