package queue;

import java.util.Objects;
import java.util.function.Predicate;


// Model: elements[front]...elements[top - 1]
// Invariant: for i = front...top - 1: elements[i] != null

// let immutable(f, t): for i = f...t - 1: elements'[i] = elements[i]
// let sameState(): immutable(front, front + size) && front' = front && size' = size
// let predicateFalse(f, t): for i = f...t: predicate.test(elements[i]) = false

public class ArrayQueueModule {

    private static Object[] elements = new Object[2];
    private static int front = 0;
    private static int size = 0;


    private static void ensureCapacity() {
        Object[] temp = new Object[elements.length * 2];
        int suffix = elements.length - front;
        if (size < suffix) {
            System.arraycopy(elements, front, temp, 0, elements.length);
        } else {
            System.arraycopy(elements, front, temp, 0, suffix);
            System.arraycopy(elements, 0, temp, suffix, size - suffix);
        }
        elements = temp;
        front = 0;
    }


    private static int move(int index, int step) {
        return (index + step + elements.length) % elements.length;
    }


    // Pred:
    // element != null
    // ----
    // Post:
    // front' = front
    // size' = size + 1
    // immutable(front, front + size)
    // elements'[front + size] = element
    public static void enqueue(Object element) {
        Objects.requireNonNull(element);
        elements[move(front, size)] = element;
        size++;
        if (size == elements.length) {
            ensureCapacity();
        }
    }


    // Pred:
    // size > 0
    // ----
    // Post:
    // sameState()
    // result = elements[front]
    public static Object element() {
        assert size > 0;
        return elements[front];
    }


    // Pred:
    // size > 0
    // ----
    // Post:
    // front' = front + 1
    // size' = size - 1
    // immutable(front', front + size)
    // elements'[front] = null
    // result = elements[front]
    public static Object dequeue() {
        Object result = element();
        elements[front] = null;
        front = move(front, 1);
        size--;
        return result;
    }


    // Pred: -
    // ----
    // Post:
    // sameState()
    // result = size
    public static int size() {
        return size;
    }


    // Pred: -
    // ----
    // Post:
    // sameState()
    // result = true if size = 0 else false
    public static boolean isEmpty() {
        return size == 0;
    }


    // Pred: -
    // ----
    // Post: -->
    public static void clear() {
        elements = new Object[2];
        front = 0;
        size = 0;
    }


    private static int iterate(Predicate<Object> predicate, int start, int end, int step, int index) {
        while (start != end) {
            if (predicate.test(elements[start])) {
                return index;
            }
            start = move(start, step);
            index += step;
        }
        return -1;
    }


    // Pred:
    // predicate != null
    // ----
    // Post:
    // sameState()
    // predicateFalse(front, result - 1) && predicate.test(elements[result]) = true ||
    // predicateFalse(front, front + size - 1) && result = -1 ||
    // size = 0 && result = -1
    public static int indexIf(Predicate<Object> predicate) {
        return iterate(predicate, front, move(front, size), 1, 0);
    }


    // Pred:
    // predicate != null
    // ----
    // Post:
    // sameState()
    // predicateFalse(front + size - 1, result + 1) && predicate.test(elements[result]) = true ||
    // predicateFalse(front, front + size - 1) && result = -1 ||
    // size = 0 && result = -1
    public static int lastIndexIf(Predicate<Object> predicate) {
        return iterate(predicate, move(front, size - 1), move(front, -1), -1, size - 1);
    }

}
