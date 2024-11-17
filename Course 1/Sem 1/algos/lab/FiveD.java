import java.util.Scanner;

public class FiveD {

    private static long max;

    private static int root(int x, int[] parents) {
        if (parents[x] == x) {
            return x;
        }
        parents[x] = root(parents[x], parents);
        return parents[x];
    }

    private static void unite(int a, int b, int[] parents, int[] ranks, long[] sums) {
        a = root(a, parents);
        b = root(b, parents);
        if (a != b) {
            if (ranks[a] > ranks[b]) {
                parents[b] = a;
                sums[a] += sums[b];
            } else if (ranks[a] < ranks[b]) {
                parents[a] = b;
                sums[b] += sums[a];
            } else {
                parents[b] = a;
                sums[a] += sums[b];
                ranks[a]++;
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        int[] perm = new int[n];
        for (int i = 0; i < n; i++) {
            perm[i] = sc.nextInt() - 1;
        }
        boolean[] ex = new boolean[n];
        int[] parents = new int[n];
        int[] ranks = new int[n];
        long[] sums = new long[n];
        long[] ans = new long[n];
        max = 0;
        for (int i = n - 1; i >= 0; i--) {
            ans[i] = max;
            ex[perm[i]] = true;
            parents[perm[i]] = perm[i];
            sums[perm[i]] = arr[perm[i]];
            if (perm[i] != 0 && ex[perm[i] - 1]) {
                unite(perm[i], perm[i] - 1, parents, ranks, sums);
            }
            if (perm[i] != n - 1 && ex[perm[i] + 1]) {
                unite(perm[i], perm[i] + 1, parents, ranks, sums);
            }
            max = Math.max(sums[root(perm[i], parents)], max);
        }
        for (int i = 0; i < n; i++) {
            System.out.println(ans[i]);
        }
    }
}
