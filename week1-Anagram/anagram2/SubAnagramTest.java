package SubAnagram;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static SubAnagram.SubAnagram.countCharsInDictionary;
import static SubAnagram.SubAnagram.findAnagram;
import static org.junit.jupiter.api.Assertions.*;

public class SubAnagramTest {

    @BeforeAll
    public static void setup(){
        countCharsInDictionary();
    }

    @Test
    public void testThreeLetters(){
        String random = "tca";
        List<String> expected = new ArrayList<>();
        expected.add("a");
        expected.add("act");
        expected.add("at");
        expected.add("c");
        expected.add("ca");
        expected.add("cat");
        expected.add("t");
        expected.add("ta");

        List<String> actual = findAnagram(random);
        assertEquals(expected, actual, "Answer doesn't match!!");
    }

    @Test
    public void testTwoLetters(){
        String random = "fo";
        List<String> expected = new ArrayList<>();
        expected.add("f");
        expected.add("o");
        expected.add("of");

        List<String> actual = findAnagram(random);
        assertEquals(expected, actual, "Answer doesn't match!!");
    }

    @Test
    public void testUpperCase(){
        String random = "TAP";
        List<String> expected = new ArrayList<>();
        expected.add("a");
        expected.add("apt");
        expected.add("at");
        expected.add("p");
        expected.add("pa");
        expected.add("pat");
        expected.add("t");
        expected.add("ta");

        List<String> actual = findAnagram(random);
        assertEquals(expected, actual, "Answer doesn't match!!");
    }

    @Test
    public void textMix(){
        String random = "Ot";
        List<String> expected = new ArrayList<>();
        expected.add("o");
        expected.add("t");
        expected.add("to");

        List<String> actual = findAnagram(random);
        assertEquals(actual, expected, "Answer doesn't match!!");
    }

    @Test
    public void testNull(){
        String random = "";
        List<String> actual = findAnagram(random);

        assertTrue(actual.isEmpty(), "Answer doesn't match!!");
    }

    @Test
    public void testOneChar(){
        String random = "a";
        List<String> actual = findAnagram(random);

        assertTrue(actual.isEmpty(), "Answer doesn't match!!");
    }

    @Test
    public void testInvalid(){
        assertThrows(IllegalArgumentException.class, () -> {
            String random = "@";
            findAnagram(random);
        });
    }

    @Test
    public void testLongCase(){  //どうやって？？
        String random = "pneumonoultramicroscopicsilicovolcanoconiosis";
        List<String> actual = findAnagram(random);

        assertNotNull(actual);
        assertTrue(actual.contains("mountain"));  //chosen randomly
    }
}
