import FullAnagram.Anagram;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static FullAnagram.Anagram.findAnagram;
import static org.junit.jupiter.api.Assertions.*;


public class AnagramTest {

    @BeforeAll
    public static void setup(){
        Anagram.sortDictionary();
    }

    @Test
    public void testOneAnagram(){
        String random = "ytpographically";
        List<String> expected = new ArrayList<>();
        expected.add("typographically");

        List<String> actual = findAnagram(random);
        assertEquals(expected, actual, "Answer doesn't match!!");
    }

    @Test
    public void testTwoAnagram(){
        String random = "tca";
        List<String> expected = new ArrayList<>();
        expected.add("act");
        expected.add("cat");

        List<String> actual = findAnagram(random);
        assertTrue(expected.containsAll(actual) && actual.containsAll(expected), "Answer doesn't match!!");
    }

    @Test
    public void testMoreAnagram(){
        String random = "trace";
        List<String> expected = new ArrayList<>();
        expected.add("caret");
        expected.add("carte");
        expected.add("cater");
        expected.add("crate");
        expected.add("react");
        expected.add("recta");
        expected.add("trace");

        List<String> actual = findAnagram(random);
        assertTrue(expected.containsAll(actual) && actual.containsAll(expected), "Answer doesn't match!!");
        }

    @Test
    public void testUpperCase(){
        String random = "TAP";
        List<String> expected = new ArrayList<>();
        expected.add("apt");
        expected.add("pat");
        expected.add("tap");

        List<String> actual = findAnagram(random);
        assertTrue(expected.containsAll(actual) && actual.containsAll(expected), "Answer doesn't match!!");
    }

    @Test
    public void textMix(){
        String random = "Ot";
        List<String> expected = new ArrayList<>();
        expected.add("to");

        List<String> actual = findAnagram(random);
        assertTrue(expected.containsAll(actual) && actual.containsAll(expected), "Answer doesn't match!!");
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
        List<String> expected = new ArrayList<>();
        expected.add("a");

        List<String> actual = findAnagram(random);

        assertEquals(actual, expected, "Answer doesn't match!!");
    }

    @Test
    public void textInvalid(){
        assertThrows(IllegalArgumentException.class, () -> {
            String random = "ab@de";
            findAnagram(random);
        });
    }

    @Test
    public void textSpace(){
        String random = "to p";
        List<String> expected = new ArrayList<>();
        expected.add("top");
        expected.add("pot");
        expected.add("opt");

        List<String> actual = findAnagram(random);
        assertTrue(expected.containsAll(actual) && actual.containsAll(expected), "Answer doesn't match!!");
    }

    @Test
    public void testLongCase(){
        String random = "Pneumonoultramicroscopicsilicovolcanoconiosis";
        List<String> actual = findAnagram(random);

        assertTrue(actual.isEmpty(), "Answer doesn't match!!");
    }

}
