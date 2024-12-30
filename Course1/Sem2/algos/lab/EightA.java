import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EightA {

    public static int n;
    public static int m;
    public static int[] p;
    public static int[] d;
    public static List<List<Integer>> childs;
    public static int[][] up;
    public static final int LOG = 20;

    public static int lca(int u, int v) {
        if (d[u] < d[v]) {
            int t = u;
            u = v;
            v = t;
        }
        for (int k = up[0].length - 1; k >= 0; k--) {
            int _u = up[u][k];
            if (d[_u] >= d[v])
                u = _u;
        }
        if (u == v)
            return u;
        for (int k = up[0].length - 1; k >= 0; k--) {
            int _u = up[u][k];
            int _v = up[v][k];
            if (_u != _v) {
                u = _u;
                v = _v;
            }
        }
        return p[u];
    }

    public static void depths(int v, int lvl) {
        d[v] = lvl;
        for (int x : childs.get(v))
            depths(x, lvl + 1);
    }

    public static void makeUp() {
        up = new int[n][LOG];
        for (int i = 0; i < n; i++)
            up[i][0] = p[i];
        for (int k = 1; k < LOG; k++)
            for (int i = 0; i < n; i++)
                up[i][k] = up[up[i][k - 1]][k - 1];
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        if (n <= 1) {
            System.out.println(0);
            return;
        }
        m = sc.nextInt();

        p = new int[n];
        childs = new ArrayList<>();
        for (int i = 0; i < n; i++)
            childs.add(new ArrayList<>());
        for (int i = 1; i < n; i++) {
            int c = sc.nextInt();
            p[i] = c;
            childs.get(c).add(i);
        }

        d = new int[n];
        depths(0, 0);
        makeUp();

        int[] a = new int[2 * m + 1];
        a[1] = sc.nextInt();
        a[2] = sc.nextInt();
        long x = sc.nextInt();
        long y = sc.nextInt();
        long z = sc.nextInt();
        for (int i = 3; i <= 2 * m; i++)
            a[i] = (int) ((x * a[i - 2] + y * a[i - 1] + z) % (long) n);

        int u, v;
        long sum = 0;
        int ans = 0;
        for (int i = 1; i <= m; i++) {
            u = (a[2 * i - 1] + ans) % n;
            v = a[2 * i];
            ans = lca(u, v);
            sum += ans;
        }
        System.out.println(sum);
    }
}
