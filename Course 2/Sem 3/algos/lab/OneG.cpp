#include <iostream>
#include <vector>
#include <string>

#define UNVISITED 0
#define VISITING 1
#define VISITED 2

using namespace std;

void find_cycle(int v, vector<vector<int>>& edges, vector<int>& mark, bool &found) {
    if (found) return;
    mark[v] = VISITING;
    for (auto u : edges[v]) {
        if (mark[u] == UNVISITED)
            find_cycle(u, edges, mark, found);
        else if (mark[u] == VISITING) {
            found = true;
            return;
        }
    }
    mark[v] = VISITED;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int n;
    cin >> n;

    vector<vector<int>> edges(n, vector<int>());
    for (int i = 0; i < n - 1; i++) {
        string str;
        cin >> str;
        for (int j = i + 1; j < n; j++) {
            char edge = str[j - i - 1];
            switch (edge) {
            case 'R':
                edges[i].push_back(j);
                break;
            case 'B':
                edges[j].push_back(i);
                break;
            }
        }
    }

    vector<int> mark(n, UNVISITED);
    bool found = false;
    for (int v = 0; v < n; v++)
        if (mark[v] == UNVISITED)
            find_cycle(v, edges, mark, found);

    cout << (found ? "NO" : "YES") << endl;

}

