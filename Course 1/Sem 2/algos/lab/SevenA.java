import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SevenA {

    private static class MyHashSet {
        private static final int INITIAL_CAPACITY = 16;
        private static final float LOAD_FACTOR = 0.75f;
        private Node[] buckets;
        private int size;

        private static class Node {
            int key;
            Node next;

            Node(int key, Node next) {
                this.key = key;
                this.next = next;
            }
        }

        public MyHashSet() {
            buckets = new Node[INITIAL_CAPACITY];
            size = 0;
        }

        private int getBucketIndex(int key) {
            return Math.abs(Integer.hashCode(key)) % buckets.length;
        }

        public void add(int key) {
            if (contains(key)) {
                return;
            }
            if (size + 1 > buckets.length * LOAD_FACTOR) {
                resize();
            }
            int bucketIndex = getBucketIndex(key);
            buckets[bucketIndex] = new Node(key, buckets[bucketIndex]);
            size++;
        }

        public void remove(int key) {
            int bucketIndex = getBucketIndex(key);
            Node current = buckets[bucketIndex];
            Node prev = null;
            while (current != null) {
                if (current.key == key) {
                    if (prev == null) {
                        buckets[bucketIndex] = current.next;
                    } else {
                        prev.next = current.next;
                    }
                    size--;
                    return;
                }
                prev = current;
                current = current.next;
            }
        }

        public boolean contains(int key) {
            int bucketIndex = getBucketIndex(key);
            Node current = buckets[bucketIndex];
            while (current != null) {
                if (current.key == key) {
                    return true;
                }
                current = current.next;
            }
            return false;
        }

        private void resize() {
            Node[] oldBuckets = buckets;
            buckets = new Node[oldBuckets.length * 2];
            size = 0;
            for (Node node : oldBuckets) {
                Node current = node;
                while (current != null) {
                    add(current.key);
                    current = current.next;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        MyHashSet set = new MyHashSet();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s;
        while ((s = br.readLine()) != null) {
            String[] q = s.split(" ");
            int x = Integer.parseInt(q[1]);
            switch (q[0]) {
                case "insert":
                    set.add(x);
                    break;
                case "delete":
                    set.remove(x);
                    break;
                case "exists":
                    System.out.println(set.contains(x));
                    break;
            }
        }
    }
}