import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OneD {

    public static int countOfZeros;

    public static int[] zeroCounts;
    public static int[] leftZeros;
    public static int length;

    public static int getCountOfZeros(int ql, int qr) {
        countOfZeros = 0;
        getCountOfZeros(0, 0, length, ql, qr);
        return countOfZeros;
    }

    public static void getCountOfZeros(int v, int l, int r, int ql, int qr) {
        if (l >= qr || r <= ql || v > 2 * length - 2) {
            return;
        } else if (ql <= l && r <= qr) {
            countOfZeros += zeroCounts[v];
            return;
        }
        int m = (l + r) / 2;
        getCountOfZeros(2 * v + 1, l, m, ql, qr);
        getCountOfZeros(2 * v + 2, m, r, ql, qr);
    }

    public static int getKZero(int k) {
        int v = 0;
        int l = 0;
        int r = length;
        while (v < length - 1) {
            int left_son = 2 * v + 1;
            int right_son = left_son + 1;
            if (k >= zeroCounts[left_son]) {
                k -= zeroCounts[left_son];
                v = right_son;
            } else {
                v = left_son;
            }
        }
        return leftZeros[v];
    }

    public static void update(int index) {
        int left_son = 2 * index + 1;
        int right_son = left_son + 1;
        zeroCounts[index] = zeroCounts[left_son] + zeroCounts[right_son];
        leftZeros[index] = Math.min(leftZeros[left_son], leftZeros[right_son]);
    }

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int n = Integer.parseInt(br.readLine());
            length = (int) Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));
            zeroCounts = new int[2 * length - 1];
            leftZeros = new int[2 * length - 1];

            String[] mas = br.readLine().split(" ");
            for (int i = 0; i < n; i++) {
                int num = Integer.parseInt(mas[i]);
                if (num == 0) {
                    zeroCounts[i + length - 1] = 1;
                    leftZeros[i + length - 1] = i;
                } else {
                    zeroCounts[i + length - 1] = 0;
                    leftZeros[i + length - 1] = Integer.MAX_VALUE;
                }
            }
            for (int i = n + length - 1; i < 2 * length - 1; i++) {
                zeroCounts[i] = 0;
                leftZeros[i] = Integer.MAX_VALUE;
            }

            for (int i = length - 2; i >= 0; i--) {
                update(i);
            }

            int m = Integer.parseInt(br.readLine());
            for (int i = 0; i < m; i++) {
                String[] q = br.readLine().split(" ");
                switch (q[0]) {
                    case "u":
                        int index = Integer.parseInt(q[1]) + length - 2;
                        int num = Integer.parseInt(q[2]);
                        zeroCounts[index] = num == 0 ? 1 : 0;
                        leftZeros[index] = num == 0 ? index - length + 1 : Integer.MAX_VALUE;
                        while (index != 0) {
                            int father = (index - 1) / 2;
                            update(father);
                            index = father;
                        }
                        break;
                    case "s":
                        int ql = Integer.parseInt(q[1]) - 1;
                        int qr = Integer.parseInt(q[2]);
                        int k = Integer.parseInt(q[3]) - 1;
                        int preZeros = ql != 0 ? getCountOfZeros(0, ql) : 0;
                        k += preZeros;
                        preZeros = getCountOfZeros(0, qr);
                        if (k >= preZeros) {
                            System.out.print(-1 + " ");
                        } else {
                            int result = getKZero(k) + 1;
                            System.out.println(result + " ");
                        }
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
