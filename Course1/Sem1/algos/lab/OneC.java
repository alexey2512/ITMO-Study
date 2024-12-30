import java.util.Scanner;

public class OneC {

    public static long count = 0;
    public static long k;

    public static long[] mergeSort(long[] q) {
        int len = q.length;
        if (len == 1) {
            return q;
        } else {
            long[] left = new long[len / 2];
            long[] right = new long[len - len / 2];
            System.arraycopy(q, 0, left, 0, len / 2);
            System.arraycopy(q, len / 2, right, 0, len - len / 2);
            long[] l = mergeSort(left);
            long[] r = mergeSort(right);
            return merge(l , r);
        }
    }

    public static long[] merge(long[] a, long[] b) {
        int n = a.length, m = b.length;
        int i = 0, j = 0;
        while (i < n && a[i] + k <= b[m - 1]) {
            while (j < m && a[i] + k > b[j]) {
                j++;
            }
            count += m - j;
            i++;
        }
        i = 0;
        j = 0;
        long[] c = new long[n + m];
        while (i < n || j < m) {
            if ((j == m) || (i < n && a[i] <= b[j])) {
                c[i + j] = a[i];
                i++;
            } else {
                c[i + j] = b[j];
                j++;
            }
        }
        return c;
    }
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        k = sc.nextLong();
        long[] mas = new long[n + 1];

        mas[0] = 0;
        for (int i = 1; i < n + 1; i++) {
            mas[i] = sc.nextLong() + mas[i - 1];
        }
        sc.close();

        mergeSort(mas);
        System.out.println(count);
    }
}
