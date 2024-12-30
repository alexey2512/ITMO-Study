import java.util.Scanner;

public class SevenD {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int sum = 0;
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
            sum += arr[i];
        }
        if (sum % 2 == 1) {
            System.out.println("NO");
        } else {
            boolean[][] dp = new boolean[n + 1][sum / 2 + 1];
            for (int i = 0; i < n + 1; i++) {
                dp[i][0] = true;
            }
            for (int i = 1; i < sum / 2 + 1; i++) {
                dp[0][i] = false;
            }

            for (int i = 1; i < n + 1; i++) {
                for (int j = 1; j < sum / 2 + 1; j++) {
                    dp[i][j] = dp[i - 1][j];
                    if (j - arr[i - 1] >= 0) {
                        dp[i][j] |= dp[i - 1][j - arr[i - 1]];
                    }
                }
            }

            if (dp[n][sum / 2]) {
                System.out.println("YES");
            } else {
                System.out.println("NO");
            }
        }
    }
}
