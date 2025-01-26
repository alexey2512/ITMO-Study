#include <iostream>
#include <vector>
#include <queue>
#include <cstdint>
#include <algorithm>

using namespace std;

vector<vector<int>> capacity;
vector<vector<int>> adj;

int bfs(const int s, const int t, vector<int>& p) {
    ranges::fill(p, -1);
    p[s] = s;
    queue<pair<int, int>> q;
    q.emplace(s, INT32_MAX);
    while (!q.empty()) {
        const int cur = q.front().first;
        const int flow = q.front().second;
        q.pop();
        for (int next : adj[cur]) {
            if (p[next] != -1 || capacity[cur][next] <= 0)
                continue;
            p[next] = cur;
            int flow_ = min(flow, capacity[cur][next]);
            if (next == t)
                return flow_;
            q.emplace(next, flow_);
        }
    }
    return 0;
}

int ek(const int s, const int t, const int n) {
    int max_f = 0;
    vector<int> p(n);
    int f;
    while ((f = bfs(s, t, p)) > 0) {
        max_f += f;
        int cur = t;
        while (cur != s) {
            const int prev = p[cur];
            capacity[prev][cur] -= f;
            capacity[cur][prev] += f;
            cur = prev;
        }
    }
    return max_f;
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);

    int n, m;
    cin >> n >> m;

    capacity.resize(n, vector(n, 0));
    adj.resize(n, vector<int>());
    for (int i = 0; i < m; i++) {
        int a, b, c;
        cin >> a >> b >> c;
        a--;
        b--;
        capacity[a][b] += c;
        adj[a].push_back(b);
        adj[b].push_back(a);
    }

    cout << ek(0, n - 1, n) << endl;
    return 0;
}
