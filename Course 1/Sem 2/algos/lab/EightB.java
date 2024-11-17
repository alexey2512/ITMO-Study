import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EightB {

    public static int n;
    public static int m;
    public static long[] values;
    public static List<List<Integer>> neighbors;
    public static int[] p;
    public static int[] d;
    public static int[][] up;
    public static long[][] sums;
    public static List<List<Integer>> childs;
    public static int log;

    public static long lca(int u, int v) {
        long sum = 0;
        if (d[u] < d[v]) {
            int t = u;
            u = v;
            v = t;
        }
        for (int k = log - 1; k >= 0; k--) {
            int _u = up[u][k];
            if (d[_u] > d[v]) {
                sum += sums[u][k];
                u = _u;
            }
        }
        if (d[u] > d[v]) {
            sum += values[u];
            u = p[u];
        }
        if (u == v)
            return sum + values[u];
        for (int k = log - 1; k >= 0; k--) {
            int _u = up[u][k];
            int _v = up[v][k];
            if (_u != _v) {
                sum += sums[u][k];
                sum += sums[v][k];
                u = _u;
                v = _v;
            }
        }
        return sum + values[u] + values[v] + values[p[u]];
    }

    public static void hang(int v) {
        for (int x : neighbors.get(v)) {
            if (x != p[v]) {
                childs.get(v).add(x);
                p[x] = v;
                hang(x);
            }
        }
    }

    public static void depths(int v, int lvl) {
        d[v] = lvl;
        for (int x : childs.get(v))
            depths(x, lvl + 1);
    }

    public static void make() {
        for (int i = 0; i < n; i++) {
            up[i][0] = p[i];
            sums[i][0] = values[i];
        }
        for (int k = 1; k < log; k++)
            for (int i = 0; i < n; i++) {
                int mid = up[i][k - 1];
                up[i][k] = up[mid][k - 1];
                sums[i][k] = sums[i][k - 1] + sums[mid][k - 1];
            }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        n = Integer.parseInt(br.readLine());
        log = (int) (Math.ceil(Math.log(n) / Math.log(2))) + 1;
        values = Arrays.stream(br.readLine().split(" ")).mapToLong(Long::parseLong).toArray();

        neighbors = new ArrayList<>();
        for (int i = 0; i < n; i++)
            neighbors.add(new ArrayList<>());
        childs = new ArrayList<>();
        for (int i = 0; i < n; i++)
            childs.add(new ArrayList<>());

        for (int i = 0; i < n - 1; i++) {
            String[] ky = br.readLine().split(" ");
            int v = Integer.parseInt(ky[0]) - 1;
            int u = Integer.parseInt(ky[1]) - 1;
            neighbors.get(v).add(u);
            neighbors.get(u).add(v);
        }

        p = new int[n];
        p[0] = 0;
        hang(0);
        d = new int[n];
        d[0] = 0;
        depths(0, 0);
        up = new int[n][log];
        sums = new long[n][log];
        make();

        m = Integer.parseInt(br.readLine());
        for (int i = 0; i < m; i++) {
            String[] ky = br.readLine().split(" ");
            int v = Integer.parseInt(ky[0]) - 1;
            int u = Integer.parseInt(ky[1]) - 1;
            bw.write(Long.valueOf(lca(u, v)).toString() + "\n");
        }
        bw.close();
    }
}
