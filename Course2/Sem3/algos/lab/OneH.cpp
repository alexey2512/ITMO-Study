#include <iostream>
#include <vector>

using namespace std;

int n, m;
vector<vector<int>> edges;
vector<int> mark; // 0 - white, 1 - grey, 2 - black
vector<int> A, B, C;
bool fount = false;

void check() {
    if (2 * B.size() + A.size() == n)
        fount = true;
}

void dfs(int v) {
    mark[v] = 1;
    if (!fount) {
        A.push_back(v);
        check();
    }

    for (int u : edges[v])
        if (mark[u] == 0) {
            if (fount)
                C.push_back(u);
            dfs(u);
        }

    mark[v] = 2;
    if (!fount) {
        B.push_back(v);
        A.pop_back();
        check();
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    cin >> n >> m;
    edges.resize(n);
    mark.resize(n, 0);
    for (int i = 0; i < m; i++) {
        int x, y;
        cin >> x >> y;
        x--;
        y--;
        edges[x].push_back(y);
        edges[y].push_back(x);
    }

    for (int v = 0; v < n; v++)
        if (mark[v] == 0)
            dfs(v);

    cout << A.size() << " " << B.size() << endl;
    for (int v : A)
        cout << v + 1 << " ";
    cout << endl;

    for (int v : B)
        cout << v + 1 << " ";
    cout << endl;

    for (int v : C)
        cout << v + 1 << " ";
    cout << endl;

    return 0;
}
