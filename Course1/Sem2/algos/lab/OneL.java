import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;

public class OneL {

    public static int length;
    public static int sum;
    public static int[] sums;

    public static int get(int ql, int qr) {
        sum = 0;
        get(0, 0, length, ql, qr);
        return sum;
    }

    public static void get(int v, int l, int r, int ql, int qr) {
        if (qr <= l || r <= ql || v > 2 * length - 2) {
            return;
        } else if (ql <= l && r <= qr) {
            sum += sums[v];
            return;
        }
        int m = (l + r) / 2;
        get(2 * v + 1, l, m, ql, qr);
        get(2 * v + 2, m, r, ql, qr);
    }

    public static void update(int x) {
        do {
            x = (x - 1) / 2;
            sums[x] = sums[2 * x + 1] + sums[2 * x + 2];
        } while (x != 0);
    }


    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int n = Integer.parseInt(br.readLine());
            String[] mas = br.readLine().split(" ");
            int[][] array = new int[n][2];
            for (int i = 0; i < n; i++) {
                array[i][0] = Integer.parseInt(mas[i]);
                array[i][1] = i;
            }
            Arrays.sort(array, Comparator.comparingInt(o -> o[0]));

            length = (int) Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));
            sums = new int[2 * length - 1];

            int inverseCount = 0;
            for (int i = 0; i < n; i++) {
                int index = array[i][1];
                inverseCount += get(index + 1, length);
                sums[index + length - 1] = 1;
                update(index + length - 1);
            }
            System.out.println(inverseCount);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
