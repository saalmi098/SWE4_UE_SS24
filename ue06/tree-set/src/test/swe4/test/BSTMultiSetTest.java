package swe4.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import swe4.collections.BSTMultiSet;
import swe4.collections.SortedMultiSet;

/** BinarySearchTree-MultiSet-Test */
public class BSTMultiSetTest {
    @Test
    public void testAddAndSizeAreConsistent() {
        SortedMultiSet<Integer> s = new BSTMultiSet<>(); // <Integer> rechts kann man weglassen. "<>" = diamond operator

        assertEquals(0, s.size());
        s.add(2);
        assertEquals(1, s.size());
        s.add(1);
        assertEquals(2, s.size());
        s.add(3);
        assertEquals(3, s.size());
    }

    @Test
    public void testAddAndGetAreConsistent() {
        SortedMultiSet<Integer> s = new BSTMultiSet<>();
        s.add(2);
        s.add(1);
        s.add(3);
        assertEquals(2, s.get(2));
        assertEquals(1, s.get(1));
        assertEquals(3, s.get(3));
        assertNull(s.get(999));
    }

    @Test
    public void testAddAndContainsAreConsistent() {
        SortedMultiSet<Integer> s = new BSTMultiSet<>();
        assertFalse(s.contains(999));

        s.add(2);
        assertFalse(s.contains(999));
        assertTrue(s.contains(2));

        s.add(1);
        assertFalse(s.contains(999));
        assertTrue(s.contains(1));
        assertTrue(s.contains(2));
    }
}