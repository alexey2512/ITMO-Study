import java.io.*;

public class EightC {

    public static int n;
    public static int m;
    public static int log;
    public static int[][] up;
    public static int[][] min;
    public static int[] p;
    public static int[] d;
    public static int[] edges;

    public static int lca(int u, int v) {
        int curMin = Integer.MAX_VALUE;
        if (d[u] < d[v]) {
            int t = u;
            u = v;
            v = t;
        }
        for (int k = log - 1; k >= 0; k--) {
            int _u = up[u][k];
            if (d[_u] >= d[v]) {
                curMin = Math.min(curMin, min[u][k]);
                u = _u;
            }
        }
        if (u == v)
            return curMin;
        for (int k = log - 1; k >= 0; k--) {
            int _u = up[u][k];
            int _v = up[v][k];
            if (_u != _v) {
                curMin = Math.min(curMin, min[u][k]);
                curMin = Math.min(curMin, min[v][k]);
                u = _u;
                v = _v;
            }
        }
        return Math.min(curMin, Math.min(min[u][0], min[v][0]));
    }

    public static void make() {
        for (int i = 0; i < n; i++) {
            up[i][0] = p[i];
            min[i][0] = edges[i];
        }
        for (int k = 1; k < log; k++)
            for (int i = 0; i < n; i++) {
                int mid = up[i][k - 1];
                up[i][k] = up[mid][k - 1];
                min[i][k] = Math.min(min[i][k - 1], min[mid][k - 1]);
            }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        n = Integer.parseInt(br.readLine());
        log = (int) (Math.ceil(Math.log(n) / Math.log(2))) + 1;

        p = new int[n];
        d = new int[n];
        edges = new int[n];
        up = new int[n][log];
        min = new int[n][log];
        p[0] = 0;
        d[0] = 0;
        edges[0] = Integer.MAX_VALUE;
        for (int i = 1; i < n; i++) {
            String[] s = br.readLine().split(" ");
            int anc = Integer.parseInt(s[0]) - 1;
            p[i] = anc;
            d[i] = d[anc] + 1;
            edges[i] = Integer.parseInt(s[1]);
        }
        make();

        m = Integer.parseInt(br.readLine());
        for (int i = 0; i < m; i++) {
            String[] s = br.readLine().split(" ");
            int u = Integer.parseInt(s[0]) - 1;
            int v = Integer.parseInt(s[1]) - 1;
            bw.write(lca(u, v) + "\n");
        }
        bw.close();
    }
}
