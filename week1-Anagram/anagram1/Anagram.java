package FullAnagram;

import java.io.*;
import java.util.*;

public class Anagram {

    //
    // Find best approach to store sorted dictionary...
    //
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
            while(true){
                try {
                    if ((line = reader.readLine()) == null) break;

                    char[] div = line.toCharArray();
                    Arrays.sort(div);
                    String sortedWord = new String(div);

                    //store pair of <Sorted word, Original word>
                    Pair words = new Pair(sortedWord, line);
                    dictionary.add(words);

                } catch (IOException e) {
                    throw new RuntimeException("File not found: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dictionary.sort(Comparator.comparing(a -> a.sorted));
    }

    public static List<String> findAnagram(String random){
        //store anagrams to print later
        ArrayList<String> anagramList = new ArrayList<>();

        //convert to lower case string
        String lowerCase = random.toLowerCase();
        char[] strList = lowerCase.toCharArray();
        Arrays.sort(strList);
        String wordToFind = new String(strList);

        //binary search
        int left = 0;
        int right = dictionary.size()-1;
        while(left <= right){
            int mid = (left+right) / 2;
            String midWord = dictionary.get(mid).getSorted();  //sorted word in mid of dictionary

            if(midWord.equals(wordToFind)){
                //add all words with the same sortedWord
                int pointer = mid;  //search towards left
                while(pointer >= 0 && dictionary.get(pointer).getSorted().equals(wordToFind)){
                    anagramList.add(dictionary.get(pointer).getWord());
                    pointer--;
                }
                pointer = mid + 1;  //search towards right
                while(pointer < dictionary.size() && dictionary.get(pointer).getSorted().equals(wordToFind)){
                    anagramList.add(dictionary.get(pointer).getWord());
                    pointer++;
                }
                break;
            }else if(midWord.compareTo(wordToFind) > 0){
                right = mid -1;
            }else{
                left = mid + 1;
            }
        }
        anagramList.remove(lowerCase);  //remove original string

        for(String str : anagramList){
            System.out.println(str);
        }
        return anagramList;
    }
}
class Pair{  //initially intended to use Pair<>()
    String sorted;
    String word;

    public Pair(String sorted, String word){
        this.sorted = sorted;
        this.word = word;
    }

    public String getSorted() {
        return sorted;
    }

    public String getWord() {
        return word;
    }
}