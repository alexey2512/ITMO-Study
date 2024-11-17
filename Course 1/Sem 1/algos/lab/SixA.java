import java.util.Scanner;

public class SixA {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        sc.nextLine();
        String road = sc.nextLine().trim();

        int[] dp = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            dp[i] = -1;
        }
        dp[1] = 0;

        for (int i = 1; i <= n; i++) {
            if (road.charAt(i - 1) != 'w') {
                for (int j = 1; j < 6; j += 2) {
                    if (i + j <= n && dp[i] != -1) {
                        dp[i + j] = Math.max(dp[i + j], dp[i] + ((road.charAt(i + j - 1) == '"') ? 1 : 0));
                    }
                }
            }
        }

        System.out.println(dp[n]);

    }
}
