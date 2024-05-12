package SubAnagram;

import java.io.*;
import java.util.*;

//
// Need to change the process
// Execution time is now 3 mins for large.txt
//

public class SubAnagram {

    static BufferedReader reader;
    static char[] one = {'a', 'e', 'h', 'i', 'n', 'o', 'r', 's', 't'};
    static char[] two = {'c', 'd', 'l', 'm', 'u'};
    static char[] three = {'b', 'f', 'g', 'p', 'v', 'w', 'y'};
    static char[] four = {'j', 'k', 'q', 'x', 'z'};
    private static final Map<Character, Integer> alphabets = new HashMap<>();
    int[] scores;

    static List<String> bestAnagram = new ArrayList<>();;
    static int highest = 0;


    public static void setCharGroup(char[] c, int value){
        for(char chars: c){
            alphabets.put(chars, value);  //Better make a list?
        }
    }

    public static void writeScores(String file){
        String[] fileType = file.split(".txt", 2);
        String outputFile = fileType[0] + "_answer.txt";

        System.out.println("---Result---");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))){
            for(String str : bestAnagram){
                writer.write(str);
                writer.newLine();
            }
            System.out.println("Highest score: " + highest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void findHiScore(String file){
        //make a list of scores for each alphabet
        if(alphabets.isEmpty()) {
            setCharGroup(one, 1);
            setCharGroup(two, 2);
            setCharGroup(three, 3);
            setCharGroup(four, 4);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            SubAnagram.reader = reader;
            String line;

            while ((line = reader.readLine()) != null) {  //for each line in file
                List<String> anagrams = findAnagram(line);  //find all anagrams
                calculate(anagrams);  //find the anagram with the highest score
            }
            writeScores(file);

        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void calculate(List<String> anagrams){
        String best = null;
        int hiScore = 0;

        for(String anagram : anagrams){  //for each anagram from original string
            int sum = 0;
            for(int i = 0; i < anagram.length(); i++){  //for each letters in an anagram
                int score = alphabets.get(anagram.charAt(i));
                sum += score;
            }
            if(sum > hiScore){  //update when sum exceeds current top
                hiScore = sum;
                best = anagram;
                if(hiScore > highest) highest = hiScore;
            }
        }
        bestAnagram.add(best);
        System.out.println(best);  //**Debug**
    }

    public static List<String> findAnagram(String random){
        //store anagrams to return later
        ArrayList<String> anagramList = new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader("words.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        String lowerCase = random.toLowerCase();
        char[] strList = lowerCase.toCharArray();

        int len = strList.length;
        HashMap<Character, Integer> count = new HashMap<>();

        for(char c : strList){
            if(c > 96 && c < 123){
                count.put(c, count.getOrDefault(c, 0) + 1);
            }else{
                System.out.println("Invalid character in input: " + c);
                return anagramList;
            }
        }

        //check frequency of each letter and compare
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                String word = line.toLowerCase();
                char[] chars = word.toCharArray();

                if (len >= chars.length) {
                    HashMap<Character, Integer> counter = new HashMap<>();
                    boolean anagram = true;

                    for (char c : chars) {
                        counter.put(c, counter.getOrDefault(c, 0) + 1);
                    }

                    for(char c : counter.keySet()){
                        if(!count.containsKey(c) || counter.get(c) > count.get(c)){
                            anagram = false;
                            break;
                        }
                    }
                    if (anagram && !word.equals(lowerCase)) anagramList.add(word);
                }
            }
        }catch (IOException e){
            throw new RuntimeException("Error occurred: " + e);
        }

        return anagramList;
    }
}
