import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OneE {

    public static int answer;
    public static boolean found;
    public static int[] maximums;
    public static int length;

    public static void getK(int ql, int qr, int x) {
        answer = Integer.MIN_VALUE;
        found = false;
        getK(0, 0, length, ql, qr, x);
    }

    public static void getK(int v, int l, int r, int ql, int qr, int x) {
        if (found) {
            return;
        }
        if (r <= ql || l >= qr || v > 2 * length - 2) {
            return;
        } else if (ql <= l && r <= qr) {
            if (maximums[v] >= x) {
                answer = sift(v, x);
                found = true;
            }
            return;
        }
        int m = (l + r) / 2;
        getK(2 * v + 1, l, m, ql, qr, x);
        getK(2 * v + 2, m, r, ql, qr, x);
    }

    public static int sift(int v, int x) {
        while (v < length - 1) {
            int left_son = 2 * v + 1;
            int right_son = 2 * v + 2;
            if (maximums[left_son] >= x) {
                v = left_son;
            } else {
                v = right_son;
            }
        }
        return v - length + 2;
    }

    public static void update(int index) {
        maximums[index] = Math.max(maximums[2 * index + 1], maximums[2 * index + 2]);
    }

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String[] nm = br.readLine().split(" ");
            int n = Integer.parseInt(nm[0]);
            int m = Integer.parseInt(nm[1]);
            length = (int) Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));
            maximums = new int[2 * length - 1];

            String[] arr = br.readLine().split(" ");
            for (int i = 0; i < n; i++) {
                maximums[i + length - 1] = Integer.parseInt(arr[i]);
            }
            for (int i = n + length - 1; i < 2 * length - 1; i++) {
                maximums[i] = Integer.MIN_VALUE;
            }

            for (int i = length - 2; i >= 0; i--) {
                update(i);
            }

            for (int i = 0; i < m; i++) {
                String[] query = br.readLine().split(" ");
                switch (query[0]) {
                    case "0":
                        int index = Integer.parseInt(query[1]) + length - 2;
                        int value = Integer.parseInt(query[2]);
                        maximums[index] = value;
                        while (index != 0) {
                            int father = (index - 1) / 2;
                            update(father);
                            index = father;
                        }
                        break;
                    case "1":
                        int left = Integer.parseInt(query[1]) - 1;
                        int inf = Integer.parseInt(query[2]);
                        getK(left, length, inf);
                        if (found) {
                            System.out.println(answer);
                        } else {
                            System.out.println(-1);
                        }
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
