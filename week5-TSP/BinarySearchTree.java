import com.sun.source.tree.BinaryTree;

public class BinarySearchTree {
    private Node root;

    public BinarySearchTree(){
        this.root = null;
    }

    public void addNode(Node newNode) {
        root = addNode(root, newNode);
    }

    public Node addNode(Node current, Node newNode){
        if(current == null){
            return current;
        }

        if (newNode.compareTo(current) < 0) {
            current.setLeft(addNode(current.getLeft(), newNode));
        } else if (newNode.compareTo(current) > 0) {
            current.setRight(addNode(current.getRight(), newNode));
        }
        return current;
    }
}
