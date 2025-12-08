import java.util.ArrayList;

public class Node {

    private int id;

    private String label;

    private ArrayList<Link> targetLinks;

    private ArrayList<Link> sourceLinks;


    public Node(int id, String label, ArrayList<Link> targetLinks, ArrayList<Link> sourceLinks) {
        this.id = id;
        this.label = label;
        this.targetLinks = targetLinks;
        this.sourceLinks = sourceLinks;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void addTargetLink(Link link) {
        this.targetLinks.add(link);
    }

    public void addSourceLink(Link link) {
        this.sourceLinks.add(link);
    }
    

    public ArrayList<Link> getTargetLinks() {
        return targetLinks;
    }

    public ArrayList<Link> getSourceLinks() {
        return sourceLinks;
    }
}
