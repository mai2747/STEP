package FullAnagram;

import java.io.*;
import java.util.*;

public class Anagram {

    // Better change to Dictionary<>()?
    static List<Pair> dictionary = new ArrayList<>();

    public static void main(String[] args){
        Scanner s = new Scanner(System.in);
        String input = s.next();

        sortDictionary();
        findAnagram(input);
    }

    public static void sortDictionary(){
        try (BufferedReader reader = new BufferedReader(new FileReader("words.txt"))){
            String line;
            while((line = reader.readLine()) != null){
                char[] div = line.toCharArray();
                Arrays.sort(div);
                String sortedWord = new String(div);

                Pair words = new Pair(sortedWord, line); //store pair of <Sorted word, Original word>
                dictionary.add(words);
            }
            dictionary.sort(Comparator.comparing(a -> a.sorted));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> findAnagram(String random){
        //store anagrams to print later
        ArrayList<String> anagramList = new ArrayList<>();

        //convert to lower case string without spacing
        String originalLine = random.toLowerCase();
        String deleteSpace = originalLine.replaceAll(" ", "");
        char[] strList = deleteSpace.toCharArray();
        for(char c : strList){  //check if the input only contains alphabets
            if(!Character.isLetter(c)){
                throw new IllegalArgumentException("Invalid character: " + c);
            }
        }

        Arrays.sort(strList);
        String wordToFind = new String(strList);

        //binary search
        int left = 0;
        int right = dictionary.size()-1;
        while(left <= right){
            int mid = (left+right) / 2;
            String midWord = dictionary.get(mid).sorted;  //sorted word in mid of dictionary

            if(midWord.equals(wordToFind)){
                //add all words with the same sortedWord
                int pointer = mid;  //search towards left
                while(pointer >= 0 && dictionary.get(pointer).sorted.equals(wordToFind)){
                    anagramList.add(dictionary.get(pointer).word);
                    pointer--;
                }
                pointer = mid + 1;  //search towards right
                while(pointer < dictionary.size() && dictionary.get(pointer).sorted.equals(wordToFind)){
                    anagramList.add(dictionary.get(pointer).word);
                    pointer++;
                }
                break;
            }else if(midWord.compareTo(wordToFind) > 0){
                right = mid -1;
            }else{
                left = mid + 1;
            }
        }

        System.out.println("Found " + anagramList.size() + " anagrams");
        System.out.println("-----------------");
        for(String str : anagramList){
            System.out.println(str);
        }

        return anagramList;
    }
}
class Pair{
    String sorted;
    String word;

    public Pair(String sorted, String word){
        this.sorted = sorted;
        this.word = word;
    }
}