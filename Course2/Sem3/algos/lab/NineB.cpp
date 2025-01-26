#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
#include <cstdint>

using namespace std;

#define ll long long

struct Edge {
    int to, rev;
    ll cap, flow;
};

int n, m;
vector<vector<Edge>> adj;
vector<int> level;
vector<int> ptr;

void addEdge(const int from, const int to, const ll cap) {
    const Edge forward = {to, static_cast<int>(adj[to].size()), cap, 0};
    const Edge backward = {from, static_cast<int>(adj[from].size()), 0, 0};
    adj[from].push_back(forward);
    adj[to].push_back(backward);
}

bool bfs(const int s, const int t) {
    ranges::fill(level, -1);
    level[s] = 0;
    queue<int> q;
    q.push(s);
    while (!q.empty()) {
        const int node = q.front();
        q.pop();
        for (const Edge& edge : adj[node]) {
            if (level[edge.to] == -1 && edge.flow < edge.cap) {
                level[edge.to] = level[node] + 1;
                q.push(edge.to);
            }
        }
    }
    return level[t] != -1;
}

ll dfs(const int node, const int t, const ll flow) {
    if (flow == 0 || node == t)
        return flow;
    for (; ptr[node] < adj[node].size(); ptr[node]++) {
        Edge& edge = adj[node][ptr[node]];
        if (!(level[edge.to] == level[node] + 1 && edge.flow < edge.cap))
            continue;
        const ll pushed = dfs(edge.to, t, min(flow, edge.cap - edge.flow));
        if (pushed <= 0)
            continue;
        edge.flow += pushed;
        adj[edge.to][edge.rev].flow -= pushed;
        return pushed;
    }
    return 0;
}

ll dinic(const int s, const int t) {
    ll total = 0;
    while (bfs(s, t)) {
        ranges::fill(ptr, 0);
        while (const ll pushed = dfs(s, t, INT64_MAX))
            total += pushed;
    }
    return total;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    cin >> n >> m;

    adj.resize(n);
    level.resize(n);
    ptr.resize(n);

    for (int i = 0; i < m; ++i) {
        int u, v;
        ll c;
        cin >> u >> v >> c;
        u--, v--;
        addEdge(u, v, c);
    }

    cout << dinic(0, n - 1) << endl;
    return 0;
}
