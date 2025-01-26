#include <iostream>
#include <vector>
 
using namespace std;
 
int n, m;
vector<vector<int>> edges;
vector<bool> mark;
vector<int> match;
 
bool dfs(const int v) {
    if (mark[v])
        return false;
    mark[v] = true;
    for (const int u : edges[v])
        if (const int m = match[u]; m == -1 || dfs(m)) {
            match[u] = v;
            return true;
        }
    return false;
}
 
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
 
    cin >> n >> m;
    mark.resize(m, false);
    match.resize(n, -1);
    edges.resize(m, vector<int>());
 
    for (int i = 0; i < n; i++) {
        while (true) {
            int v;
            cin >> v;
            if (v == 0) break;
            v--;
            edges[v].push_back(i);
        }
    }
 
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < m; j++)
            mark[j] = false;
        dfs(i);
    }
 
    int cnt = 0;
    for (int i = 0; i < n; i++)
        if (match[i] != -1)
            cnt++;
    cout << cnt << endl;
    for (int i = 0; i < n; i++)
        if (match[i] != -1)
            cout << i + 1 << " " << match[i] + 1 << endl;
 
    return 0;
}
