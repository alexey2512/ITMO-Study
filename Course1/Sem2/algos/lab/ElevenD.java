import java.io.*;
import java.util.*;

public class ElevenD {

    public static int length;
    public static int sum;
    public static int[] sums;

    public static int get(int ql, int qr) {
        sum = 0;
        get(0, 0, length, ql, qr);
        return sum;
    }

    public static void get(int v, int l, int r, int ql, int qr) {
        if (r <= ql || l >= qr || v > 2 * length - 2) {
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
            int leftSon = 2 * x + 1;
            int rightSon = leftSon + 1;
            sums[x] = sums[leftSon] + sums[rightSon];
        } while (x != 0);
    }


    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        length = (int) Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));
        int[] numbers = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        Map<Integer, Integer> map = new HashMap<>();
        int[] indexesOfNext = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            int number = numbers[i];
            if (!map.containsKey(number)) {
                indexesOfNext[i] = i;
                map.put(number, i);
            } else {
                indexesOfNext[i] = map.get(number);
                map.put(number, i);
            }
        }
        sums = new int[2 * length - 1];
        for (Integer index: map.values()) {
            sums[index + length - 1] = 1;
        }
        for (int i = length - 2; i >= 0; i--) {
            int leftSon = 2 * i + 1;
            int rightSon = leftSon + 1;
            sums[i] = sums[leftSon] + sums[rightSon];
        }
        int q = Integer.parseInt(br.readLine());
        int[][] queries = new int[q + 1][3];
        queries[0][0] = 0;
        queries[0][1] = 0;
        queries[0][2] = -1;
        for (int i = 1; i <= q; i++) {
            String[] mas = br.readLine().split(" ");
            queries[i][0] = Integer.parseInt(mas[0]) - 1;
            queries[i][1] = Integer.parseInt(mas[1]);
            queries[i][2] = i - 1;
        }
        Arrays.sort(queries, Comparator.comparingInt(o -> o[0]));
        int[] results = new int[q];
        for (int i = 1; i <= q; i++) {
            for (int j = queries[i - 1][0]; j < queries[i][0]; j++) {
                int indexOfNext = indexesOfNext[j] + length - 1;
                sums[indexOfNext] = 1;
                update(indexOfNext);
            }
            results[queries[i][2]] = get(queries[i][0], queries[i][1]);
        }
        for (int i = 0; i < q; i++) {
            System.out.println(results[i]);
        }
    }
}
