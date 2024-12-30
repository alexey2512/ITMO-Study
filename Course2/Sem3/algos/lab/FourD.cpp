#include <iostream>
#include <vector>
#include <queue>

using namespace std;

constexpr int SIZE = 10000;
vector prevs(SIZE, -1);

bool bfs(const int s, const int t) {
    vector mark(SIZE, false);
    queue<int> qu;
    mark[s] = true;
    qu.push(s);
    while (!qu.empty()) {
        const int x = qu.front();
        qu.pop();
        if (x == t)
            return true;
        int y;
        if (x < 9000) {
            y = x + 1000;
            if (!mark[y]) {
                mark[y] = true;
                qu.push(y);
                prevs[y] = x;
            }
        }
        if (x % 10 > 1) {
            y = x - 1;
            if (!mark[y]) {
                mark[y] = true;
                qu.push(y);
                prevs[y] = x;
            }
        }
        y = x / 10 + (x % 10) * 1000;
        if (!mark[y]) {
            mark[y] = true;
            qu.push(y);
            prevs[y] = x;
        }
        y = (x % 1000) * 10 + x / 1000;
        if (!mark[y]) {
            mark[y] = true;
            qu.push(y);
            prevs[y] = x;
        }
    }
    return false;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int s, t;
    cin >> s >> t;

    bfs(s, t);

    vector<int> path;
    path.push_back(t);

    int cur = t;
    while (cur != s) {
        cur = prevs[cur];
        path.push_back(cur);
    }

    for (int v = path.size() - 1; v >= 0; v--)
        cout << path[v] << endl;

    return 0;
}
