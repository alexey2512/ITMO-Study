import java.util.Arrays;
import java.util.Scanner;

public class SixE {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        char[] a = sc.next().toCharArray();
        int n = a.length;
        char[] b = sc.next().toCharArray();
        int m = b.length;
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 0; i < n + 1; i++) {
            for (int j = 0; j < m + 1; j++) {
                if (i == 0) {
                    dp[0][j] = j;
                } else if (j == 0) {
                    dp[i][0] = i;
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i][j - 1] + 1, dp[i - 1][j] + 1), dp[i - 1][j - 1] + (a[i - 1] == b[j - 1] ? 0 : 1));
                }
            }
        }
        System.out.println(dp[n][m]);
    }
}

