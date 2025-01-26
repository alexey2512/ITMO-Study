#include <algorithm>
#include <iostream>
#include <vector>
#include <cstdint>

using namespace std;

constexpr int INF = INT32_MAX;

int n;
vector<vector<pair<int, int>>> edges;

vector<int> fb(vector<vector<pair<int, int>>>& edges, const int s) {
    vector d(n, INF);
    vector p(n, 0);
    for (int i = 0; i < n; i++)
        p[i] = i;
    d[s] = 0;
    for (int i = 1; i < n; i++) {
        for (int v = 0; v < n; v++) {
            if (d[v] == INF)
                continue;
            for (auto const& [to, w]: edges[v]) {
                if (d[to] > d[v] + w) {
                    d[to] = d[v] + w;
                    p[to] = v;
                }
            }
        }
    }

    for (int v = 0; v < n; v++) {
        if (d[v] == INF)
            continue;
        for (auto [to, w]: edges[v]) {
            if (d[to] <= d[v] + w)
                continue;
            for (int i = 0; i < n; i++)
                to = p[to];
            int u = to;
            vector<int> cycle;
            while (true) {
                if (u == to && !cycle.empty())
                    break;
                cycle.push_back(u);
                u = p[u];
            }
            ranges::reverse(cycle);
            return cycle;
        }
    }
    return {};
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    cin >> n;
    edges.resize(n, vector<pair<int, int>>());
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            int w;
            cin >> w;
            if (w < 100000)
                edges[i].emplace_back(j, w);
        }
    }

    for (int i = 0; i < n; i++) {
        vector<int> ans = fb(edges, i);
        if (ans.empty())
            continue;
        cout << "YES" << '\n';
        cout << ans.size() << '\n';
        for (auto const& x: ans)
            cout << x + 1 << " ";
        cout << endl;
        return 0;
    }

    cout << "NO" << '\n';
    return 0;
}
