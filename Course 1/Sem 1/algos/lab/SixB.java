import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SixB {
    public static void main(String[] args) {

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            String[] tmp = br.readLine().split(" ");
            int N = Integer.parseInt(tmp[0]);
            int M = Integer.parseInt(tmp[1]);

            int[][] desk = new int[N][M];
            for (int i = 0; i < N; i++) {
                tmp = br.readLine().split(" ");
                for (int j = 0; j < M; j++) {
                    desk[i][j] = Integer.parseInt(tmp[j]);
                }
            }

            int[][] dp = new int[N][M];
            dp[0][0] = desk[0][0];
            for (int i = 1; i < N; i++) {
                dp[i][0] = dp[i - 1][0] + desk[i][0];
            }
            for (int i = 1; i < M; i++) {
                dp[0][i] = dp[0][i - 1] + desk[0][i];
            }

            for (int i = 1; i < N; i++) {
                for (int j = 1; j < M; j++) {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]) + desk[i][j];
                }
            }

            StringBuilder sb = new StringBuilder();
            System.out.println(dp[N - 1][M - 1]);
            int n = N - 1;
            int m = M - 1;
            for (int i = 0; i < N + M - 2; i++) {
                if (n == 0) {
                    sb.append("R");
                    m--;
                } else if (m == 0) {
                    sb.append("D");
                    n--;
                } else {
                    int value = dp[n][m] - desk[n][m];
                    if (value == dp[n - 1][m]) {
                        sb.append("D");
                        n--;
                    } else {
                        sb.append("R");
                        m--;
                    }
                }
            }
            System.out.println(sb.reverse());

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
