package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
    @Test
    public void testSquarePrimesSimple() {
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrimesSimple2() {
        IntList lst = IntList.of(1, 2, 3, 4, 5, 6, 7);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("1 -> 4 -> 9 -> 4 -> 25 -> 6 -> 49", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrimesSimple3() {
        IntList lst = IntList.of(9, 11, 13);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("9 -> 121 -> 169", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrimesSimple4() {
        IntList lst = IntList.of(20, 22, 24, 26, 28);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("20 -> 22 -> 24 -> 26 -> 28", lst.toString());
        assertFalse(changed);
    }

    @Test
    public void testSquarePrimesSimple5() {
        IntList lst = IntList.of(43, 44, 45, 46, 47);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("1849 -> 44 -> 45 -> 46 -> 2209", lst.toString());
        assertTrue(changed);
    }
}
