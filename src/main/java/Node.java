import java.util.ArrayList;

public class Node {
    private Header value;
    private ArrayList<Node> children;

    public Node(Header value){
        this.value = value;
        this.children = new ArrayList<>();
    }

    public void addChild(Node node){
        this.children.add(node);
    }

}
