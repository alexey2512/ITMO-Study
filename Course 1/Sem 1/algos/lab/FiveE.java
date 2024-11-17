import java.util.ArrayList;
import java.util.Scanner;

public class FiveE {

    public static int root(int x, int[] parents) {
        if (x == parents[x]) {
            return x;
        }
        parents[x] = root(parents[x], parents);
        return parents[x];
    }

    public static void unite(int a, int b, int[] parents, int[] ranks, ArrayList<Integer>[] arrays) {
        a = root(a, parents);
        b = root(b, parents);
        if (a != b) {
            if (ranks[a] > ranks[b]) {
                parents[b] = a;
                arrays[a].addAll(arrays[b]);
                arrays[b].clear();
            } else if (ranks[a] < ranks[b]) {
                parents[a] = b;
                arrays[b].addAll(arrays[a]);
                arrays[a].clear();
            } else {
                parents[b] = a;
                arrays[a].addAll(arrays[b]);
                arrays[b].clear();
                ranks[a]++;
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] parents = new int[n];
        int[] ranks = new int[n];
        ArrayList<Integer>[] arrays = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            arrays[i] = new ArrayList<>();
            arrays[i].add(i);
            parents[i] = i;
        }
        for (int i = 0; i < n - 1; i++) {
            int x = sc.nextInt() - 1;
            int y = sc.nextInt() - 1;
            unite(x, y, parents, ranks, arrays);
        }
        for (int i: arrays[root(0, parents)]) {
            System.out.print((i + 1) + " ");
        }
    }
}
