#include <iostream>
#include <set>
#include <vector>

using namespace std;


void dfs(int u, vector<set<int>>& edges, vector<bool>& mark) {
    mark[u] = true;
    for (auto v : edges[u]) {
        if (!mark[v]) {
            cout << u + 1 << " " << v + 1 << "\n";
            dfs(v, edges, mark);
        }
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int n, m;
    cin >> n;
    cin >> m;
    vector<set<int>> edges(n, set<int>());
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u; u--;
        cin >> v; v--;
        edges[u].insert(v);
        edges[v].insert(u);
    }
    vector<bool> mark(n, false);
    dfs(0, edges, mark);
}
