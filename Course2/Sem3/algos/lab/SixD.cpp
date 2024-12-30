#include <vector>
#include <set>
#include <iostream>

using namespace std;

void fast_dfs(const int v, set<int>& unvisited, vector<set<int>>& edges) {
    unvisited.erase(v);
    vector<int> adj;
    for (auto x : unvisited)
        if (!edges[v].contains(x))
            adj.push_back(x);
    for (auto x : adj)
        unvisited.erase(x);
    for (const auto x : adj)
        fast_dfs(x, unvisited, edges);
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int n, m;
    cin >> n >> m;

    vector edges(n, set<int>());
    for (int i = 0; i < m; i++) {
        int a, b;
        cin >> a >> b;
        edges[a - 1].insert(b - 1);
        edges[b - 1].insert(a - 1);
    }

    set<int> unvisited;
    for (int i = 0; i < n; i++)
        unvisited.insert(i);

    int kc = -1;
    for (int v = 0; v < n; v++)
        if (unvisited.contains(v)) {
            fast_dfs(v, unvisited, edges);
            kc++;
        }

    cout << kc << endl;
    return 0;
}
