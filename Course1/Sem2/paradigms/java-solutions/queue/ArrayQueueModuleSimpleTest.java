package queue;

import static queue.Tester.*;
import static queue.ArrayQueueModule.*;

public class ArrayQueueModuleSimpleTest {

    public static void main(String[] args) {
        for (int i = 0; i < 64; i++) {
            enqueue(i);
        }
        for (int i = 0; i < 32; i++) {
            printTest(dequeue(), i);
        }
        printTest(size(), 32);
        printTest(isEmpty(), false);
        printTest(element(), 32);
        for (int i = 32; i < 64; i++) {
            printTest(dequeue(), i);
        }
        printTest(size(), 0);
        printTest(isEmpty(), true);
        enqueue(1);
        clear();
        printTest(size(), 0);
    }

}
