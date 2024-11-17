import java.io.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class FourA {

    private static class Node {
        long key;
        long prior;
        long sum;
        Node left = null;
        Node right = null;
        Node(long key, long prior) {
            this.key = key;
            this.prior = prior;
            this.sum = key;
        }
    }

    private static class Pair {
        Node first;
        Node second;
        Pair(Node first, Node second) {
            this.first = first;
            this.second = second;
        }
    }

    private static Node root = null;
    private static long sum;
    private static long max = Long.MIN_VALUE;
    private static long min = Long.MAX_VALUE;
    private static final Random rand = new Random();
    private static long lastQuery = Long.MIN_VALUE;
    private static final Set<Long> set = new HashSet<>();

    private static void update(Node a) {
        if (a == null) return;
        a.sum = a.key + (a.left == null ? 0 : a.left.sum) + (a.right == null ? 0 : a.right.sum);
    }

    private static Node merge(Node a, Node b) {
        update(a);
        update(b);
        if (a == null) return b;
        if (b == null) return a;
        if (a.prior > b.prior) {
            a.right = merge(a.right, b);
            update(a);
            return a;
        } else {
            b.left = merge(a, b.left);
            update(a);
            return b;
        }
    }

    private static Pair split(Node a, long key) {
        if (a == null) return new Pair(null, null);
        if (a.key >= key) {
            Pair p = split(a.left, key);
            a.left = p.second;
            update(a);
            return new Pair(p.first, a);
        } else {
            Pair p = split(a.right, key);
            a.right = p.first;
            update(a);
            return new Pair(a, p.second);
        }
    }

    private static void insert(long key) {
        if (set.contains(key)) return;
        set.add(key);
        Node node = new Node(key, rand.nextLong());
        root = insert(root, node);
        max = Math.max(max, key);
        min = Math.min(min, key);
    }

    private static Node insert(Node a, Node node) {
        if (a == null) return node;
        if (node.prior > a.prior) {
            Pair p = split(a, node.key);
            node.left = p.first;
            node.right = p.second;
            update(node);
            return node;
        } else {
            if (node.key < a.key)
                a.left = insert(a.left, node);
            else
                a.right = insert(a.right, node);
            update(a);
            return a;
        }
    }

    private static long get(long ql, long qr) {
        sum = 0;
        get(root, min, max + 1, ql, qr);
        return sum;
    }

    private static void get(Node a, long l, long r, long ql, long qr) {
        if (a == null || r <= ql || qr <= l) return;
        if (ql <= l && r <= qr) {
            sum += a.sum;
            return;
        }
        if (ql <= a.key && a.key < qr)
            sum += a.key;
        get(a.left, l, a.key, ql, qr);
        get(a.right, a.key, r, ql, qr);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        int n = Integer.parseInt(br.readLine());
        for (int i = 0; i < n; i++) {
            String[] query = br.readLine().split(" ");
            switch (query[0]) {
                case "+":
                    if (lastQuery != Long.MIN_VALUE) {
                        insert((Long.parseLong(query[1]) + lastQuery) % 1000000000);
                        lastQuery = Long.MIN_VALUE;
                    } else {
                        insert(Long.parseLong(query[1]));
                    }
                    break;
                case "?":
                    lastQuery = get(Long.parseLong(query[1]), Long.parseLong(query[2]) + 1);
                    bw.write(lastQuery + "\n");
                    break;
            }
        }
        bw.close();
    }
}
