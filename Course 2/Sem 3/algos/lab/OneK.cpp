#include <vector>
#include <iostream>

using namespace std;

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int n, m;
    cin >> n >> m;

    vector<vector<int>> edges(n, vector<int>());
    for (int i = 0; i < m; i++) {
        int a, b;
        cin >> a >> b;
        a--; b--;
        edges[a].push_back(b);
    }
    vector<int> indexes(n, 0);
    for (int i = 0; i < n; i++) {
        int a;
        cin >> a;
        a--;
        indexes[a] = i;
    }

    bool result = true;
    for (int a = 0; a < n; a++)
        for (auto b : edges[a])
            result = result && indexes[a] < indexes[b];

    cout << (result ? "YES" : "NO") << endl;

}
