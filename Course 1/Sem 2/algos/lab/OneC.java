import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OneC {

    public static int maxZero;
    public static int maxPrefix;
    public static int maxSuffix;
    public static int size;

    public static int length;
    public static int[] maxZeros;
    public static int[] maxPrefixes;
    public static int[] maxSuffixes;
    public static int[] sizes;

    public static void get(int ql, int qr) {
        maxZero = 0;
        maxPrefix = 0;
        maxSuffix = 0;
        size = 0;
        get(0, 0, length, ql, qr);
    }

    public static void get(int v, int l, int r, int ql, int qr) {
        if (l >= qr || r <= ql || v > 2 * length - 2) {
            return;
        } else if (ql <= l && r <= qr) {
            maxZero = max(maxZero, maxZeros[v], maxSuffix + maxPrefixes[v]);
            maxPrefix = maxPrefix == size ? size + maxPrefixes[v] : maxPrefix;
            maxSuffix = maxSuffixes[v] == sizes[v] ? maxSuffix + sizes[v] : maxSuffixes[v];
            size += sizes[v];
            return;
        }
        int m = (l + r) / 2;
        get(2 * v + 1, l, m, ql, qr);
        get(2 * v + 2, m, r, ql, qr);
    }

    public static int max(int... numbers) {
        int mx = Integer.MIN_VALUE;
        for (int el: numbers) {
            mx = Math.max(mx, el);
        }
        return mx;
    }

    public static void update(int index) {
        int ls = 2 * index + 1;
        int rs = ls + 1;
        maxPrefixes[index] = maxPrefixes[ls] == sizes[ls] ? sizes[ls] + maxPrefixes[rs] : maxPrefixes[ls];
        maxSuffixes[index] = maxSuffixes[rs] == sizes[rs] ? maxSuffixes[ls] + sizes[rs] : maxSuffixes[rs];
        maxZeros[index] = max(maxZeros[ls], maxZeros[rs], maxSuffixes[ls] + maxPrefixes[rs]);
        sizes[index] = sizes[ls] + sizes[rs];
    }

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int n = Integer.parseInt(br.readLine());
            length = (int) Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));
            maxZeros = new int[2 * length - 1];
            maxPrefixes = new int[2 * length - 1];
            maxSuffixes = new int[2 * length - 1];
            sizes = new int[2 * length - 1];

            String[] mas = br.readLine().split(" ");
            for (int i = 0; i < n; i++) {
                int x = Integer.parseInt(mas[i]);
                int value = x == 0 ? 1 : 0;
                maxZeros[i + length - 1] = value;
                maxPrefixes[i + length - 1] = value;
                maxSuffixes[i + length - 1] = value;
            }

            for (int i = length - 1; i < 2 * length - 1; i++) {
                sizes[i] = 1;
            }

            for (int index = length - 2; index >= 0; index--) {
                update(index);
            }

            int k = Integer.parseInt(br.readLine());
            for (int i = 0; i < k; i++) {
                String[] query = br.readLine().split(" ");
                switch (query[0]) {
                    case "UPDATE":
                        int index = Integer.parseInt(query[1]) + length - 2;
                        int x = Integer.parseInt(query[2]);
                        int value = x == 0 ? 1 : 0;
                        maxZeros[index] = value;
                        maxPrefixes[index] = value;
                        maxSuffixes[index] = value;
                        while (index != 0) {
                            int father = (index - 1) / 2;
                            update(father);
                            index = father;
                        }
                        break;
                    case "QUERY":
                        int l = Integer.parseInt(query[1]) - 1;
                        int r = Integer.parseInt(query[2]);
                        get(l, r);
                        System.out.println(maxZero);
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
