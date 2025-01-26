#include <iostream>
#include <deque>
#include <vector>
#include <cstdint>

using namespace std;

int n, m, k;
vector<vector<pair<int, int>>> edges;

int bfs(const int a, const int b) {
    vector fine(n, INT32_MAX);
    fine[a] = 0;
    deque<int> deq;
    deq.push_back(a);
    while (!deq.empty()) {
        const int c = deq.front();
        deq.pop_front();
        for (const auto& [a, w] : edges[c]) {
            if (fine[a] <= fine[c] + w)
                continue;
            if (w == 1)
                deq.push_back(a);
            else
                deq.push_front(a);
            fine[a] = fine[c] + w;
        }
    }
    return fine[b] == INT32_MAX ? -1 : fine[b];
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);

    cin >> n >> m;
    edges.resize(n);
    for (int i = 0; i < m; i++) {
        int a, b;
        cin >> a >> b;
        edges[a - 1].emplace_back(b - 1, 0);
        edges[b - 1].emplace_back(a - 1, 1);
    }

    cin >> k;
    vector<pair<int, int>> qs;
    for (int i = 0; i < k; i++) {
        int a, b;
        cin >> a >> b;
        qs.emplace_back(a - 1, b - 1);
    }

    for (const auto& [a, b] : qs)
        cout << bfs(a, b) << endl;

    return 0;
}
