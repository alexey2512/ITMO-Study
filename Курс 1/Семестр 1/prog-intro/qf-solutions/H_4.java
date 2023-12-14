import java.util.Scanner;
import java.lang.Math;

public class H {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt(), k;
        int max = 0, sum = 0;
        int[] prefs = new int[n];
        int[] f = new int[1000000];

        for (int i = 0; i < n; i++) {
            k = sc.nextInt();
            prefs[i] = sum;
            for (int j = 0; j < k; j++) {
                f[sum + j] = i;
            }
            sum += k;
            max = Math.max(max, k);
        }

        int q = sc.nextInt();
        int t, count, b;
        int[] ans = new int[1000000];

        for (int i = 0; i < q; i++) {

            t = sc.nextInt();

            if (t < max) {
                System.out.println("Impossible");
                continue;
            }

            if (ans[t] != 0) {
                System.out.println(ans[t]);
                continue;
            }

            count = 0;
            b = 0;
            while (b < n - 1 && prefs[b] + t < sum) {
                b = f[prefs[b] + t];
                count++;
            }

            ans[t] = count + 1;
            System.out.println(ans[t]);
        }
    }
}
