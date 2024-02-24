import java.util.*;

class Tree {

    public final int[] crews;
    public final int[] parents;
    public final List<List<Integer>> children;
    public final int size;
    public int root;

    public Tree(int n) {

        size = n;

        crews = new int[n];

        parents = new int[n];
        for (int i = 0; i < n; i++) {
            parents[i] = -1;
        }

        children = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            children.add(new ArrayList<>());
        }

    }

    public void addEdge(int v, int u) {
        children.get(v).add(u);
        children.get(u).add(v);
    }

    public void addCrew(int v) {
        crews[v] = 1;
    }

    public void buildTree(int cur, int parent) {

        if (parent == -1) {
            root = cur;
        }

        if (children.get(cur).contains(parent)) {
            children.get(cur).remove((Integer) parent);
            parents[cur] = parent;
        }

        for (int child: children.get(cur)) {
            buildTree(child, cur);
        }

    }

    public int[] getMaxDepth(int v) {

        int[] ans = new int[2];

        for (int child: children.get(v)) {
            int[] temp = getMaxDepthFromLeaves(child);
            if (temp[0] >= ans[0]) {
                ans = temp;
            }
        }

        ans[0]++;
        return ans;
    }

    private int[] getMaxDepthFromLeaves(int v) {

        int[] ans = new int[2];
        ans[0] = Integer.MIN_VALUE;
        ans[1] = v;

        if (crews[v] == 1) {
            ans[0] = 0;
            return ans;
        }

        for (int child: children.get(v)) {
            int[] temp = getMaxDepthFromLeaves(child);
            if (temp[0] >= ans[0]) {
                ans = temp;
            }
        }

        ans[0]++;
        return ans;
    }

    public int findCityFromLeave(int leave, int depth) {

        for (int i = 0; i < depth; i++) {
            leave = parents[leave];
        }

        return leave;

    }

    public void clear() {

        for (int i = 0; i < size; i++) {
            if (parents[i] != -1) {
                children.get(i).add(parents[i]);
                parents[i] = -1;
            }
        }

    }

    public int getDepth(int v) {
        int d = 0;
        while (v != root) {
            v = parents[v];
            d++;
        }
        return d;
    }

}



public class E {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int m = sc.nextInt();
        int v, u, command;
        int[] commands = new int[m];
        Tree tree = new Tree(n);

        for (int i = 0; i < n - 1; i++) {
            v = sc.nextInt() - 1;
            u = sc.nextInt() - 1;
            tree.addEdge(v, u);
        }

        for (int i = 0; i < m; i++) {
            command = sc.nextInt() - 1;
            commands[i] = command;
            tree.addCrew(command);
        }

        sc.close();

        int c1, depth, mid, ans = 0;
        boolean isFound = false;

        for (int rootIndex = 0; rootIndex < m; rootIndex++) {

            c1 = commands[rootIndex];
            tree.clear();
            tree.buildTree(c1, -1);
            int[] dep_ver = tree.getMaxDepth(c1);
            v = dep_ver[1];
            depth = dep_ver[0];

            if (depth % 2 == 0) {

                mid = tree.findCityFromLeave(v, depth / 2);
                tree.clear();
                tree.buildTree(mid, -1);
                boolean flag = true;

                for (int i = 0; i < m; i++) {
                    if (commands[i] != mid && tree.getDepth(commands[i]) != depth / 2) {
                        flag = false;
                        break;
                    }
                }

                if (flag) {
                    isFound = true;
                    ans = mid + 1;
                    break;
                }

            }
        }

        if (isFound) {
            System.out.println("YES\n" + ans);
        } else {
            System.out.println("NO");
        }

    }
}
