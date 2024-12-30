import java.util.Scanner;

public class SixG {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String string = sc.next();
        int len = string.length();
        int n = sc.nextInt();
        boolean f = true;
        for (int i = 0; i < len - 1; i++) {
            if (string.charAt(i + 1) - string.charAt(i) == 1) {
                f = false;
                break;
            }
        }
        if (f) {
            int[] dp = new int[26];
            int sum;
            dp[string.charAt(len - 1) - 97] = 1;
            for (int i = 0; i < n - len; i++) {
                sum = 0;
                int[] dp1 = new int[26];
                for (int j = 0; j < 26; j++) {
                    sum = (sum + dp[j]) % 998244353;
                }
                dp1[0] = sum % 998244353;
                for (int j = 1; j < 26; j++) {
                    dp1[j] = (sum - dp[j - 1] + 998244353) % 998244353;
                }
                dp = dp1;
            }
            sum = 0;
            for (int j = 0; j < 26; j++) {
                sum = (sum + dp[j]) % 998244353;
            }
            System.out.println(sum % 998244353);
        } else {
            System.out.println(0);
        }
    }
}
