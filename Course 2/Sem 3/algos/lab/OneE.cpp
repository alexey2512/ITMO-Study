#include <iostream>
#include <vector>

using namespace std;

bool dfs(int u, vector<vector<int>>& edges, vector<int>& mark, int color) {
    mark[u] = color;
    for (auto v : edges[u]) {
        if (mark[v] == 0) {
            if (!dfs(v, edges, mark, 3 - color))
                return false;
        } else if (mark[v] == mark[u]) {
            return false;
        }
    }
    return true;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int n, m;
    cin >> n >> m;
    vector<vector<int>> edges(n);
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        u--;
        v--;
        edges[u].push_back(v);
        edges[v].push_back(u);
    }

    vector<int> mark(n, 0);
    bool result = true;

    for (int u = 0; u < n; u++) {
        if (mark[u] == 0) {
            if (!dfs(u, edges, mark, 1)) {
                result = false;
                break;
            }
        }
    }

    if (!result) {
        cout << -1 << endl;
    } else {
        for (int c : mark)
            cout << c << " ";
        cout << endl;
    }
    return 0;
}
