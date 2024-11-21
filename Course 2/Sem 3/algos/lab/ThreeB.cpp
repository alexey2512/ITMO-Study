#include <iostream>
#include <vector>
#include <set>

using namespace std;

int n, m;
vector<vector<int>> edges;
vector<bool> mark;
vector<int> depth;
vector<int> up;
set<int> art_points;

void dfs(int v, int p, int d) {
    mark[v] = true;
    depth[v] = d;
    up[v] = d;
    int children = 0;
    for (int u : edges[v]) {
        if (u == p)
            continue;
        if (mark[u]) {
            up[v] = min(up[v], depth[u]);
        } else {
            dfs(u, v, d + 1);
            children++;
            up[v] = min(up[v], up[u]);
            if (p != v && up[u] >= depth[v])
                art_points.insert(v);
        }
    }

    if (p == v && children > 1)
        art_points.insert(v);
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    cin >> n >> m;
    edges.resize(n, vector<int>());
    for (int i = 0; i < m; i++) {
        int a, b;
        cin >> a >> b;
        a--; b--;
        edges[a].push_back(b);
        edges[b].push_back(a);
    }

    mark.resize(n, false);
    depth.resize(n, 0);
    up.resize(n, 0);

    for (int v = 0; v < n; v++)
        if (!mark[v])
            dfs(v, v, 0);

    cout << art_points.size() << endl;
    for (int v : art_points)
        cout << v + 1 << " ";
    cout << endl;

    return 0;
}

