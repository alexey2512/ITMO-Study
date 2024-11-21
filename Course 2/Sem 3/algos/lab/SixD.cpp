#include <vector>
#include <iostream>

using namespace std;

vector<vector<int>> edges;
vector<int> mark;

int dfs(const int v) {
    mark[v] = true;
    int count = 1;
    for (const int u : edges[v])
        if (!mark[u])
            count += dfs(u);
    return count;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int n, m;
    cin >> n >> m;
    edges.resize(n, vector<int>());
    for (int i = 0; i < m; i++) {
        int a, b;
        cin >> a >> b;
        edges[a].push_back(b);
        edges[b].push_back(a);
    }

    vector<int> sizes;
    mark.resize(n, false);

    for (int v = 0; v < n; v++)
        if (!mark[v])
            sizes.push_back(dfs(v));

    int sum = 0;
    for (const int size : sizes)
        sum += size - 1;

    cout << sum << endl;
}
