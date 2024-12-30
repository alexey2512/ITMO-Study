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
    vector<int> prufer;
    for (int i = 0; i < n - 2; i++) {
        int x;
        cin >> x;
        prufer.push_back(x - 1);
    }

    vector<int> count_in_prufer(n, 0);
    for (int v : prufer)
        count_in_prufer[v]++;

    priority_queue<int, vector<int>, greater<>> heap;
    for (int v = 0; v < n; v++)
        if (count_in_prufer[v] == 0)
            heap.push(v);

    set<int> vertices;
    for (int i = 0; i < n; i++)
        vertices.insert(i);

    for (int i = 0; i < n - 2; i++) {
        int u = prufer.front();
        int v = heap.top();
        cout << (u + 1) << " " << (v + 1) << "\n";
        prufer.erase(prufer.begin());
        heap.pop();
        count_in_prufer[u]--;
        if (count_in_prufer[u] == 0)
            heap.push(u);
        vertices.erase(v);
    }

    auto it = vertices.begin();
    int v = *it;
    ++it;
    int u = *it;
    cout << (v + 1) << " " << (u + 1) << "\n";



}
