package swe4.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import swe4.collections.EmptyListException;
import swe4.collections.SLList;

// static import
// importiert alle statischen Methoden (somit kann man assertEquals statt Assertions.assertEquals schreiben)
import static org.junit.jupiter.api.Assertions.*;

public class SLListTest {

    @Test
    public void testAppendAndSizeAreConsistent() {
        SLList<Integer> list = new SLList<>(); // Integer ... heap (langsamer)
        assertEquals(0, list.size());

        list.append(5); // "boxing" (int -> Integer)
        assertEquals(1, list.size());

        list.append(1);
        assertEquals(2, list.size());
    }

    @Test
    public void testPrependAndSizeAreConsistent() {
        SLList<Integer> list = new SLList<>();
        assertEquals(0, list.size());

        list.prepend(5); // "boxing" (int -> Integer)
        assertEquals(1, list.size());

        list.prepend(1);
        assertEquals(2, list.size());
    }

    @Test
    public void testFirstOnEmptyListThrowsException() {
        SLList<Integer> list = new SLList<>();
        assertThrows(EmptyListException.class, () -> list.first());
    }
}
