import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class TwoE {

    public static final int LENGTH = 1 << 21;
    public static final int MATCH = 1000000;
    public static final int[] maximums = new int[2 * LENGTH - 1];
    public static final int[] indexes = new int[2 * LENGTH - 1];
    public static final int[] rem = new int[2 * LENGTH - 1];
    public static int result;
    public static int index;

    public static void get(int ql, int qr) {
        result = Integer.MIN_VALUE;
        index = 0;
        get(0, 0, LENGTH, ql, qr);
    }

    public static void get(int v, int l, int r, int ql, int qr) {
        if (r <= ql || qr <= l || v > 2 * LENGTH - 2) {
            return;
        }
        if (ql <= l && r <= qr) {
            if (maximums[v] > result) {
                result = maximums[v];
                index = indexes[v];
            }
            return;
        }
        push(v);
        int m = (l + r) / 2;
        get(2 * v + 1, l, m, ql, qr);
        get(2 * v + 2, m, r, ql, qr);
    }

    public static void change(int ql, int qr, int d) {
        change(0, 0, LENGTH, ql, qr, d);
    }

    public static void change(int v, int l, int r, int ql, int qr, int d) {
        if (r <= ql || qr <= l || v > 2 * LENGTH - 2) {
            return;
        }
        if (ql <= l && r <= qr) {
            maximums[v] += d;
            rem[v] += d;
            return;
        }
        push(v);
        int m = (l + r) / 2;
        change(2 * v + 1, l, m, ql, qr, d);
        change(2 * v + 2, m, r, ql, qr, d);
        update(v);
    }

    public static void update(int v) {
        int ls = 2 * v + 1;
        int rs = ls + 1;
        if (maximums[ls] >= maximums[rs]) {
            maximums[v] = maximums[ls];
            indexes[v] = indexes[ls];
        } else {
            maximums[v] = maximums[rs];
            indexes[v] = indexes[rs];
        }
    }

    public static void push(int v) {
        maximums[2 * v + 1] += rem[v];
        maximums[2 * v + 2] += rem[v];
        rem[2 * v + 1] += rem[v];
        rem[2 * v + 2] += rem[v];
        rem[v] = 0;
    }


    public static void main(String[] args) throws IOException {
        for (int i = LENGTH - 1; i < 2 * LENGTH - 1; i++) {
            indexes[i] = i - LENGTH + 1;
        }
        for (int i = LENGTH - 2; i >= 0; i--) {
            indexes[i] = indexes[2 * i + 1];
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int[][] events = new int[2 * n][4]; // 0 - x, 1 - y_, 2 - y^, 3 - type
        for (int i = 0; i < n; i++) {
            int[] temp = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            events[2 * i] = new int[]{temp[0], temp[1], temp[3], 1};
            events[2 * i + 1] = new int[]{temp[2], temp[1], temp[3], -1};
        }
        Arrays.sort(events, (o1, o2) -> {
            if (o1[0] == o2[0]) {
                return o2[3] - o1[3];
            }
            return o1[0] - o2[0];
        });
        int max = Integer.MIN_VALUE;
        int x = 0, y = 0;
        for (int i = 0; i < 2 * n; i++) {
            if (events[i][3] == 1) {
                change(events[i][1] + MATCH, events[i][2] + MATCH + 1, events[i][3]);
                get(0, LENGTH);
                if (result > max) {
                    max = result;
                    x = events[i][0];
                    y = index - MATCH;
                }
            } else {
                get(0, LENGTH);
                if (result > max) {
                    max = result;
                    x = events[i][0];
                    y = index - MATCH;
                }
                change(events[i][1] + MATCH, events[i][2] + MATCH + 1, events[i][3]);
            }
        }
        System.out.println(max);
        System.out.println(x + " " + y);
    }


}
