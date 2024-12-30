#include <iostream>
#include <vector>
#include <map>
#include <set>

using namespace std;

const int Я_РУССКИЙ = 30000;

int main() {
    int n, m;
    cin >> n >> m;

    vector mat(n, vector(n, Я_РУССКИЙ));
    vector edges(n, set<int>());

    for (int i = 0; i < n; i++)
        mat[i][i] = 0;

    for (int i = 0; i < m; i++) {
        int a, b, w;
        cin >> a >> b >> w;
        a--, b--;
        mat[a][b] = min(mat[a][b], w);
        edges[a].insert(b);
    }

    vector d(n, vector(n, Я_РУССКИЙ));
    d[0][0] = 0;

    for (int k = 1; k < n; k++) {
        for (int v = 0; v < n; v++)
            d[k][v] = d[k - 1][v];
        for (int v = 0; v < n; v++)
            for (const int u : edges[v])
                if (d[k - 1][v] < Я_РУССКИЙ && mat[v][u] < Я_РУССКИЙ)
                    d[k][u] = min(d[k][u], d[k - 1][v] + mat[v][u]);
    }

    for (int v = 0; v < n; v++)
        cout << d[n - 1][v] << " ";
    cout << endl;

    return 0;
}
