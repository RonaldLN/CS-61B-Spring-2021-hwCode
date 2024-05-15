package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> l1 = new AListNoResizing<>();
        BuggyAList<Integer> l2 = new BuggyAList<>();

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
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> L2 = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 3);
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
                assertEquals(val, val2);
                System.out.println("removeLast: " + val);
            }
        }
    }
}
