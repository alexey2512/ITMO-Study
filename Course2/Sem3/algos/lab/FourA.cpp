#include <iostream>
#include <queue>
#include <vector>

using namespace std;

int n, m;
vector<vector<int>> edges;
vector<int> prevs;
vector<int> paths;

void bfs(const int s) {
    prevs.resize(n, 0);
    paths.resize(n, INT_MAX);
    paths[s] = 0;
    queue<int> que;
    que.push(s);
    while (!que.empty()) {
        const int v = que.front();
        que.pop();
        for (int u : edges[v])
            if (paths[u] == INT_MAX) {
                paths[u] = paths[v] + 1;
                prevs[u] = v;
                que.push(u);
            }
    }
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
    }
    int s, t;
    cin >> s >> t;
    s--; t--;

    bfs(s);
    if (paths[t] == INT_MAX) {
        cout << -1 << endl;
        return 0;
    }
    vector<int> path;
    path.push_back(t);
    int cur = t;
    while (cur != s) {
        cur = prevs[cur];
        path.push_back(cur);
    }
    cout << paths[t] << endl;
    for (int v = path.size() - 1; v >= 0; v--)
        cout << path[v] + 1 << " ";
    cout << endl;
    return 0;

}
