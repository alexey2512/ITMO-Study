package queue;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractQueue implements Queue {

    protected int size;

    @Override
    public void enqueue(Object element) {
        Objects.requireNonNull(element);
        size++;
        customEnqueue(element);
    }

    protected abstract void customEnqueue(Object element);


    @Override
    public Object element() {
        assert size > 0;
        return customElement();
    }

    protected abstract Object customElement();


    @Override
    public Object dequeue() {
        Object result = element();
        size--;
        customDequeue();
        return result;
    }

    protected abstract void customDequeue();


    @Override
    public int size() {
        return size;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        size = 0;
        customClear();
    }

    protected abstract void customClear();

    @Override
    public void distinct() {
        Set<Object> set = new HashSet<>();
        int sz = size;
        for (int i = 0; i < sz; i++) {
            Object element = dequeue();
            if (!set.contains(element)) {
                set.add(element);
                enqueue(element);
            }
        }
    }

}
