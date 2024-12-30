package queue;

import java.util.Objects;
import java.util.function.Predicate;


// Model: elements[front]...elements[top - 1]
// Invariant: for i = front...top - 1: elements[i] != null

// let immutable(q, f, t): for i = f...t - 1: q.elements'[i] = q.elements[i]
// let sameState(q): immutable(q, q.front, q.front + q.size) && q.front' = q.front && q.size' = q.size
// let predicateFalse(q, f, t): for i = f...t: predicate.test(q.elements[i]) = false
// let q = queue

/* Для каждого метода в качестве одного из предусловий неявно прописано q != null.
 * При этом понимается, что это условие остается неизменным и после выполнения метода. */

public class ArrayQueueADT {

    private Object[] elements = new Object[2];
    private int front = 0;
    private int size = 0;


    private static void ensureCapacity(ArrayQueueADT queue) {
        int length = queue.elements.length, front = queue.front, size = queue.size;
        Object[] temp = new Object[length * 2];
        int suffix = length - front;
        if (size < suffix) {
            System.arraycopy(queue.elements, front, temp, 0, length);
        } else {
            System.arraycopy(queue.elements, front, temp, 0, suffix);
            System.arraycopy(queue.elements, 0, temp, suffix, size - suffix);
        }
        queue.elements = temp;
        queue.front = 0;
    }


    private static int move(ArrayQueueADT queue, int index, int step) {
        return (index + step + queue.elements.length) % queue.elements.length;
    }


    // Pred:
    // element != null
    // ----
    // Post:
    // q.front' = q.front
    // q.size' = q.size + 1
    // immutable(q, q.front, q.front + q.size)
    // q.elements'[q.front + q.size] = element
    public static void enqueue(ArrayQueueADT queue, Object element) {
        Objects.requireNonNull(element);
        queue.elements[move(queue, queue.front, queue.size)] = element;
        queue.size++;
        if (queue.size == queue.elements.length) {
            ensureCapacity(queue);
        }
    }


    // Pred:
    // q.size > 0
    // ----
    // Post:
    // sameState(q)
    // result = q.elements[q.front]
    public static Object element(ArrayQueueADT queue) {
        assert queue.size > 0;
        return queue.elements[queue.front];
    }


    // Pred:
    // q.size > 0
    // ----
    // Post:
    // q.front' = q.front + 1
    // q.size' = q.size - 1
    // immutable(q, q.front', q.front + q.size)
    // q.elements'[q.front] = null
    // result = q.elements[q.front]
    public static Object dequeue(ArrayQueueADT queue) {
        Object result = element(queue);
        queue.elements[queue.front] = null;
        queue.front = move(queue, queue.front, 1);
        queue.size--;
        return result;
    }


    // Pred: -
    // ----
    // Post:
    // sameState(q)
    // result = q.size
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }


    // Pred: -
    // ----
    // Post:
    // sameState(q)
    // result = true if q.size = 0 else false
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }


    // Pred: -
    // ----
    // Post: -->
    public static void clear(ArrayQueueADT queue) {
        queue.elements = new Object[2];
        queue.front = 0;
        queue.size = 0;
    }


    private static int iterate(ArrayQueueADT queue, Predicate<Object> predicate, int start, int end, int step, int index) {
        while (start != end) {
            if (predicate.test(queue.elements[start])) {
                return index;
            }
            start = move(queue, start, step);
            index += step;
        }
        return -1;
    }


    // Pred:
    // predicate != null
    // ----
    // Post:
    // sameState(q)
    // predicateFalse(q, q.front, result - 1) && predicate.test(q.elements[result]) = true ||
    // predicateFalse(q, q.front, q.front + q.size - 1) && result = -1 ||
    // q.size = 0 && result = -1
    public static int indexIf(ArrayQueueADT queue, Predicate<Object> predicate) {
        return iterate(queue, predicate, queue.front,
                move(queue, queue.front, queue.size), 1, 0);
    }


    // Pred:
    // predicate != null
    // ----
    // Post:
    // sameState(q)
    // predicateFalse(q, q.front + q.size - 1, result + 1) && predicate.test(q.elements[result]) = true ||
    // predicateFalse(q, q.front, q.front + q.size - 1) && result = -1 ||
    // q.size = 0 && result = -1
    public static int lastIndexIf(ArrayQueueADT queue, Predicate<Object> predicate) {
        return iterate(queue, predicate, move(queue,queue.front, queue.size - 1),
                move(queue, queue.front, -1), -1, queue.size - 1);
    }

}
