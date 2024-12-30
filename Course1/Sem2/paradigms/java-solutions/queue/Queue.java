package queue;

// Model: elements[front]...elements[front + size - 1]
// Invariant: for i = front...top - 1: elements[i] != null

// let immutable(f, t): for i = f...t - 1: elements'[i] = elements[i]
// let sameState(): immutable(front, front + size) && front' = front && size' = size

public interface Queue {


    // Pred:
    // element != null
    // ----
    // Post:
    // front' = front
    // size' = size + 1
    // immutable(front, front + size)
    // elements'[front + size] = element
    void enqueue(Object element);


    // Pred:
    // size > 0
    // ----
    // Post:
    // sameState()
    // result = elements[front]
    Object element();


    // Pred:
    // size > 0
    // ----
    // Post:
    // front' = front + 1
    // size' = size - 1
    // immutable(front', front + size)
    // elements'[front] = null
    // result = elements[front]
    Object dequeue();


    // Pred: -
    // ----
    // Post:
    // sameState()
    // result = size
    int size();


    // Pred: -
    // ----
    // Post:
    // sameState()
    // result = true if size = 0 else false
    boolean isEmpty();


    // Pred: -
    // ----
    // Post:
    // front' = 0
    // size' = 0
    void clear();

    // Pred: -
    // ----
    // Post:
    // let set = saveOrderSet(elements)
    // elements' = set
    // front' = front
    // size' = len(set)
    void distinct();

}
