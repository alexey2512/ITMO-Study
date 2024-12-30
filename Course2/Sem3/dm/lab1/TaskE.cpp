#include <iostream>
#include <vector>
#include <queue>
#include <set>

using namespace std;

int main() {

    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    int n;
    cin >> n;
    vector<set<int>> adj(n, set<int>{});

    for (int i = 0; i < n - 1; i++) {
        int u, v;
        cin >> v;
        cin >> u;
        u--;
        v--;
        adj[v].insert(u);
        adj[u].insert(v);
    }

    priority_queue<int, vector<int>, greater<>> leafs;
    for (int v = 0; v < n; v++)
        if (adj[v].size() == 1)
            leafs.push(v);

    for (int i = 0; i < n - 2; i++) {
        int v = leafs.top();
        leafs.pop();
        int p = *(adj[v].begin());
        adj[v].erase(p);
        adj[p].erase(v);
        if (adj[p].size() == 1)
            leafs.push(p);
        cout << (p + 1) << " ";
    }
    cout << "\n";
}
