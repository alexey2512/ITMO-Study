import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SevenB {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int m = sc.nextInt();

        int[] weights = new int[n];
        int[] costs = new int[n];
        for (int i = 0; i < n; i++) {
            weights[i] = sc.nextInt();
        }
        for (int i = 0; i < n; i++) {
            costs[i] = sc.nextInt();
        }

        int[][] dp = new int[n + 1][m + 1];
        for (int j = 1; j <= m; j++) {
            dp[0][j] = -1;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j - weights[i - 1] >= 0 && dp[i - 1][j - weights[i - 1]] != -1) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - weights[i - 1]] + costs[i - 1]);
                }
            }
        }
        int sum = -1;
        int index = -1;
        for (int j = m; j >= 0; j--) {
            if (dp[n][j] > sum) {
                sum = dp[n][j];
                index = j;
            }
        }
        int j = index;
        List<Integer> ans = new ArrayList<>();
        for (int i = n; i >= 1; i--) {
            if (j - weights[i - 1] >= 0 && dp[i][j] == dp[i - 1][j - weights[i - 1]] + costs[i - 1]) {
                ans.add(0, i);
                j -= weights[i - 1];
            }
        }
        System.out.println(ans.size());
        for (Integer integer : ans) {
            System.out.print(integer + " ");
        }
    }
}
