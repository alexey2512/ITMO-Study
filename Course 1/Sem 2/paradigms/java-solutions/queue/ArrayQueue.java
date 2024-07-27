package queue;

import java.util.function.Predicate;

// Model: elements[front]...elements[front + size - 1]
// Invariant: for i = front...top - 1: elements[i] != null

// let immutable(f, t): for i = f...t - 1: elements'[i] = elements[i]
// let sameState(): immutable(front, front + size) && front' = front && size' = size
// let predicateFalse(f, t): for i = f...t: predicate.test(elements[i]) = false

public class ArrayQueue extends AbstractQueue {

    private Object[] elements;
    private int front;


    public ArrayQueue() {
        clear();
    }


    private void ensureCapacity() {
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


    private int move(int index, int step) {
        return (index + step + elements.length) % elements.length;
    }


    @Override
    public void customEnqueue(Object element) {
        elements[move(front, size - 1)] = element;
        if (size == elements.length) {
            ensureCapacity();
        }
    }


    @Override
    public Object customElement() {
        return elements[front];
    }

    @Override
    public void customDequeue() {
        elements[front] = null;
        front = move(front, 1);
    }

    @Override
    public void customClear() {
        elements = new Object[2];
        front = 0;
    }

    private int iterate(Predicate<Object> predicate, int start, int end, int step, int index) {
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
    public int indexIf(Predicate<Object> predicate) {
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
    public int lastIndexIf(Predicate<Object> predicate) {
        return iterate(predicate, move(front, size - 1), move(front, -1), -1, size - 1);
    }

}
