import java.util.Scanner;

public class SixC {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] mas = new int[n];
        int[] dp = new int[n];
        int[] parents = new int[n];
        int index = 0, max = 0;
        for (int i = 0; i < n; i++) {
            mas[i] = sc.nextInt();
            dp[i] = 1;
            parents[i] = i;
            for (int j = 0; j < i; j++) {
                if (mas[j] < mas[i]) {
                    if (dp[j] + 1 > dp[i]) {
                        dp[i] = dp[j] + 1;
                        parents[i] = j;
                    }
                }
            }
            if (dp[i] >= max) {
                max = dp[i];
                index = i;
            }
        }
        System.out.println(max);
        int[] ans = new int[max];
        int last = max - 1;
        do {
            ans[last] = mas[index];
            index = parents[index];
            last--;
        } while (parents[index] != index);
        ans[last] = mas[index];
        for (int i = 0; i < max; i++) {
            System.out.print(ans[i] + " ");
        }
    }
}
