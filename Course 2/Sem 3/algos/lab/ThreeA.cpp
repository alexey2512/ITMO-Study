#include <iostream>
#include <vector>
#include <set>

using namespace std;

constexpr int VERT = 20001;

vector<pair<int, int>> edges[VERT];
set<int> bridges;
int timer = 0;

vector depth(VERT, -1);
vector up(VERT, -1);
vector mark(VERT, false);

void find_bridges(const int v, const int pe = -1) {
    mark[v] = true;
    depth[v] = timer;
    up[v] = timer;
    timer++;
    for (auto [u, id] : edges[v]) {
        if (id == pe)
            continue;
        if (!mark[u]) {
            find_bridges(u, id);
            up[v] = min(up[v], up[u]);
            if (up[u] > depth[v])
                bridges.insert(id);
        } else
            up[v] = min(up[v], depth[u]);
    }
}

int main() {
    int n, m;
    cin >> n >> m;

    for (int i = 1; i <= m; i++) {
        int a, b;
        cin >> a >> b;
        edges[a].emplace_back(b, i);
        edges[b].emplace_back(a, i);
    }

    for (int i = 1; i <= n; i++)
        if (!mark[i])
            find_bridges(i);

    cout << bridges.size() << endl;
    for (const int bridge : bridges)
        cout << bridge << " ";
    cout << endl;

    return 0;
}
