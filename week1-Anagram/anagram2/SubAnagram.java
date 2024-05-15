package SubAnagram;

import java.io.*;
import java.util.*;


public class SubAnagram {

    static BufferedReader reader;

    final static int[] scores = {1, 3, 2, 2, 1, 3, 3, 1, 1, 4, 4, 2, 2, 1, 1, 3, 4, 1, 1, 1, 2, 3, 3, 4, 3, 4};
    static List<String> bestAnagram = new ArrayList<>();
    static List<Pair> dictionary = new ArrayList<>();


    public static void countCharsInDictionary(){
        try {
            reader = new BufferedReader(new FileReader("words.txt"));
        }catch (IOException e) {
            throw new RuntimeException("Error opening file: " + e);
        }

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                char[] chars = line.toCharArray();
                int[] charCount = new int[26];

                for (char c : chars) {
                    charCount[c - 'a']++;
                }
                Pair pair = new Pair(line, charCount);
                dictionary.add(pair);  //store word with its alphabet count
            }
        }catch (IOException e){
            throw new RuntimeException("Error occurred: " + e);
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void findHiScore(String file){

        countCharsInDictionary();

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

        for(String anagram : anagrams){  //for each anagram
            int sum = 0;
            char[] chars = anagram.toCharArray();

            for(char c : chars){
                sum += scores[c - 'a'];
            }

            if(sum > hiScore){  //update when the sum exceeds the current top
                hiScore = sum;
                best = anagram;
            }
        }
        bestAnagram.add(best);
        System.out.println(best + ": score..." + hiScore);  //**Debug**
    }

    public static List<String> findAnagram(String random){
        //store anagrams to return later
        ArrayList<String> anagramList = new ArrayList<>();

        String originalLine = random.toLowerCase();
        char[] strList = originalLine.toCharArray();

        int[] inputCharCount = new int[26];

        for(char c : strList){  //store nums of occurrences of each alphabet
            if(Character.isLetter(c)){
                inputCharCount[c - 'a']++;
            }else{
                throw new IllegalArgumentException("Invalid character in input: " + c);
            }
        }

        for(Pair word : dictionary){  //loop for whole dictionary
            boolean isAnagram = true;

            for (int i = 0; i < 26; i++){  //check if the word in dictionary can be created from chars in input
                if (inputCharCount[i] < word.charCount[i]) {
                    isAnagram = false;
                    break;
                }
            }
            if (isAnagram && !word.word.equals(originalLine)){
                anagramList.add(word.word);
            }
        }
            return anagramList;
    }
}

class Pair{
    String word;
    int[] charCount;

    public Pair(String word, int[] charCount){
        this.word = word;
        this.charCount = charCount;
    }
}
