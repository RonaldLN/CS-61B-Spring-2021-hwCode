package tester;

import static org.junit.Assert.*;

import afu.org.checkerframework.checker.signature.qual.SourceName;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {
    @Test
    public void randomizedTest() {
        StudentArrayDeque<Integer> L = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> L2 = new ArrayDequeSolution<>();

        String message = "";
        String m;

        int N = 100000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 6);
            if (operationNumber == 0) {
                // addFirst
                int randVal = StdRandom.uniform(0, 100);
                L.addFirst(randVal);
                L2.addFirst(randVal);
                // System.out.println("addFirst(" + randVal + ")");
                m = "addFirst(" + randVal + ")\n";
                message += m;
                System.out.print(m);
            } else if (operationNumber == 1) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                L2.addLast(randVal);
                // System.out.println("addLast(" + randVal + ")");
                m = "addLast(" + randVal + ")\n";
                message += m;
                System.out.print(m);
            } else if (operationNumber == 2) {
                // removeFirst
                if (L.size() == 0 || L2.size() == 0) {
                    continue;
                }
                Integer val = L.removeFirst();
                Integer val2 = L2.removeFirst();
                if (val != val2) {
                    System.out.println(L.size());
                    System.out.println(L2.size());
                }
                m = "removeFirst: " + val + "\n";
                message += m;
                assertEquals(message, val, val2);
                // System.out.println("removeFirst: " + val);
                System.out.print(m);
            } else if (operationNumber == 3) {
                // removeLast
                if (L.size() == 0 || L2.size() == 0) {
                    continue;
                }
                Integer val = L.removeLast();
                Integer val2 = L2.removeLast();
                if (val != val2) {
                    System.out.println(L.size());
                    System.out.println(L2.size());
                }
                m = "removeLast: " + val + "\n";
                message += m;
                assertEquals(message, val, val2);
                // System.out.println("removeLast: " + val);
                System.out.print(m);
            } else if (operationNumber == 4) {
                // size
                int size = L.size();
                int size2 = L2.size();
                m = "removeLast: " + size + "\n";
                message += m;
                assertEquals(message, size, size2);
                // System.out.println("size: " + size);
                System.out.print(m);
            } else if (operationNumber == 5) {
                // get
                if (L.size() == 0 || L2.size() == 0) {
                    continue;
                }
                double ratio = StdRandom.uniform();
                int size = L2.size();
                int index = (int) (size * ratio);
                Integer val = L.get(index);
                Integer val2 = L2.get(index);
                m = "get: " + val + "\n";
                message += m;
                assertEquals(message, val, val2);
                // System.out.println("get: " + val);
                System.out.print(m);
            }
        }
    }
}
