import java.util.Scanner;

public class FiveH {

    private static int n;

    public static int root(int x, int[] parents) {
        if (parents[x] == x) {
            return x;
        }
        parents[x] = root(parents[x], parents);
        return parents[x];
    }

    public static void unite(int a, int b, int[] parents, int[] ranks, int[] maxes, boolean[] used) {
        a = root(a, parents);
        b = root(b, parents);
        if (a != b) {
            if (used[(maxes[b] + 1) % n] && check(a, (maxes[b] + 1) % n, parents)) {
                if (ranks[a] > ranks[b]) {
                    parents[b] = a;
                } else if (ranks[a] < ranks[b]) {
                    maxes[b] = maxes[a];
                    parents[a] = b;
                } else {
                    parents[b] = a;
                    ranks[a]++;
                }
            } else {
                if (ranks[a] > ranks[b]) {
                    maxes[a] = maxes[b];
                    parents[b] = a;
                } else if (ranks[a] < ranks[b]) {
                    parents[a] = b;
                } else {
                    parents[a] = b;
                    ranks[b]++;
                }
            }
        }
    }

    public static boolean check(int b, int a, int[] parents) {
        return root(b, parents) == root(a, parents);
    }

    public static int[] fast(int[] arr, int[] parents, int[] ranks, int[] maxes, boolean[] used) {
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            int t = arr[i] - 1;
            if (used[t]) {
                int x = root(t, parents);
                t = (maxes[x] + 1) % n;
            }
            ans[i] = t + 1;
            used[t] = true;
            parents[t] = t;
            maxes[t] = t;
            if (used[(t + 1) % n]) {
                unite(t, (t + 1) % n, parents, ranks, maxes, used);
            }
            if (used[(t - 1 + n) % n]) {
                unite(t, (t - 1 + n) % n, parents, ranks, maxes, used);
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        int[] array = new int[n];
        int[] parents = new int[n];
        int[] ranks = new int[n];
        int[] maxes = new int[n];
        boolean[] used = new boolean[n];
        for (int i = 0; i < n; i++) {
            array[i] = sc.nextInt();
        }
        int[] answer = fast(array, parents, ranks, maxes, used);
        for (int el : answer) {
            System.out.print(el + " ");
        }
    }
}
