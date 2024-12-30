import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class SixA {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] f = br.readLine().split(" ");
        int n = Integer.parseInt(f[0]);
        int m = Integer.parseInt(f[1]);
        int height = (int) Math.floor(Math.log(n) / Math.log(2)) + 1;
        int[][] sparse = new int[height][n];
        sparse[0][0] = Integer.parseInt(f[2]);
        for (int i = 1; i < n; i++) {
            sparse[0][i] = (23 * sparse[0][i - 1] + 21563) % 16714589;
        }
        for (int h = 1; h < height; h++) {
            for (int i = 0; i <= n - (1 << h); i++) {
                sparse[h][i] = Math.min(sparse[h - 1][i], sparse[h - 1][i + (1 << (h - 1))]);
            }
        }
        int[] logs = new int[n + 1];
        int[] lens = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            logs[i] = (int) Math.floor(Math.log(i) / Math.log(2));
            lens[i] = (int) Math.pow(2, logs[i]);
        }
        String[] uv = br.readLine().split(" ");
        int u = Integer.parseInt(uv[0]);
        int v = Integer.parseInt(uv[1]);
        int ans = 0;
        for (int i = 1; i <= m; i++) {
            if (u == v) {
                ans = sparse[0][u - 1];
            } else if (u < v) {
                int l = v - u + 1;
                int log = logs[l];
                int len = lens[l];
                ans = Math.min(sparse[log][u - 1], sparse[log][v - len]);
            } else {
                int l = u - v + 1;
                int log = logs[l];
                int len = lens[l];
                ans = Math.min(sparse[log][v - 1], sparse[log][u - len]);
            }
            if (i == m) {
                System.out.print(u + " " + v + " ");
            }
            u = (17 * u + 751 + ans + 2 * i) % n + 1;
            v = (13 * v + 593 + ans + 5 * i) % n + 1;
        }
        System.out.println(ans);
    }
}
