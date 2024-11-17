#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

int n, m;
vector<vector<int>> towards_edges;
vector<vector<int>> backwards_edges;
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

    towards_edges.resize(n, vector<int>());
    backwards_edges.resize(n, vector<int>());
    for (int i = 0; i < m; i++) {
        int a, b;
        cin >> a >> b;
        a--; b--;
        towards_edges[a].push_back(b);
        backwards_edges[b].push_back(a);
    }

    mark.resize(n, false);
    order = vector<int>();
    for (int v = 0; v < n; v++)
        if (!mark[v])
            top_sort(v);
    reverse(order.begin(), order.end());
    fill(mark.begin(), mark.end(), false);
    kc_numbers.resize(n, 0);
    int current_number = 1;
    for (auto v : order)
        if (!mark[v]) {
            pack(v, current_number);
            current_number++;
        }

    cout << current_number - 1 << endl;
    for (int v = 0; v < n; v++)
        cout << kc_numbers[v] << " ";
    cout << endl;
}