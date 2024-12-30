import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OneB {

    public static int value;
    public static int count;
    public static int length;
    public static int[] values;
    public static int[] counts;

    public static void get(int ql, int qr) {
        value = Integer.MIN_VALUE;
        count = 1;
        get(0, 0, length, ql, qr);
    }

    public static void get(int v, int l, int r, int ql, int qr) {
        if (l >= qr || r <= ql || v > 2 * length - 1) {
            return;
        } else if (ql <= l && r <= qr) {
            if (values[v] > value) {
                value = values[v];
                count = counts[v];
            } else if (values[v] == value) {
                count += counts[v];
            }
            return;
        }
        int m = (l + r) / 2;
        get(2 * v + 1, l, m, ql, qr);
        get(2 * v + 2, m, r, ql, qr);
    }

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int n = Integer.parseInt(br.readLine());
            length = (int) Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));
            values = new int[2 * length - 1];
            counts = new int[2 * length - 1];

            for (int i = length - 1; i < 2 * length - 1; i++) {
                counts[i] = 1;
            }

            String[] mas = br.readLine().split(" ");
            for (int i = length - 1; i < length - 1 + n; i++) {
                values[i] = Integer.parseInt(mas[i - length + 1]);
            }
            for (int i = length - 1 + n; i < 2 * length - 1; i++) {
                values[i] = Integer.MIN_VALUE;
            }

            for (int i = length - 2; i >= 0; i--) {
                int l = 2 * i + 1;
                int r = l + 1;
                if (values[l] > values[r]) {
                    values[i] = values[l];
                    counts[i] = counts[l];
                } else if (values[l] < values[r]) {
                    values[i] = values[r];
                    counts[i] = counts[r];
                } else {
                    values[i] = values[l];
                    counts[i] = counts[l] + counts[r];
                }
            }

            int k = Integer.parseInt(br.readLine());
            for (int i = 0; i < k; i++) {
                String[] lr = br.readLine().split(" ");
                int ql = Integer.parseInt(lr[0]) - 1;
                int qr = Integer.parseInt(lr[1]);
                get(ql, qr);
                System.out.println(value + " " + count);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
