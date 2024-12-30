#include <iostream>
#include <vector>
#include <deque>

using llong = long long;

using namespace std;

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int n;
    cin >> n;

    vector d(n, vector<llong>(n, 0));
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            cin >> d[i][j];

    deque<int> order;
    for (int i = 0; i < n; i++) {
        int v;
        cin >> v;
        v--;
        order.push_front(v);
    }

    deque<llong> result;
    vector mark(n, false);
    for (int k = 0; k < n; k++) {
        const int x = order[k];
        mark[x] = true;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                d[i][j] = min(d[i][j], d[i][x] + d[x][j]);
        llong sum = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (mark[i] && mark[j])
                    sum += d[i][j];
        result.push_front(sum);
    }

    for (const llong sum : result)
        cout << sum << " ";
    cout << endl;

    return 0;
}
