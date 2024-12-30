import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FiveA {

    private static int p;

    private static int root(int x, int[] parents, int[] points) {
        p += points[x];
        if (parents[x] == x) {
            return x;
        }
        if (points[parents[x]] == 0) {
            parents[x] = parents[parents[x]];
        }
        return root(parents[x], parents, points);
    }

    private static void unite(int a, int b, int[] parents, int[] ranks, int[] points) {
        a = root(a, parents, points);
        b = root(b, parents, points);
        if (a != b) {
            if (ranks[a] > ranks[b]) {
                parents[b] = a;
                points[b] -= points[a];
            } else if (ranks[a] == ranks[b]) {
                parents[b] = a;
                ranks[a]++;
                points[b] -= points[a];
            } else {
                parents[a] = b;
                points[a] -= points[b];
            }
        }
    }

    private static void add(int x, int v, int[] parents, int[] points) {
        x = root(x, parents, points);
        points[x] += v;
    }

    private static int get(int x, int[] parents, int[] points) {
        p = 0;
        x = root(x, parents, points);
        return p;
    }

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String[] tmp = br.readLine().split(" ");
            int n = Integer.parseInt(tmp[0]);
            int m = Integer.parseInt(tmp[1]);
            int[] parents = new int[n + 1];
            int[] ranks = new int[n + 1];
            int[] points = new int[n + 1];
            for (int i = 1; i < n + 1; i++) {
                parents[i] = i;
            }
            int x, y;
            for (int i = 0; i < m; i++) {
                tmp = br.readLine().split(" ");
                switch (tmp[0]) {
                    case "join":
                        x = Integer.parseInt(tmp[1]);
                        y = Integer.parseInt(tmp[2]);
                        unite(x, y, parents, ranks, points);
                        break;
                    case "add":
                        x = Integer.parseInt(tmp[1]);
                        y = Integer.parseInt(tmp[2]);
                        add(x, y, parents, points);
                        break;
                    case "get":
                        x = Integer.parseInt(tmp[1]);
                        System.out.println(get(x, parents, points));
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
