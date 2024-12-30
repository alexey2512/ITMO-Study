#include <iostream>
#include <vector>

#define UNVISITED 0
#define VISITING 1
#define VISITED 2

using namespace std;

bool dfs(int v, vector<vector<int>>& edges, vector<int>& mark, vector<int>& stack, int& start) {
    mark[v] = VISITING;
    for (int u : edges[v]) {
        if (mark[u] == UNVISITED) {
            if (dfs(u, edges, mark, stack, start)) {
                if (start != -1) {
                    stack.push_back(v);
                    if (v == start) {
                        start = -1;
                    }
                }
                return true;
            }
        } else if (mark[u] == VISITING) {
            start = u;
            stack.push_back(v);
            return true;
        }
    }
    mark[v] = VISITED;
    return false;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int n, m;
    cin >> n >> m;

    vector<vector<int>> edges(n, vector<int>());
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        u--; v--;
        edges[u].push_back(v);
    }

    vector<int> mark(n, UNVISITED);
    vector<int> stack;
    int start = -1;
    for (int i = 0; i < n; i++)
        if (mark[i] == UNVISITED && dfs(i, edges, mark, stack, start))
            break;

    if (stack.empty())
        cout << -1 << endl;
    else {
        cout << stack.size() << endl;
        for (int i = stack.size() - 1; i >= 0; i--)
            cout << stack[i] + 1 << " ";
        cout << endl;
    }

}
