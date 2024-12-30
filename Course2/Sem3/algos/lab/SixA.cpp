#include <iostream>
#include <vector>
#include <limits>
#include <iomanip>
#include <math.h>

using namespace std;

double f(const double a, const double b) {
    return sqrt(a * a + b * b);
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int n;
    cin >> n;
    vector<pair<int, int>> xy;
    for (int i = 0; i < n; i++) {
        int x, y;
        cin >> x >> y;
        xy.push_back(pair(x, y));
    }

    vector minEdge(n, numeric_limits<double>::max());
    minEdge[0] = numeric_limits<double>::min();
    vector mark(n, false);
    double ans = 0;

    while (true) {
        int v = -1;
        for (int i = 0; i < n; i++) {
            if (!mark[i] && (v == -1 || minEdge[i] < minEdge[v])) {
                v = i;
            }
        }
        if (v == -1) break;
        mark[v] = true;
        ans += minEdge[v];
        double edge;
        for (int u = 0; u < n; u++) {
            if (!mark[u] && minEdge[u] > (edge = f(xy[u].first - xy[v].first, xy[u].second - xy[v].second))) {
                minEdge[u] = edge;
            }
        }
    }

    cout << fixed << setprecision(10) << ans << endl;
    return 0;
}

