import java.util.Scanner;
import java.lang.Math;

public class I {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int[] X = new int[n];
        int[] Y = new int[n];
        int[] H = new int[n];
        int xl = Integer.MAX_VALUE,
            xr = Integer.MIN_VALUE,
            yl = Integer.MAX_VALUE,
            yr = Integer.MIN_VALUE;

        for (int i = 0; i < n; i++) {
            X[i] = sc.nextInt();
            Y[i] = sc.nextInt();
            H[i] = sc.nextInt();
        }

        sc.close();

        for (int i = 0; i < n; i++) {
            xl = Math.min(xl, X[i] - H[i]);
            xr = Math.max(xr, X[i] + H[i]);
            yl = Math.min(yl, Y[i] - H[i]);
            yr = Math.max(yr, Y[i] + H[i]);
        }

        System.out.print(
                (xl + xr) / 2 + " " +
                (yl + yr) / 2 + " " +
                (int) Math.ceil((double) Math.max(xr - xl, yr - yl) / 2)
        );
    }
}
