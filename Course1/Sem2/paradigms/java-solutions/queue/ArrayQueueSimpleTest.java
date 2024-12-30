package queue;

import static queue.Tester.*;

public class ArrayQueueSimpleTest {

    public static void main(String[] args) {
        ArrayQueue queue = new ArrayQueue();
        for (int i = 0; i < 64; i++) {
            queue.enqueue(i);
        }
        for (int i = 0; i < 32; i++) {
            printTest(queue.dequeue(), i);
        }
        printTest(queue.size(), 32);
        printTest(queue.isEmpty(), false);
        printTest(queue.element(), 32);
        for (int i = 32; i < 64; i++) {
            printTest(queue.dequeue(), i);
        }
        printTest(queue.size(), 0);
        printTest(queue.isEmpty(), true);
        queue.enqueue(1);
        queue.clear();
        printTest(queue.size(), 0);
    }

}
