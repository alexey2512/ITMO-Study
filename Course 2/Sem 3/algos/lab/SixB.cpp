#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

struct Edge {
    int a, b;
    int weight;
    Edge(const int a, const int b, const int w) : a(a), b(b), weight(w) {}
};

int root(const int x, vector<int>& parent) {
    if (x == parent[x])
        return x;
    return parent[x] = root(parent[x], parent);
}

void unite(int a, int b, vector<int>& parent, vector<int>& rank) {
    a = root(a, parent);
    b = root(b, parent);
    if (a == b)
        return;
    if (rank[a] > rank[b])
        parent[b] = a;
    else if (rank[a] < rank[b])
        parent[a] = b;
    else {
        parent[b] = a;
        rank[a]++;
    }
}

bool check(const int a, const int b, vector<int>& parent) {
    return root(a, parent) == root(b, parent);
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int n, m;
    cin >> n >> m;

    vector<Edge> edges;
    vector parents(n, 0);
    vector ranks(n, 0);

    for (int v = 0; v < n; v++)
        parents[v] = v;

    for (int i = 0; i < m; i++) {
        int a, b, w;
        cin >> a >> b >> w;
        a--; b--;
        edges.push_back(Edge(a, b, w));
    }

    std::sort(edges.begin(), edges.end(), [](const Edge& a, const Edge& b) {
        if (a.weight == b.weight)
            return a.a < b.a;
        return a.weight < b.weight;
    });

    long long ans = 0;
    for (const auto edge : edges) {
        if (check(edge.a, edge.b, parents))
            continue;
        ans += edge.weight;
        unite(edge.a, edge.b, parents, ranks);
    }

    cout << ans << endl;
    return 0;
}
