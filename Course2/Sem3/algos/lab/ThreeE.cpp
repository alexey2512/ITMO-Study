#include <iostream>
#include <vector>
#include <set>
#include <functional>

using namespace std;

int main() {
    int n, m;
    cin >> n >> m;

    vector<vector<pair<short, int>>> edges(n + 1);
    vector<set<short>> adj(n + 1);
    vector<pair<short, short>> bridges;
    vector<short> depth(n + 1, -1), up(n + 1, -1), kc(n + 1, 0);
    vector mark(n + 1, false);
    int timer = 0;

    for (int i = 1; i <= m; i++) {
        int a, b;
        cin >> a >> b;
        edges[a].emplace_back(b, i);
        edges[b].emplace_back(a, i);
        adj[a].insert(b);
        adj[b].insert(a);
    }

    function<void(short, int)> find_bridges = [&](short v, int pe) {
        mark[v] = true;
        depth[v] = up[v] = timer++;
        for (auto [u, id] : edges[v]) {
            if (id == pe) continue;
            if (!mark[u]) {
                find_bridges(u, id);
                up[v] = min(up[v], up[u]);
                if (up[u] > depth[v])
                    bridges.push_back({u, v});
            } else
                up[v] = min(up[v], depth[u]);
        }
    };

    for (short i = 1; i <= n; i++)
        if (!mark[i])
            find_bridges(i, -1);

    for (auto [u, v] : bridges) {
        adj[u].erase(v);
        adj[v].erase(u);
    }

    fill(mark.begin(), mark.end(), false);
    short cur_kc = 1;

    function<void(int, int)> dfs = [&](int v, int k) {
        mark[v] = true;
        kc[v] = k;
        for (const int u : adj[v])
            if (!mark[u])
                dfs(u, k);
    };

    for (int i = 1; i <= n; i++)
        if (!mark[i])
            dfs(i, cur_kc++);

    cout << cur_kc - 1 << endl;
    for (int i = 1; i <= n; i++)
        cout << kc[i] << " ";
    cout << endl;

    return 0;
}
