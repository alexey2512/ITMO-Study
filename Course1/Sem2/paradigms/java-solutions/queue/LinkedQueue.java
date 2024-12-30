package queue;

public class LinkedQueue extends AbstractQueue {

    private Node top;
    private Node front;

    public LinkedQueue() {
        clear();
    }

    @Override
    public void customEnqueue(Object element) {
        Node newNode = new Node(element, null);
        top.next = newNode;
        top = newNode;
        front = size == 1 ? top : front;
    }

    @Override
    public Object customElement() {
        return front.element;
    }

    @Override
    public void customDequeue() {
        front = front.next;
    }

    @Override
    public void customClear() {
        front = null;
        top = new Node(null, null);
    }

    private static class Node {
        private final Object element;
        private Node next;

        private Node(Object element, Node next) {
            this.element = element;
            this.next = next;
        }
    }

}
