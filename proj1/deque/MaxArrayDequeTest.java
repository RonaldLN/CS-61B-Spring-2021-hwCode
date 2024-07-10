package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Comparator;

public class MaxArrayDequeTest {
    private static class intComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    }

    private static Integer func(Integer x) {
        return (x * (x + 3) - 25) * x;
    }

    private static class functionComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return func(o1).compareTo(func(o2));
        }
    }

    @Test
    public void testIntComparator() {
        int N = 100;
        Integer max = 0;
        MaxArrayDeque<Integer> ad = new MaxArrayDeque<>(new intComparator());

        for (int i = 0; i < N; i++) {
            int randVal = StdRandom.uniform( 100);
            max = Math.max(max, randVal);
            ad.addLast(randVal);
        }

        assertEquals(max, ad.max());
    }

    @Test
    public void testStringComparator() {
        int N = 5000;
        int maxRes = 0;
        Integer max = 0;
        MaxArrayDeque<Integer> ad = new MaxArrayDeque<>(new intComparator());

        for (int i = 0; i < N; i++) {
            int randVal = StdRandom.uniform( 100);
            int randRes = func(randVal);
            if (randRes > maxRes) {
                maxRes = randRes;
                max = randVal;
            }
            ad.addLast(randVal);
        }

        assertEquals(max, ad.max(new functionComparator()));
    }
}
