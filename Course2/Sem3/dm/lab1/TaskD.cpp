#include <bits/stdc++.h>
#include <deque>
#include <iostream>
#include <vector>

using namespace std;

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    int n;
    cin >> n;
    vector<vector<bool>> adj(n, vector<bool>(n, false));

    for (int i = 1; i < n; i++) {
        string line;
        cin >> line;
        for (int j = 0; j < line.size(); j++) {
            adj[i][j] = line[j] == '1';
            adj[j][i] = line[j] == '0';
        }
    }

    deque<int> queue(n);
    for (int i = 0; i < n; i++)
        queue[i] = i;

    for (int i = 0; i < 10 * n * (n - 1); i++) {
        if (adj[queue[0]][queue[1]]) {
            queue.push_back(queue.front());
            queue.pop_front();
        } else {
            int shift = 2;
            while (shift < n && !adj[queue[0]][queue[shift]])
                shift++;
            queue.insert(queue.begin() + shift, queue.front());
            queue.pop_front();
        }
    }

    for (int v : queue) {
        cout << v + 1 << " ";
    }
    cout << "\n";

}
