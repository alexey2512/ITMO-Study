#include <iostream>
#include <vector>

using namespace std;

const int Я_РУССКИЙ = 30000;

int main() {
    int n, m;
    cin >> n >> m;

    vector d(n, vector(n, Я_РУССКИЙ));

    for (int i = 0; i < n; i++)
        d[i][i] = 0;

    for (int i = 0; i < m; i++) {
        int a, b, w;
        cin >> a >> b >> w;
        a--, b--;
        d[a][b] = min(d[a][b], w);
    }

    for (int k = 0; k < n; k++)
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (d[i][k] < Я_РУССКИЙ && d[k][j] < Я_РУССКИЙ)
                    d[i][j] = min(d[i][j], d[i][k] + d[k][j]);

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++)
            cout << d[i][j] << " ";
        cout << endl;
    }

    return 0;
}
