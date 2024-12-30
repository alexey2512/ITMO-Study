import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class SixF {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int n = Integer.parseInt(br.readLine());
            int[] a = new int[n];
            int[] dp = new int[n + 1];
            int[] last = new int[n + 1];
            int[] pre = new int[n + 1];
            int max, l, r, m;
            dp[0] = Integer.MAX_VALUE;
            for (int i = 1; i < n + 1; i++) {
                dp[i] = Integer.MIN_VALUE;
            }
            for (int i = 0; i < n; i++) {
                a[i] = Integer.parseInt(br.readLine());
                l = 0;
                r = i + 1;
                while (r - l > 1) {
                    m = (l + r) / 2;
                    if (dp[m] >= a[i]) {
                        l = m;
                    } else {
                        r = m;
                    }
                }
                dp[r] = a[i];
                last[r] = i;
                pre[i] = last[l];
            }
            max = n;
            while (dp[max] == Integer.MIN_VALUE) max--;
            int[] ans = new int[max];
            int k = last[max];
            for (int i = max; i > 0; i--) {
                ans[i - 1] = k + 1;
                k = pre[k];
            }
            System.out.println(max);
            for (int num : ans) {
                System.out.print(num + " ");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
