#include <iostream>
#include <string>
#include <vector>

using namespace std;

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    int n;
    cin >> n;
    vector<int> vec(1, 0);

    for (int i = 1; i < n; i++) {
        int left = -1;
        int right = i;
        while (right - left > 1) {
            int mid = (left + right) / 2;
            cout << 1 << " " << i + 1 << " " << (vec[mid] + 1) << endl;
            string response;
            cin >> response;
            if (response == "YES")
                right = mid;
            else
                left = mid;
        }
        vec.insert(vec.begin() + right, i);
    }

    cout << 0 << " ";
    for (int v : vec) {
        cout << v + 1 << " ";
    }
    cout << endl;

}

