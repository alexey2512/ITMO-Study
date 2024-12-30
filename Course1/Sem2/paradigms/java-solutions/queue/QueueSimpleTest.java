package queue;

import static queue.Tester.printTest;

public class QueueSimpleTest {

    public static void test(Queue queue) {
        System.out.println("TESTING " + queue.getClass());
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

    public static void main(String[] args) {
        Queue queue1 = new ArrayQueue();
        test(queue1);
        Queue queue2 = new LinkedQueue();
        test(queue2);
    }

}
