import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OneK {

    public static int length;
    public static int sum;
    public static int[] sums;

    public static int getSum(int ql, int qr) {
        sum = 0;
        getSum(0, 0, length, ql, qr);
        return sum;
    }

    public static void getSum(int v, int l, int r, int ql, int qr) {
        if (r <= ql || l >= qr || v > 2 * length - 2) {
            return;
        } else if (ql <= l && r <= qr) {
            sum += sums[v];
            return;
        }
        int m = (l + r) / 2;
        getSum(2 * v + 1, l, m, ql, qr);
        getSum(2 * v + 2, m, r, ql, qr);
    }

    public static void update(int index) {
        sums[index] = sums[2 * index + 1] + sums[2 * index + 2];
    }

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int n = Integer.parseInt(br.readLine());
            length = (int) Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));
            sums = new int[2 * length - 1];

            String[] massive = br.readLine().split(" ");
            for (int i = 0; i < n; i++) {
                int num = Integer.parseInt(massive[i]);
                sums[i + length - 1] = i % 2 == 0 ? num : -num;
            }
            for (int i = n + length - 1; i < 2 * length - 1; i++) {
                sums[i] = 0;
            }

            for (int i = length - 2; i >= 0; i--) {
                update(i);
            }

            int m = Integer.parseInt(br.readLine());
            for (int i = 0; i < m; i++) {
                String[] q = br.readLine().split(" ");
                switch (q[0]) {
                    case "0":
                        int index = Integer.parseInt(q[1]) + length - 2;
                        int value = Integer.parseInt(q[2]);
                        sums[index] = (index - length + 1) % 2 == 0 ? value : -value;
                        while (index != 0) {
                            int father = (index - 1) / 2;
                            update(father);
                            index = father;
                        }
                        break;
                    case "1":
                        int ql = Integer.parseInt(q[1]) - 1;
                        int qr = Integer.parseInt(q[2]);
                        int s = getSum(ql, qr);
                        s = ql % 2 == 0 ? s : -s;
                        System.out.println(s);
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
