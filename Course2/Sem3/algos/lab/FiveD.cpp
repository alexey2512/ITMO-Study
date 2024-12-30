#include <iostream>
#include <vector>

using namespace std;

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int n;
    cin >> n;
    vector d(n, vector(n, 0));
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            cin >> d[i][j];

    for (int k = 0; k < n; k++)
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                d[i][j] = max(d[i][j], d[i][k] & d[k][j]);

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++)
            cout << d[i][j] << " ";
        cout << endl;
    }

    return 0;
}
