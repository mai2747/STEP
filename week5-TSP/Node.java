public class Node {
    int current;
    int next;
    float distance;
    Node nextNode;

    Node right;
    Node left;

    public Node(int current, int next, float distance){
        this.current = current;
        this.next = next;
        this.distance = distance;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getLeft() {
        return left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getRight(){
        return right;
    }


    public int compareTo(Node other){
        return Integer.compare(this.current, other.current);
    }
}