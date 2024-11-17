#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
#include <math.h>

using namespace std;

int n, m;
vector<set<int>> towards_edges;
vector<set<int>> backwards_edges;
vector<int> mark;
vector<int> order;
vector<int> kc_numbers;

void top_sort(int v) {
    mark[v] = true;
    for (auto u : towards_edges[v])
        if (!mark[u])
            top_sort(u);
    order.push_back(v);
}

void pack(int v, int number) {
    mark[v] = true;
    kc_numbers[v] = number;
    for (auto u : backwards_edges[v])
        if (!mark[u])
            pack(u, number);
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    cin >> n >> m;

    towards_edges.resize(n, set<int>());
    backwards_edges.resize(n, set<int>());
    for (int i = 0; i < m; i++) {
        int a, b;
        cin >> a >> b;
        if (a == b)
            continue;
        a--; b--;
        towards_edges[a].insert(b);
        backwards_edges[b].insert(a);
    }

    mark.resize(n, false);
    order = vector<int>();
    for (int v = 0; v < n; v++)
        if (!mark[v])
            top_sort(v);
    reverse(order.begin(), order.end());
    fill(mark.begin(), mark.end(), false);
    kc_numbers.resize(n, 0);
    int current_number = 0;
    for (auto v : order)
        if (!mark[v]) {
            pack(v, current_number);
            current_number++;
        }

    vector<int> sizes_of_kcs(current_number, 0);
    for (auto kc : kc_numbers)
        sizes_of_kcs[kc]++;
    int result = 0;
    for (auto sz : sizes_of_kcs)
        result += min(2, sz);

    cout << result << endl;
}