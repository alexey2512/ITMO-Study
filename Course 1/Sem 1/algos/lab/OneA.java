import java.util.Scanner;

public class OneA {

    public static int[] mergeSort(int[] q) {
        int len = q.length;
        if (len == 1) {
            return q;
        } else {
            int[] left = new int[len / 2];
            int[] right = new int[len - len / 2];
            System.arraycopy(q, 0, left, 0, len / 2);
            System.arraycopy(q, len / 2, right, 0, len - len / 2);
            int[] l = mergeSort(left);
            int[] r = mergeSort(right);
            return merge(r, l);
        }
    }

    public static int[] merge(int[] a, int[] b) {
        int n = a.length, m = b.length;
        int i = 0, j = 0;
        int[] c = new int[n + m];
        while (i < n || j < m) {
            if ((j == m) || (i < n && a[i] < b[j])) {
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
        int[] mas = new int[n];

        for (int i = 0; i < n; i++) {
            mas[i] = sc.nextInt();
        }
        sc.close();

        int[] sorted_mas = mergeSort(mas);
        for (int i = 0; i < n; i++) {
            System.out.print(sorted_mas[i] + " ");
        }

    }
}