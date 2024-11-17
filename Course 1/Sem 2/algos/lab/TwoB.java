import java.io.*;
import java.util.Arrays;

public class TwoB {

    public static int length;
    public static final int POWER = 21;
    public static int[][] tree;
    public static int[] rem;
    public static int[] result;

    public static long get(int ql, int qr) {
        result = new int[POWER];
        get(0, 0, length, ql, qr);
        long x = 0;
        for (int i = 0; i < POWER; i++) {
            x += (long) result[i] * (1 << i);
        }
        return x;
    }

    public static void get(int v, int l, int r, int ql, int qr) {
        if (r <= ql || qr <= l || v > 2 * length - 2) {
            return;
        }
        if (ql <= l && r <= qr) {
            addTo(result, tree[v]);
            return;
        }
        push(v, (r - l) / 2);
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
            compose(tree[v], convert(d), r - l);
            rem[v] ^= d;
            return;
        }
        push(v, (r - l) / 2);
        int m = (l + r) / 2;
        change(2 * v + 1, l, m, ql, qr, d);
        change(2 * v + 2, m, r, ql, qr, d);
        tree[v] = add(tree[2 * v + 1], tree[2 * v + 2]);
    }

    public static void push(int v, int len) {
        int[] t = convert(rem[v]);
        compose(tree[2 * v + 1], t, len);
        compose(tree[2 * v + 2], t, len);
        rem[2 * v + 1] ^= rem[v];
        rem[2 * v + 2] ^= rem[v];
        rem[v] = 0;
    }

    public static int[] convert(int number) {
        int[] result = new int[POWER];
        for (int i = 0; i < POWER; i++) {
            result[i] = number % 2;
            number /= 2;
        }
        return result;
    }

    public static int[] add(int[] a, int[] b) {
        int[] result = new int[POWER];
        for (int i = 0; i < POWER; i++) {
            result[i] = a[i] + b[i];
        }
        return result;
    }

    public static void addTo(int[] a, int[] b) {
        for (int i = 0; i < POWER; i++) {
            a[i] += b[i];
        }
    }

    public static void compose(int[] a, int[] num, int len) {
        for (int i = 0; i < POWER; i++) {
            if (num[i] == 1) {
                a[i] = len - a[i];
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        length = (int) Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));
        tree = new int[2 * length - 1][];
        rem = new int[2 * length - 1];
        int[] array = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        for (int i = 0; i < n; i++) {
            tree[i + length - 1] = convert(array[i]);
        }
        for (int i = n + length - 1; i < 2 * length - 1; i++) {
            tree[i] = new int[POWER];
        }
        for (int i = length - 2; i >= 0; i--) {
            tree[i] = add(tree[2 * i + 1], tree[2 * i + 2]);
        }
        int m = Integer.parseInt(br.readLine());
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        for (int i = 0; i < m; i++) {
            int[] query = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            switch (query[0]) {
                case 1:
                    bw.write(get(query[1] - 1, query[2]) + "\n");
                    break;
                case 2:
                    change(query[1] - 1, query[2], query[3]);
                    break;
            }
        }
        bw.close();
    }
}
