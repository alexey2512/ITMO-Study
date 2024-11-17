#include <iostream>
#include <set>
#include <vector>

using namespace std;

void dfs(int u, vector<set<int>>& edges, vector<bool>& mark, vector<int>& vertices) {
    mark[u] = true;
    vertices.push_back(u);
    for (auto v : edges[u])
        if (!mark[v])
            dfs(v, edges, mark, vertices);
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
    vector<vector<int>> ks(n, vector<int>());
    int cnt = 0;
    for (int v = 0; v < n; v++) {
        if (!mark[v]) {
            dfs(v, edges, mark, ks[cnt]);
            cnt++;
        }
    }
    cout << cnt - 1 << endl;
    for (int i = 0; i < cnt - 1; i++)
        cout << ks[i][0] + 1 << " " << ks[i + 1][0] + 1 << endl;
}
