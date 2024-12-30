import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;

public class OneG {

    public static int length;
    public static long sum;
    public static long[] locals;
    public static long[] globals;

    public static long get(long[] tree, int ql, int qr) {
        sum = 0;
        get(tree, 0, 0, length, ql, qr);
        return sum;
    }

    public static void get(long[] tree, int v, int l, int r, int ql, int qr) {
        if (qr <= l || r <= ql || v > 2 * length - 2) {
            return;
        } else if (ql <= l && r <= qr) {
            sum += tree[v];
            return;
        }
        int m = (l + r) / 2;
        get(tree, 2 * v + 1, l, m, ql, qr);
        get(tree, 2 * v + 2, m, r, ql, qr);
    }

    public static void update(long[] tree, int x) {
        do {
            x = (x - 1) / 2;
            tree[x] = tree[2 * x + 1] + tree[2 * x + 2];
        } while (x != 0);
    }

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int n = Integer.parseInt(br.readLine());
            String[] mas = br.readLine().split(" ");
            long[][] array = new long[n][2];
            for (int i = 0; i < n; i++) {
                array[i][0] = Long.parseLong(mas[i]);
                array[i][1] = i;
            }
            Arrays.sort(array, (o1, o2) -> Math.toIntExact(o1[0] == o2[0] ? o1[1] - o2[1] : o1[0] - o2[0]));

            length = (int) Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));
            locals = new long[2 * length - 1];
            globals = new long[2 * length - 1];
            long tripleInversions = 0;

            for (int i = n - 1; i >= 0; i--) {
                int index = (int) array[i][1];
                tripleInversions += get(globals, 0, index);
                locals[index + length - 1] = 1;
                update(locals, index + length - 1);
                globals[index + length - 1] = get(locals, 0, index);
                update(globals, index + length - 1);
            }
            System.out.println(tripleInversions);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
