#include <iostream>
#include <vector>
#include <set>

using namespace std;

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int n, m;
    cin >> n >> m;

    vector edges(n, vector<pair<int, int>>());
    for (int i = 0; i < m; i++) {
        int a, b, w;
        cin >> a >> b >> w;
        edges[a - 1].push_back(pair(b - 1, w));
        edges[b - 1].push_back(pair(a - 1, w));
    }

    vector d(n, INT_MAX);
    d[0] = 0;

    set<pair<int, int>> q;
    q.insert(pair(d[0], 0));

    while (!q.empty()) {
        const int v = q.begin()->second;
        q.erase(pair(d[v], v));
        for (const auto edge : edges[v]) {
            const int u = edge.first;
            const int w = edge.second;
            if (d[u] <= d[v] + w)
                continue;
            q.erase(pair(d[u], u));
            d[u] = d[v] + w;
            q.insert(pair(d[u], u));
        }
    }

    for (const auto x : d)
        cout << x << " ";
    cout << endl;
    return 0;
}