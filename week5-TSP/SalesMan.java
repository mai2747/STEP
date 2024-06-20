
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
        List<Node> fixedTSP = salesMan.removeIntersection(firstTSP);  //with any intersections removed

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

    public List<Node> removeIntersection(List<Node> nodes){
        List<Node> nodeList = nodes;
        Node current = head.nextNode;
        Node next = current.nextNode;

        // 交点が存在する限りループする。調査中の線分と他のすべての線分を比較、交点があれば繋ぎなおし。
        // 繋ぎなおし方は寝る前の自分にまかせた。
        // current と nextから[current.current & current.next]の座標を取得、別でnextも同様に。
        // 次のノードへ移る際はnext = next.nextNodeで引き渡し。この時点では仮のwhile(true)の中に別ループがあるはず。
        while(true){
            Point p1 = pointList.get(current.current);
            Point p2 = pointList.get(next.current);
            while(!next.equals(current)) {
                Point p3 = pointList.get(next.current);
                Point p4 = pointList.get(next.nextNode.current);

                // check if segments between p1&p2 and p3&p4 have intersection
                boolean haveIntersection = doIntersect(p1, p2, p3, p4);

                // 繋ぎなおし用メソッド
                if(haveIntersection){
                    fixPath(p1, p2, p3, p4);
                }

                next = next.nextNode;
            }

        }
        return nodeList;
    }

    // ** NOT WORKING PROPERLY NOW **
    public void fixPath(Point p1, Point p2,Point p3, Point p4){
        //始点から始点、終点から終点へ繋ぎ直す？
        // change path as below
        // p1 -> p2, p3 -> p4  >>>  p1 -> p3, p2 -> p4
        int index = pointList.indexOf(p1);

    }

    public List<Node> greedyAlgoSearch() {

        int length = pointList.size();
        int currentNode = 0;
        int[] visited = new int[length];  // change value to 1 once visited (indexes are corresponding to node nums)

        // linked list to obtain path at the end
        List<Node> nodeList = new ArrayList<>();
        nodeList.add(head);
        Node prevNode = head;

        // Calculate to find the closest node from the current node,
        // iterating for all nodes in approx order of the shortest path
        for(int i = 0; i < length; i++){
            float x1 = pointList.get(currentNode).x;
            float y1 = pointList.get(currentNode).y;
            float shortest = Integer.MAX_VALUE;
            int nextNode = 0;  // 0 will be used to return to the initial node
            visited[currentNode] = 1;

            // Record the shortest path between current node and others
            for(int j = 0; j < length; j++){
                if(visited[j] == 0) {
                    float x2 = pointList.get(j).x;
                    float y2 = pointList.get(j).y;

                    float ans = (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
                    if (ans < shortest) {
                        shortest = ans;
                        nextNode = j;
                    }
                    //else if(ans == shortest){
                    // What if some have the same distance??
                    //}
                }
            }
            Node node = new Node(currentNode, nextNode, shortest);  //nextNodeとshortest(distance)だけで事足りる?
            prevNode.nextNode = node;
            prevNode = node;
            nodeList.add(node);
            currentNode = nextNode;
        }

        return nodeList;
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

