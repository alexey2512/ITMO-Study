#include <iostream>
#include <queue>
#include <vector>
#include <cstdint>

using namespace std;

vector<vector<pair<int, int>>> edges;

void solve(const int n, const int s) {
    vector d(2, vector(n, INT32_MAX));
    d[0][s] = 0;
    d[1][s] = 0;
    queue<pair<int, int>> q;
    q.emplace(s, 0);
    q.emplace(s, 1);
    while (!q.empty()) {
        const int a = q.front().first;
        const int t = q.front().second;
        q.pop();
        for (const auto& [b, tt] : edges[a]) {
            if (t != tt && d[tt][b] > d[t][a] + 1) {
                d[tt][b] = d[t][a] + 1;
                q.emplace(b, tt);
            }
        }
    }
    vector<int> ans(n - 1);
    for (int i = 0; i < n; i++) {
        ans[i] = min(d[0][i], d[1][i]);
        if (ans[i] == INT32_MAX)
            ans[i] = -1;
    }
    for (const auto& x : ans)
        cout << x << " ";
    cout << endl;
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);

    int n, m;
    cin >> n >> m;

    edges.resize(n, vector<pair<int, int>>());

    for (int i = 0; i < m; i++) {
        int a, b, t;
        cin >> a >> b >> t;
        edges[b - 1].emplace_back(a - 1, t - 1);
    }

    solve(n, n - 1);
    return 0;
}
