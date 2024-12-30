#include <iostream>
#include <vector>
#include <algorithm>
#include <math.h>

using namespace std;

struct Edge {
    int a, b;
    int w;
    Edge(const int a, const int b, const int w) : a(a), b(b), w(w) {}
};

vector<int> par;
vector<int> dep;
vector<bool> mark;
vector<vector<int>> up;
vector<vector<int>> mx;
vector<vector<int>> mst_table;
int LOG;

// DSU begin
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
// DSU end


void dfs(const int v, const int p, const int d = 0) {
    par[v] = p;
    dep[v] = d;
    mark[v] = true;
    for (int u = 0; u < mark.size(); u++)
        if (mst_table[v][u] > 0 && !mark[u] && u != p)
            dfs(u, v, d + 1);
}

void make(const int n) {
    for (int i = 0; i < n; i++) {
        up[i][0] = par[i];
        mx[i][0] = mst_table[i][par[i]];
    }
    for (int k = 1; k < LOG; k++)
        for (int i = 0; i < n; i++) {
            const int mid = up[i][k - 1];
            up[i][k] = up[mid][k - 1];
            mx[i][k] = max(mx[i][k - 1], mx[mid][k - 1]);
        }
}

int lca(int u, int v) {
    int cur = INT_MIN;
    if (dep[u] < dep[v])
        swap(u, v);
    for (int k = LOG - 1; k >= 0; k--) {
        int _u = up[u][k];
        if (dep[_u] >= dep[v]) {
            cur = max(cur, mx[u][k]);
            u = _u;
        }
    }
    if (u == v)
        return cur;
    for (int k = LOG - 1; k >= 0; k--) {
        int _u = up[u][k];
        int _v = up[v][k];
        if (_u != _v) {
            cur = max(cur, mx[u][k]);
            cur = max(cur, mx[v][k]);
            u = _u;
            v = _v;
        }
    }
    return max(cur, max(mx[u][0], mx[v][0]));
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
        if (a.w == b.w)
            return a.a < b.a;
        return a.w < b.w;
    });

    vector edge_in_mst(m, false);
    long long fst = 0;
    for (int i = 0; i < m; i++) {
        const auto& edge = edges[i];
        if (check(edge.a, edge.b, parents))
            continue;
        fst += edge.w;
        edge_in_mst[i] = true;
        unite(edge.a, edge.b, parents, ranks);
    }

    mst_table.resize(n, vector(n, 0));
    for (int i = 0; i < m; i++) {
        if (!edge_in_mst[i])
            continue;
        const auto& edge = edges[i];
        mst_table[edge.a][edge.b] = edge.w;
        mst_table[edge.b][edge.a] = edge.w;
    }

    LOG = static_cast<int>(ceil(log(n) / log(2)) + 1);

    par.resize(n, 0);
    dep.resize(n, 0);
    mark.resize(n, false);
    up.resize(n, vector(LOG, 0));
    mx.resize(n, vector(LOG, 0));
    dfs(0, 0);
    make(n);

    long long snd = INT_MAX;
    for (int i = 0; i < m; i++) {
        if (edge_in_mst[i])
            continue;
        const Edge edge = edges[i];
        const int max_edge = lca(edge.a, edge.b);
        long long new_snd = fst - max_edge + edge.w;
        if (new_snd < snd)
            snd = new_snd;
    }

    cout << fst << " " << snd << endl;
    return 0;
}
