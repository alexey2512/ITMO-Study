import java.io.*;
import java.util.Arrays;

public class TwoA {

    public static int length;
    public static int[] tree;
    public static int[] rem;
    public static int result;

    public static int get(int ql, int qr) {
        result = Integer.MIN_VALUE;
        get(0, 0, length, ql, qr);
        return result;
    }

    public static void get(int v, int l, int r, int ql, int qr) {
        if (r <= ql || qr <= l || v > 2 * length - 2) {
            return;
        }
        if (ql <= l && r <= qr) {
            result = Math.max(result, tree[v]);
            return;
        }
        push(v);
        int m = (l + r) / 2;
        get(2 * v + 1, l, m, ql, qr);
        get(2 * v + 2, m, r, ql, qr);
    }

    public static void change(int ql, int qr, int d) {
        change(0, 0, length, ql, qr, d);
    }

    public static void change(int v, int l, int r, int ql, int qr, int d) {
        if (r <= ql || qr <= l || v > 2 * length - 2) {
            return;
        }
        if (ql <= l && r <= qr) {
            tree[v] += d;
            rem[v] += d;
            return;
        }
        push(v);
        int m = (l + r) / 2;
        change(2 * v + 1, l, m, ql, qr, d);
        change(2 * v + 2, m, r, ql, qr, d);
        tree[v] = Math.max(tree[2 * v + 1], tree[2 * v + 2]);
    }

    public static void push(int v) {
        tree[2 * v + 1] += rem[v];
        tree[2 * v + 2] += rem[v];
        rem[2 * v + 1] += rem[v];
        rem[2 * v + 2] += rem[v];
        rem[v] = 0;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int[] array = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        length = (int) Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));
        tree = new int[2 * length - 1];
        rem = new int[2 * length - 1];
        System.arraycopy(array, 0, tree, length - 1, n);
        for (int i = n + length - 1; i < 2 * length - 1; i++) {
            tree[i] = Integer.MIN_VALUE;
        }
        for (int i = length - 2; i >= 0; i--) {
            tree[i] = Math.max(tree[i * 2 + 1], tree[i * 2 + 2]);
        }
        int m = Integer.parseInt(br.readLine());
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        for (int i = 0; i < m; i++) {
            String[] query = br.readLine().split(" ");
            switch (query[0]) {
                case "m":
                    bw.write(get(Integer.parseInt(query[1]) - 1, Integer.parseInt(query[2])) + " ");
                    break;
                case "a":
                    change(Integer.parseInt(query[1]) - 1, Integer.parseInt(query[2]), Integer.parseInt(query[3]));
                    break;
            }
        }
        bw.write("\n");
        bw.close();
    }
}
