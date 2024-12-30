#include <iostream>
#include <vector>
#include <set>

using namespace std;


void dfs(int v, vector<vector<int>>& adj, vector<bool>& mark, vector<int>& colors, vector<set<int>>& banned_colors) {
    mark[v] = true;
    int c = 0;
    while (banned_colors[v].find(c) != banned_colors[v].end())
        c++;
    colors[v] = c;
    for (int u : adj[v])
        banned_colors[u].insert(c);

    for (int u : adj[v])
        if (!mark[u])
            dfs(u, adj, mark, colors, banned_colors);
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    int n, m;
    cin >> n;
    cin >> m;
    vector<vector<int>> adj(n, vector<int>{});
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u;
        cin >> v;
        u--;
        v--;
        adj[u].push_back(v);
        adj[v].push_back(u);
    }
    int k = 0;
    for (int i = 0; i < n; i++) {
        if (adj[i].size() > k)
            k = (int)adj[i].size();
    }
    cout << (k % 2 == 0 ? k + 1 : k) << "\n";
    vector<bool> mark(n, false);
    vector<int> colors(n, 0);
    vector<set<int>> banned_colors(n, set<int>{});
    dfs(0, adj, mark, colors, banned_colors);
    for (int color : colors)
        cout << (color + 1) << "\n";
}
