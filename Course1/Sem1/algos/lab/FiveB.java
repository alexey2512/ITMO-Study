import java.util.*;

public class FiveB {

    public static int root(int x, int[] parents) {
        if (x == parents[x]) {
            return x;
        }
        parents[x] = root(parents[x], parents);
        return parents[x];
    }

    public static void unite(int a, int b, int[] parents, int[] ranks) {
        a = root(a, parents);
        b = root(b, parents);
        if (a != b) {
            if (ranks[a] > ranks[b]) {
                parents[b] = a;
            } else if (ranks[a] < ranks[b]) {
                parents[a] = b;
            } else {
                parents[b] = a;
                ranks[a]++;
            }
        }
    }

    public static boolean check(int a, int b, int[] parents) {
        return root(a, parents) == root(b, parents);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(); //кол-во вершин
        int m = sc.nextInt(); //кол-во ребер
        int k = sc.nextInt(); //кол-во операций
        int cut = 0; //кол-во разрезаний
        int[][] queries = new int[k][3]; // 0 - ask
                                         // 1 - cut
        Map<Integer, HashSet<Integer>> edges = new HashMap<>();
        for (int i = 0; i < m; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            edges.getOrDefault(x, new HashSet<>()).add(y);
            edges.getOrDefault(y, new HashSet<>()).add(x);
        }
        for (int i = 0; i < k; i++) {
            queries[i][0] = (sc.next().equals("cut")) ? 1 : 0;
            int x = sc.nextInt();
            int y = sc.nextInt();
            queries[i][1] = x;
            queries[i][2] = y;
            if (queries[i][0] == 1) {
                edges.getOrDefault(x, new HashSet<>()).remove(y);
                edges.getOrDefault(y, new HashSet<>()).remove(x);
                cut++;
            }
        }
        int[] parents = new int[n + 1];
        int[] ranks = new int[n + 1];
        for (int i = 0; i < n + 1; i++) {
            parents[i] = i;
        }
        for (Integer x: edges.keySet()) {
            for (Integer y: edges.get(x)) {
                unite(x, y, parents, ranks);
            }
        }
        boolean[] ans = new boolean[k - cut];
        int last = k - cut - 1;
        for (int i = k - 1; i > -1; i--) {
            if (queries[i][0] == 1) {
                unite(queries[i][1], queries[i][2], parents, ranks);
            } else {
                ans[last] = check(queries[i][1], queries[i][2], parents);
                last--;
            }
        }
        for (int i = 0; i < k - cut; i++) {
            System.out.println(ans[i] ? "YES" : "NO");
        }
    }
}
