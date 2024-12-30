import java.util.Scanner;

public class SixD {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
        }
        int m = sc.nextInt();
        int[] b = new int[m];
        for (int i = 0; i < m; i++) {
            b[i] = sc.nextInt();
        }
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < m + 1; j++) {
                if (a[i - 1] == b[j - 1]) {
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        int max = dp[n][m];
        System.out.println(max);
        int[] ans = new int[max];
        int last = max - 1;
        int i = n, j = m;
        while (i > 0 && j > 0) {
            if (a[i - 1] == b[j - 1]) {
                ans[last] = a[i - 1];
                i--;
                j--;
                last--;
            } else {
                if (dp[i][j] == dp[i - 1][j]) {
                    i--;
                } else {
                    j--;
                }
            }
        }
        for (int c = 0; c < max; c++) {
            System.out.print(ans[c] + " ");
        }
    }
}
