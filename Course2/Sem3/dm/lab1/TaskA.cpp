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
            adj[j][i] = adj[i][j];
        }
    }

    deque<int> queue(n);
    for (int i = 0; i < n; i++)
        queue[i] = i;

    for (int i = 0; i < n * (n - 1) / 2; i++) {
        if (!adj[queue[0]][queue[1]]) {
            int shift = 2;
            while (!adj[queue[0]][queue[shift]] || !adj[queue[1]][queue[shift + 1]])
                shift++;
            reverse(queue.begin() + 1, queue.begin() + shift + 1);
        }
        queue.push_back(queue.front());
        queue.pop_front();
    }

    for (int v : queue) {
        cout << v + 1 << " ";
    }
    cout << "\n";
}