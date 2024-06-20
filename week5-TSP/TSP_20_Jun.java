
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SalesMan {

    List<Point> pointList = new ArrayList<>();

    Node head;

    public SalesMan(){
        head = new Node(-1, 0, 0);
    }


    public static void main(String[] args) {
        SalesMan salesMan = new SalesMan();
        salesMan.storeLocation();
        List<Node> firstTSP = salesMan.greedyAlgoSearch();  //with some intersection
        //List<Node> fixedTSP =
        salesMan.removeIntersection(firstTSP);  //with any intersections removed

        // .....
    }

    public void storeLocation(){
        Scanner s = new Scanner(System.in);
        try{
            Scanner scan;
            scan = new Scanner(new File(s.next()));
            scan.nextLine();  //skip line containing "index"

            //store coordinates for each node
            while(scan.hasNext()){
                float x = scan.nextFloat();
                float y = scan.nextFloat();
                Point point = new Point(x, y);
                pointList.add(point);
            }
        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeIntersection(List<Node> nodes){
        //パラメーター不要？
        List<Node> nodeList = nodes;

        for (int i = 0; i < nodeList.size(); i++) {
            Node current = nodeList.get(i);
            Node next = current.nextNode;

            for (int j = i + 2; j < nodeList.size(); j++) { // start from i+2 to avoid adjacent nodes
                Node compareCurrent = nodeList.get(j);
                Node compareNext = compareCurrent.nextNode;

                Point p1 = pointList.get(current.current);
                Point p2 = pointList.get(next.current);
                Point p3 = pointList.get(compareCurrent.current);
                Point p4 = pointList.get(compareNext.current);

                if (doIntersect(p1, p2, p3, p4)) {
                    fixPath(current, next, compareCurrent, compareNext);
                    break; // Exit the inner loop to start checking for new intersections, but shouldn't?
                }
            }
        }

    }

    // ** NOT WORKING PROPERLY NOW **
    public void fixPath(Node node1, Node node2, Node node3, Node node4){
        // change path as below
        // p1 -> p2, p3 -> p4  >>>  p1 -> p3, p2 -> p4

        // Reconnect to remove the intersection
        Node temp = node2.nextNode;
        node2.nextNode = node4;
        node4.nextNode = temp;

        temp = node3.nextNode;
        node3.nextNode = node1;
        node1.nextNode = temp;
    }

    public List<Node> greedyAlgoSearch() {

        int length = pointList.size();
        int currentNode = 0;
        int[] visited = new int[length];  // change value to 1 once visited (indexes are corresponding to node nums)

        Node prevNode = head;

        // Calculate to find the closest node from the current node,
        // iterating for all nodes in approx order of the shortest path
        for(int i = 0; i < length; i++){
            float x1 = pointList.get(currentNode).x;
            float y1 = pointList.get(currentNode).y;
            float minDistance = Integer.MAX_VALUE;
            int closestNode = 0;  // 0 will be used to return to the initial node
            visited[currentNode] = 1;

            // Record the shortest path between current node and others
            for(int j = 0; j < length; j++){
                if(visited[j] == 0) {
                    float x2 = pointList.get(j).x;
                    float y2 = pointList.get(j).y;

                    float ans = calcDistance(x1, y1, x2, y2);
                    if (ans < minDistance) {
                        minDistance = ans;
                        closestNode = j;
                    }
                    //else if(ans == shortest){
                    // What if some have the same distance??
                    //}
                }
            }
            Node node = new Node(currentNode, closestNode, minDistance);  //nextNodeとshortest(distance)だけで事足りる?
            prevNode.nextNode = node;
            prevNode = node;
            //nodeList.add(node);
            currentNode = closestNode;
        }

        // linked list to obtain path at the end
        List<Node> nodeList = new ArrayList<>();
        Node current = head;
        do {
            nodeList.add(current);
            current = current.nextNode;
        } while (current != head);

        return nodeList;
    }

    public float calcDistance(float x1, float y1, float x2, float y2){
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    // Method to check if two line segments intersect or a point lies on a line segment
    static boolean doIntersect(Point A1, Point B1, Point A2, Point B2) {
        // Find the four orientations needed for general and special cases
        int o1 = orientation(A1, B1, A2);
        int o2 = orientation(A1, B1, B2);
        int o3 = orientation(A2, B2, A1);
        int o4 = orientation(A2, B2, B1);

        // check if they have an intersection
        if (o1 != o2 && o3 != o4) return true;

        // check if they are on any segments
        if (o1 == 0 && onSegment(A1, A2, B1)) return true;
        if (o2 == 0 && onSegment(A1, B2, B1)) return true;
        if (o3 == 0 && onSegment(A2, A1, B2)) return true;
        if (o4 == 0 && onSegment(A2, B1, B2)) return true;

        return false;
    }

    static int orientation(Point A, Point B, Point C) {
        float val = (B.y - A.y) * (C.x - B.x) - (B.x - A.x) * (C.y - B.y);
        if (val == 0.0) return 0;  // collinear
        return (val > 0) ? 1 : 2; // clock or counterclockwise
    }

    static boolean onSegment(Point A, Point B, Point C) {
        if (B.x <= Math.max(A.x, C.x) && B.x >= Math.min(A.x, C.x) &&
                B.y <= Math.max(A.y, C.y) && B.y >= Math.min(A.y, C.y))
            return true;
        return false;
    }

    static class Point {
        float x, y;
        Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}

