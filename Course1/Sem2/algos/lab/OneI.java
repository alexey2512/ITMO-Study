import java.io.*;
import java.util.Arrays;

public class OneI {

    public static long[] treeFront;
    public static long[] treeBack;
    public static int length;
    public static long res;

    public static long getFront(int ql, int qr) {
        res = 0;
        getFront(0, 0, length, ql, qr);
        return res;
    }

    public static void getFront(int v, int l, int r, int ql, int qr) {
        if (l >= qr || r <= ql || v > 2 * length - 2) {
            return;
        }
        if (ql <= l && r <= qr) {
            res = mergeFront(res, treeFront[v]);
            return;
        }
        int m = (l + r) / 2;
        getFront(2 * v + 1, l, m, ql, qr);
        getFront(2 * v + 2, m, r, ql, qr);
    }

    public static long getBack(int ql, int qr) {
        res = 0;
        getBack(0, 0, length, ql, qr);
        return res;
    }

    public static void getBack(int v, int l, int r, int ql, int qr) {
        if (l >= qr || r <= ql || v > 2 * length - 2) {
            return;
        }
        if (ql <= l && r <= qr) {
            res = mergeBack(res, treeBack[v]);
            return;
        }
        int m = (l + r) / 2;
        getBack(2 * v + 1, l, m, ql, qr);
        getBack(2 * v + 2, m, r, ql, qr);
    }

    public static boolean isValidFront(long a) {
        return a <= 1;
    }

    public static boolean isValidBack(long a) {
        return a >= -1;
    }

    public static long mergeFront(long a, long b) {
        return isValidFront(a) && isValidFront(b) ? 0 : 2;
    }

    public static long mergeBack(long a, long b) {
        return isValidBack(a) && isValidBack(b) ? 0 : -2;
    }

    public static void updateFront(int index) {
        while (index != 0) {
            index = (index - 1) / 2;
            treeFront[index] = mergeFront(treeFront[2 * index + 1], treeFront[2 * index + 2]);
        }
    }

    public static void updateBack(int index) {
        while (index != 0) {
            index = (index - 1) / 2;
            treeBack[index] = mergeBack(treeBack[2 * index + 1], treeBack[2 * index + 2]);
        }
    }


    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] nm = br.readLine().split(" ");
        int n = Integer.parseInt(nm[0]) - 1;
        length = (int) Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));
        treeFront = new long[2 * length - 1];
        treeBack = new long[2 * length - 1];
        int m = Integer.parseInt(nm[1]);
        long[] tubes = Arrays.stream(br.readLine().split(" ")).mapToLong(Long::parseLong).toArray();
        for (int i = 0; i < n; i++) {
            long t = tubes[i + 1] - tubes[i];
            treeFront[i + length - 1] = t;
            treeBack[i + length - 1] = t;
        }
        for (int i = length - 2; i >= 0; i--) {
            int l = 2 * i + 1;
            int r = l + 1;
            treeFront[i] = mergeFront(treeFront[l], treeFront[r]);
            treeBack[i] = mergeBack(treeBack[l], treeBack[r]);
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        for (int i = 0; i < m; i++) {
            int[] query = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            switch (query[0]) {
                case 1:
                    int qL = query[1] - 1;
                    int qR = query[2] - 1;
                    if (qL < qR) {
                        bw.write((isValidFront(getFront(qL, qR)) ? "YES" : "NO") + "\n");
                    } else {
                        bw.write((isValidBack(getBack(qR, qL)) ? "YES" : "NO") + "\n");
                    }
                    break;
                case 2:
                    int ql = query[1] + length - 3;
                    int qr = query[2] + length - 2;
                    long d = query[3];
                    if (ql >= length - 1) {
                        treeFront[ql] += d;
                        updateFront(ql);
                        treeBack[ql] += d;
                        updateBack(ql);
                    }
                    if (qr < 2 * length - 1) {
                        treeFront[qr] -= d;
                        updateFront(qr);
                        treeBack[qr] -= d;
                        updateBack(qr);
                    }
                    break;
            }
        }
        bw.close();
    }

}
