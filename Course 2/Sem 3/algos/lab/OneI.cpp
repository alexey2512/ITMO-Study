#include <iostream>
#include <vector>

using llong = long long;
using namespace std;

void dfs(int v, int t, vector<vector<pair<int, int>>>& edges, vector<bool>& mark, vector<llong>& min_paths) {
    mark[v] = true;
    if (v == t) {
        min_paths[v] = 0;
        return;
    }
    for (auto p : edges[v]) {
        int u = p.first;
        int w = p.second;
        if (!mark[u])
            dfs(u, t, edges, mark, min_paths);
        if (min_paths[u] + w < min_paths[v])
            min_paths[v] = min_paths[u] + w;
    }
}



int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int n, m, s, t;
    cin >> n >> m >> s >> t;
    s--; t--;

    vector<vector<pair<int, int>>> edges(n, vector<pair<int, int>>());
    for (int i = 0; i < m; i++) {
        int a, b, w;
        cin >> a >> b >> w;
        a--; b--;
        edges[a].push_back(pair<int, int>(b, w));
    }

    llong max_value = (llong)INT_MAX * 2;
    vector<llong> min_paths(n, max_value);
    vector<bool> mark(n, false);

    dfs(s, t, edges, mark, min_paths);
    if (min_paths[s] > (llong)INT_MAX)
        cout << "Unreachable" << endl;
    else
        cout << min_paths[s] << endl;

}
