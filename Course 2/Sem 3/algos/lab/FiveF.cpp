#include <iostream>
#include <vector>

using namespace std;

using llong = long long;

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    cout.tie(nullptr);

    int n, m, K, s;
    cin >> n >> m >> K >> s;
    s--;

    vector edges(n, vector<pair<int, llong>>());
    for (int i = 0; i < m; i++) {
        int a, b;
        llong w;
        cin >> a >> b >> w;
        edges[a - 1].push_back(pair(b - 1, w));
    }

    vector d(K + 1, vector<llong>(n, INT_MAX));
    d[0][s] = 0l;

    for (int k = 1; k <= K; k++)
        for (int v = 0; v < n; v++)
            for (const auto edge : edges[v]) {
                const int u = edge.first;
                const llong w = edge.second;
                d[k][u] = min(d[k][u], d[k - 1][v] + w);
            }

    for (int v = 0; v < n; v++)
        cout << (d[K][v] >= INT_MAX / 2 ? -1 : d[K][v]) << endl;

    return 0;
}
