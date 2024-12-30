import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FourB {

    private static class Node {
        int index;
        int prior;
        int size;
        Node left = null;
        Node right = null;
        Node(int index, int prior) {
            this.index = index;
            this.prior = prior;
            this.size = 1;
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

    private static void update(Node node) {
        if (node != null)
            node.size = 1 + (node.left == null ? 0 : node.left.size) + (node.right == null ? 0 : node.right.size);
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
            update(b);
            return b;
        }
    }

    private static Pair split(Node tree, int k) {
        update(tree);
        if (tree == null) return new Pair(null, null);
        if (k == 0) return new Pair(null, tree);
        if (tree.size <= k) return new Pair(tree, null);
        if (tree.left != null) {
            if (k <= tree.left.size) {
                Pair p = split(tree.left, k);
                tree.left = p.second;
                update(tree);
                return new Pair(p.first, tree);
            } else {
                Pair p = split(tree.right, k - tree.left.size - 1);
                tree.right = p.first;
                update(tree);
                return new Pair(tree, p.second);
            }
        } else {
            Pair p = split(tree.right, k - 1);
            tree.right = p.first;
            update(tree);
            return new Pair(tree, p.second);
        }
    }

    private static void build(int n) {
        int h = (int) Math.ceil(Math.log(n) / Math.log(2));
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++)
            list.add(i);
        root = build(list, h);
    }

    private static Node build(List<Integer> list, int h) {
        if (list.isEmpty()) return null;
        if (h == 0) {
            Node node = new Node(list.get(0), h);
            list.remove(0);
            return node;
        }
        Node left = build(list, h - 1);
        update(left);
        if (list.isEmpty()) return left;
        Node parent = new Node(list.get(0), h);
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
        sb.append(node.index + 1).append(" ");
        toString(node.right, sb);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        String[] mn = br.readLine().split(" ");
        int n = Integer.parseInt(mn[0]);
        int m = Integer.parseInt(mn[1]);
        build(n);
        for (int i = 0; i < m; i++) {
            String[] q = br.readLine().split(" ");
            int l = Integer.parseInt(q[0]);
            int r = Integer.parseInt(q[1]);
            Pair p1 = split(root, l - 1);
            Node A = p1.first;
            p1 = split(p1.second, r - l + 1);
            Node B = p1.first;
            Node C = p1.second;
            A = merge(B, A);
            root = merge(A, C);
        }
        bw.write(toString(root) + "\n");
        bw.close();
    }
}
