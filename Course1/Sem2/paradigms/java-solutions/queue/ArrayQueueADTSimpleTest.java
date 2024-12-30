package queue;

import static queue.ArrayQueueADT.*;
import static queue.Tester.printTest;

public class ArrayQueueADTSimpleTest {
    public static void main(String[] args) {
        ArrayQueueADT queue = new ArrayQueueADT();
        for (int i = 0; i < 64; i++) {
            enqueue(queue, i);
        }
        for (int i = 0; i < 32; i++) {
            printTest(dequeue(queue), i);
        }
        printTest(size(queue), 32);
        printTest(isEmpty(queue), false);
        printTest(element(queue), 32);
        for (int i = 32; i < 64; i++) {
            printTest(dequeue(queue), i);
        }
        printTest(size(queue), 0);
        printTest(isEmpty(queue), true);
        enqueue(queue, 1);
        clear(queue);
        printTest(size(queue), 0);
    }
}
