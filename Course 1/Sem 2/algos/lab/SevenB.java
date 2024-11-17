import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SevenB {

    private static class MyHashMap {

        private static final int INITIAL_CAPACITY = 4;
        private static final float LOAD_FACTOR = 0.75f;
        private Node[] blockHeads;
        private Node front;
        private Node back;
        private int size;

        public MyHashMap() {
            blockHeads = new Node[INITIAL_CAPACITY];
            front = null;
            back = null;
            size = 0;
        }

        private int getIndex(String key) {
            return Math.abs(key.hashCode() % blockHeads.length);
        }

        public String state() {
            StringBuilder sb = new StringBuilder();
            if (front != null)
                sb.append("front: (").append(front.key).append(", ").append(front.value).append(")\n");
            else
                sb.append("front = null");
            if (back != null)
                sb.append("back: (").append(back.key).append(", ").append(back.value).append(")\n");
            else
                sb.append("back = null");
            int i = 0;
            for (Node head : blockHeads) {
                sb.append(i).append(": ");
                StringBuilder tmp = new StringBuilder();
                Node cur = head;
                while (cur != null) {
                    String a = "(" + cur.key + ", " + cur.value + ")";
                    tmp.insert(0, a);
                    cur = cur.blockPrev;
                    if (cur != null)
                        tmp.insert(0, " ");
                 }
                sb.append(tmp).append("\n");
                i++;
            }
            return sb.toString();
        }

        private Node find(String key) {
            int index = getIndex(key);
            Node cur = blockHeads[index];
            while (cur != null) {
                if (cur.key.equals(key))
                    return cur;
                cur = cur.blockPrev;
            }
            return null;
        }

        private void resize() {
            Node[] old = blockHeads;
            blockHeads = new Node[old.length * 2];
            size = 0;
            Node cur = front;
            front = null;
            back = null;
            while (cur != null) {
                add(cur.key, cur.value);
                cur = cur.iterNext;
            }
        }

        private void deleteFromOrder(Node node) {
            Node prev = node.iterPrev;
            Node next = node.iterNext;
            if (prev != null)
                prev.iterNext = next;
            else
                front = next;
            if (next != null)
                next.iterPrev = prev;
            else
                back = prev;
        }

        private void addToOrder(Node node) {
            if (back != null)
                back.iterNext = node;
            node.iterPrev = back;
            node.iterNext = null;
            back = node;
            if (front == null)
                front = node;
        }

        private void deleteFromBlock(Node node) {
            Node prev = node.blockPrev;
            Node next = node.blockNext;
            int index = getIndex(node.key);
            if (prev != null)
                prev.blockNext = next;
            if (next != null)
                next.blockPrev = prev;
            else
                blockHeads[index] = prev;
        }

        private void addToBlock(Node node) {
            int index = getIndex(node.key);
            node.blockPrev = blockHeads[index];
            if (blockHeads[index] != null)
                blockHeads[index].blockNext = node;
            blockHeads[index] = node;
        }

        public void add(String key, String value) {
            Node existingNode = find(key);
            if (existingNode != null) {
                existingNode.value = value;
                return;
            }
            if (size + 1 > blockHeads.length * LOAD_FACTOR)
                resize();
            Node node = new Node(key, value);
            addToOrder(node);
            addToBlock(node);
            size++;
        }

        public void remove(String key) {
            Node node = find(key);
            if (node == null)
                return;
            deleteFromOrder(node);
            deleteFromBlock(node);
            size--;
        }

        public String get(String key) {
            Node node = find(key);
            return node == null ? "none" : node.value;
        }

        public String prev(String key) {
            Node node = find(key);
            if (node == null || node.iterPrev == null)
                return "none";
            return node.iterPrev.value;
        }

        public String next(String key) {
            Node node = find(key);
            if (node == null || node.iterNext == null)
                return "none";
            return node.iterNext.value;
        }

        private static class Node {
            String key;
            String value;
            Node blockPrev;
            Node blockNext;
            Node iterPrev;
            Node iterNext;

            Node(String key, String value) {
                this.key = key;
                this.value = value;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        MyHashMap map = new MyHashMap();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s;
        while ((s = br.readLine()) != null) {
            String[] q = s.split(" ");
            switch (q[0]) {
                case "put":
                    map.add(q[1], q[2]);
                    break;
                case "delete":
                    map.remove(q[1]);
                    break;
                case "get":
                    System.out.println(map.get(q[1]));
                    break;
                case "prev":
                    System.out.println(map.prev(q[1]));
                    break;
                case "next":
                    System.out.println(map.next(q[1]));
                    break;
            }
            //System.out.println(map.state());
        }
    }
}
