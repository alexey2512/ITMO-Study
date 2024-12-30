#include <iostream>
#include <vector>

#define UNVISITED 0
#define VISITING 1
#define VISITED 2

using namespace std;

void find_cycle(int v, vector<vector<int>>& edges, vector<int>& mark, bool &found) {
    if (found) return;
    mark[v] = VISITING;
    for (auto u : edges[v]) {
        if (mark[u] == UNVISITED)
            find_cycle(u, edges, mark, found);
        else if (mark[u] == VISITING) {
            found = true;
            return;
        }
    }
    mark[v] = VISITED;
}

void top_sort(int v, vector<vector<int>>& edges, vector<bool>& mark, vector<int>& result) {
    mark[v] = true;
    for (auto u : edges[v])
        if (!mark[u])
            top_sort(u, edges, mark, result);
    result.push_back(v);
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int n, m;
    cin >> n >> m;
    vector<vector<int>> edges(n, vector<int>());
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        u--;
        v--;
        edges[u].push_back(v);
    }

    vector<int> mark(n, UNVISITED);
    bool found = false;
    for (int v = 0; v < n; v++)
        if (mark[v] == UNVISITED)
            find_cycle(v, edges, mark, found);

    if (found)
        cout << -1 << endl;
    else {
        vector<int> result;
        vector<bool> new_mark(n, false);
        for (int v = 0; v < n; v++)
            if (!new_mark[v])
                top_sort(v, edges, new_mark, result);
        for (int i = n - 1; i >= 0; i--)
            cout << result[i] + 1 << " ";
        cout << endl;
    }
}
