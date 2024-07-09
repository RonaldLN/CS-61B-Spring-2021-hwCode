package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomizedComparisonTest {
    // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        LinkedListDeque<Integer> l1 = new LinkedListDeque<>();
        ArrayDeque<Integer> l2 = new ArrayDeque<>();

        l1.addLast(4);
        l1.addLast(5);
        l1.addLast(6);

        l2.addLast(4);
        l2.addLast(5);
        l2.addLast(6);

        for (int i = 0; i < 3; i++) {
            assertEquals(l1.removeLast(), l2.removeLast());
        }
    }

    @Test
    public void randomizedTest() {
        LinkedListDeque<Integer> L = new LinkedListDeque<>();
        ArrayDeque<Integer> L2 = new ArrayDeque<>();

        int N = 100000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                L2.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int size2 = L2.size();
                assertEquals(size, size2);
                System.out.println("size: " + size);
            } else if (operationNumber == 2) {
                // removeLast
                if (L.size() == 0 || L2.size() == 0) {
                    continue;
                }
                int val = L.removeLast();
                int val2 = L2.removeLast();
                if (val != val2) {
                    System.out.println(L.size());
                    System.out.println(L2.size());
                }
                assertEquals(val, val2);
                System.out.println("removeLast: " + val);
            } else if (operationNumber == 3) {
                // addFirst
                int randVal = StdRandom.uniform(0, 100);
                L.addFirst(randVal);
                L2.addFirst(randVal);
                System.out.println("addFirst(" + randVal + ")");
            }
        }
    }
}
