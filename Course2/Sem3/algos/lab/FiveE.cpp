#include <iostream>
#include <vector>

using namespace std;

constexpr int MAX_W = 100000;

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int n;
    cin >> n;
    vector d(n, vector(n, MAX_W));
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++) {
            int w;
            cin >> w;
            d[i][j] = min(d[i][j], w);
        }

    for (int k = 0; k < n; k++)
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (d[i][k] < MAX_W && d[k][j] < MAX_W)
                    d[i][j] = min(d[i][j], d[i][k] + d[k][j]);

    bool result = false;
    for (int i = 0; i < n; i++)
        result = result || d[i][i] < 0;

    cout << (result ? "YES" : "NO") << endl;
    return 0;
}
