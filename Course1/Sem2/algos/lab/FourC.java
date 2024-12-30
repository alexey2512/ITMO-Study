import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FourC {

    private static class Node {
        int value;
        int priority;
        int min;
        int size = 1;
        boolean reverse = false;
        Node left = null;
        Node right = null;
        Node(int value, int priority) {
            this.value = value;
            this.priority = priority;
            this.min = value;
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
    private static final Random rand = new Random();
    private static final int range = 1 << 20;

    private static void update(Node node) {
        if (node == null) return;
        node.size = 1 +
                (node.left == null ? 0 : node.left.size) +
                (node.right == null ? 0 : node.right.size);
        node.min = Math.min(
                node.value,
                Math.min(
                        (node.left == null ? Integer.MAX_VALUE : node.left.min),
                        (node.right == null ? Integer.MAX_VALUE : node.right.min)
                )
        );
    }

    private static void push(Node node) {
        if (node == null || !node.reverse) return;
        Node tmp = node.left;
        node.left = node.right;
        node.right = tmp;
        node.reverse = false;
        if (node.left != null) node.left.reverse = !node.left.reverse;
        if (node.right != null) node.right.reverse = !node.right.reverse;
    }

    private static Node merge(Node a, Node b) {
        if (a == null) return b;
        if (b == null) return a;
        if (a.priority > b.priority) {
            push(a);
            a.right = merge(a.right, b);
            update(a);
            return a;
        } else {
            push(b);
            b.left = merge(a, b.left);
            update(b);
            return b;
        }
    }

    private static Pair split(Node tree, int k) {
        if (tree == null) return new Pair(null, null);
        if (k == 0) return new Pair(null, tree);
        if (tree.size <= k) return new Pair(tree, null);
        push(tree);
        update(tree);
        if (tree.left != null && k <= tree.left.size) {
            Pair p = split(tree.left, k);
            tree.left = p.second;
            update(tree);
            return new Pair(p.first, tree);
        } else {
            Pair p = split(tree.right, k - (tree.left == null ? 0 : tree.left.size) - 1);
            tree.right = p.first;
            update(tree);
            return new Pair(tree, p.second);
        }
    }

    private static void build(int n, List<Integer> list) {
        int h = (int) Math.ceil(Math.log(n) / Math.log(2));
        root = build(list, h);
    }

    private static Node build(List<Integer> list, int h) {
        if (list.isEmpty()) return null;
        if (h == 0) {
            Node node = new Node(list.get(0), rand.nextInt(0, range));
            list.remove(0);
            return node;
        }
        Node left = build(list, h - 1);
        update(left);
        if (list.isEmpty()) return left;
        Node parent = new Node(list.get(0), rand.nextInt(range * h, range * (h + 1)));
        list.remove(0);
        Node right = build(list, h - 1);
        update(right);
        parent.left = left;
        parent.right = right;
        update(parent);
        return parent;
    }

    private static String toString(Node node) {
        StringBuilder sb = new StringBuilder();
        toString(node, sb);
        return sb.toString();
    }

    private static void toString(Node node, StringBuilder sb) {
        if (node == null) return;
        toString(node.left, sb);
        sb.append(node.value).append(" ");
        toString(node.right, sb);
    }


    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        String[] nm = br.readLine().split(" ");
        int n = Integer.parseInt(nm[0]);
        int m = Integer.parseInt(nm[1]);
        int[] arr = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        List<Integer> list = new ArrayList<>();
        for (int x : arr) list.add(x);
        build(n, list);
        Pair p1;
        Node A, B, C, AB;
        for (int i = 0; i < m; i++) {
            int[] query = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            switch (query[0]) {
                case 1:
                    p1 = split(root, query[1] - 1);
                    A = p1.first;
                    p1 = split(p1.second, query[2] - query[1] + 1);
                    B = p1.first;
                    C = p1.second;
                    B.reverse = !B.reverse;
                    AB = merge(A, B);
                    root = merge(AB, C);
                    break;
                case 2:
                    p1 = split(root, query[1] - 1);
                    A = p1.first;
                    p1 = split(p1.second, query[2] - query[1] + 1);
                    B = p1.first;
                    C = p1.second;
                    bw.write(B.min + "\n");
                    AB = merge(A, B);
                    root = merge(AB, C);
                    break;
            }
        }
        bw.close();
    }
}
