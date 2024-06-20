package Wikipedia;

import java.io.*;
import java.util.*;

public class Wikipedia {

    // A mapping from a page ID (integer) to the page title.
    private Map<Integer, String> titles = new HashMap<>();  // "12 スマホ"

    // A set of page links.
    private Map<Integer, List<Integer>> links = new HashMap<>();  // "12 342" <- 左のIDから飛ぶ先のID

    public int mostPop;


    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("usage: java Wikipedia.Wikipedia <pages_file> <links_file>");
            System.exit(1);
        }

        Wikipedia wikipedia = new Wikipedia(args[0], args[1]);
        wikipedia.findLongestTitles();
        wikipedia.findMostLinkedPages();
        wikipedia.findShortestPath("渋谷", "パレートの法則");
        wikipedia.findMostPopularPages();
    }

    // Initialize the graph of pages.
    public Wikipedia(String pagesFile, String linksFile) throws IOException {
        // Read the pages file into titles.
        BufferedReader pagesReader = new BufferedReader(new FileReader(pagesFile));
        String line;
        while ((line = pagesReader.readLine()) != null) {
            String[] parts = line.trim().split(" ");
            int id = Integer.parseInt(parts[0]);
            String title = parts[1];
            if (titles.containsKey(id)) {
                throw new IllegalArgumentException("Duplicate ID: " + id);
            }
            titles.put(id, title);
            links.put(id, new ArrayList<>());
        }
        pagesReader.close();
        System.out.println("Finished reading " + pagesFile);

        // Read the links file into links.
        BufferedReader linksReader = new BufferedReader(new FileReader(linksFile));
        while ((line = linksReader.readLine()) != null) {
            String[] parts = line.trim().split(" ");
            int src = Integer.parseInt(parts[0]);
            int dst = Integer.parseInt(parts[1]);
            if (!titles.containsKey(src) || !titles.containsKey(dst)) {
                throw new IllegalArgumentException("Invalid link from " + src + " to " + dst);
            }
            links.get(src).add(dst);
        }
        linksReader.close();
        System.out.println("Finished reading " + linksFile);
        System.out.println();
    }

    // Find the longest titles
    public List<String> findLongestTitles() {  // **Changed from void for test ((Does it have any disadvantages to make it return??
        List<String> titlesList = new ArrayList<>(titles.values());
        List<String> titleListTest = new ArrayList<>();  //**Debug
        titlesList.sort((a, b) -> b.length() - a.length());
        System.out.println("The longest titles are:");
        int count = 0;
        int index = 0;
        while (count < 15 && index < titlesList.size()) {
            if (!titlesList.get(index).contains("_")) {
                String title = titlesList.get(index);
                System.out.println(title);
                titleListTest.add(title);
                count++;
            }
            index++;
        }
        System.out.println();
        return titleListTest;  //**Debug
    }

    // Find the most linked pages
    public void findMostLinkedPages() {
        Map<Integer, Integer> linkCount = new HashMap<>();
        for (int id : titles.keySet()) {
            linkCount.put(id, 0);
        }

        for (int id : titles.keySet()) {
            for (int dst : links.get(id)) {
                linkCount.put(dst, linkCount.get(dst) + 1);
            }
        }

        System.out.println("The most linked pages are:");
        int linkCountMax = Collections.max(linkCount.values());
        for (int dst : linkCount.keySet()) {
            if (linkCount.get(dst) == linkCountMax) {
                System.out.println(titles.get(dst) + " " + linkCountMax);
            }
        }
        System.out.println();
    }

    // Find the shortest path.
    // |start|: The title of the start page
    // |goal|: The title of the goal page
    public void findShortestPath(String start, String goal) {
        List<String> path = new ArrayList<>();

        // Find IDs of the start/goal page
        int startTitleID = findID(start);
        int goalTitleID = findID(goal);

        Queue<Integer> searchQueue = new PriorityQueue<>();  // IDs to search next using BFS
        searchQueue.add(startTitleID);
        Map<Integer, Integer> visited = new HashMap<>();  // store sets of specific ID from visited ID, setting visited ID as value

        while(!searchQueue.isEmpty()) {
            int currentID = searchQueue.poll();
            List<Integer> linkedIDs = links.get(currentID);

            for (int l : linkedIDs) {  // Search for all IDs linked from current ID
                if (l != goalTitleID) {
                    // If the linked page ID is differ from goalID, then put on the map "visited"
                    // This prevents previously visited IDs being searched and will be used to find path
                    visited.put(l,currentID);
                    searchQueue.add(l);
                } else {
                    // Find path using the map "visited" to print path to the goal title
                    int current = l;
                    while (visited.get(current) == null) {
                        String pageName = titles.get(current);
                        path.add(pageName);

                        current = visited.get(current);  // Change current ID to previous ID
                    }
                }
            }
        }
        // Print titles in the path
        // Loop from the last index to skip the process of reversing order (which might be ignorable difference)
        for(int i = path.size()-1; i >= 0; i--){
            System.out.println(path.get(i));
            if(i != 0) System.out.println(", ");
        }

        System.out.println();
        System.out.println(path.size()-1 + " clicks will navigate you to " + "\"" + goal + "\" to " + "\"" + start + "\"");
    }

    public int findID(String pageName){
        int ID = 0;
        for(int id : titles.keySet()){
            if(titles.get(id).equals(pageName)){
                ID = id;
                break;
            }
        }
        return ID;
    }


    // Calculate the page ranks and print the most popular pages
    public void findMostPopularPages() {
        int titlesListSize = titles.size();
        float[] scores = new float[titlesListSize];
        Arrays.fill(scores, 1);  // Initialise scores list value to 1

        while (true) {
            float[] scoresToAdd = new float[titlesListSize];
            for(int i = 0; i < titlesListSize; i++) {  // loop for each node
                float score = scores[i];

                List<Integer> neighbour = links.get(i);
                if(neighbour != null) {
                    // Distribute 85% of the score to its neighbour
                    float addToNeighbour = (float) (score * 0.85 / neighbour.size());
                    for (int index : neighbour) {
                        scoresToAdd[index-1] += addToNeighbour;  //indexに0がなさそうなら+1をするべき
                    }

                    // Distribute 15% of its score to all
                    float addToAll = (float) (score * 0.15 / (titlesListSize - 1));
                    for(int j = 0; j < titlesListSize; j++){
                        if(j != i) scoresToAdd[j] += addToAll;
                    }
                }else{
                    // If the node does not have any neighbours, then Distribute its score to all
                    float addToAll = (score / (titlesListSize - 1));
                    for(int j = 0; j < titlesListSize; j++){
                        if(j != i) scoresToAdd[j] += addToAll;
                    }
                }
            }

            float sum = 0;
            // Update scores
            for(int j = 0; j < titlesListSize; j++){
                float old = scores[j];
                scores[j] += scoresToAdd[j];

                System.out.println("scores[] + scoresToAdd => " + old + " + " + scoresToAdd[j]);

                sum += Math.abs(old - scores[j]);

                System.out.println("old: " + old);
                System.out.println("new: " + scores[j]);
                System.out.println("sum: " + sum);
            }

            // Terminate loop when the sum of the changes in all scores became lower than 0.01
            if (Math.sqrt(sum) < 0.01) break;
            System.out.println(sum);

            try {
                Thread.sleep(10);  // 1秒（1000ミリ秒）間スリープする
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Find a node with the highest score
        float max = Integer.MIN_VALUE;
        mostPop = 0;
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] > max) {
                max = scores[i];
                mostPop = i;
            }
        }

        System.out.println("The most popular page is: " + titles.get(mostPop));
        // TODO:
        //  足されるvalueをまとめるための新リストを作成。scores[]*0.15/scores.lengthの値を格納する fif　を作成。
        //  各IDにおいて、linksを使用して"リンクされている数 n" と"リンク先ID"の割り出し、
        //  scores[]にあるvalue*0.85/n と　fif を　IDのvalueへ追加していく。
        //  全リンク先IDをループできたらscoresに追加、収束済みか確認 -> false であれば再度ループ。do whileがいい。
        //  収束はscoresサイズのループにて　int syusoku += Math.sqrt(scores[] - newScores[]) が 0.01を下回ればtrueとする。
    }

    public String getMostPopular(){
        return titles.get(mostPop);
    }


    // Do something more interesting!
    public void findSomethingMoreInteresting() {
        // TODO: Implement this method
    }
}
