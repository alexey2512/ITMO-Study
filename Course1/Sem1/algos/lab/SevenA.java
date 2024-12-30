import java.util.Scanner;

public class SevenA {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int N = sc.nextInt();
        int M = sc.nextInt();
        int[] w = new int[N];
        for (int i = 0; i < N; i++) {
            w[i] = sc.nextInt();
        }

        boolean[][] dp = new boolean[N + 1][M + 1];
        for (int i = 0; i < N + 1; i++) {
            dp[i][0] = true;
        }
        for (int i = 1; i < M + 1; i++) {
            dp[0][i] = false;
        }

        for (int i = 1; i < N + 1; i++) {
            for (int j = 1; j < M + 1; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j - w[i - 1] >= 0) {
                    dp[i][j] |= dp[i - 1][j - w[i - 1]];
                }
            }
        }

        int result = M;
        while (!dp[N][result]) result--;
        System.out.println(result);

    }
}
