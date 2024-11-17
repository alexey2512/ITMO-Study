# finding matrix T and T^(-1) is not so difficult, we can use paper and pen

T = [[-5, -3],
     [2, 1]]

S = [[1, 3],
     [-2, -5]]

A = [
    [[-2, 0],
     [0, 2]],

    [[5, 5],
     [4, -4]]
]

B = [[[0 for _ in range(2)] for _ in range(2)] for _ in range(2)]

for r1 in range(2):
    for j1 in range(2):
        for m1 in range(2):
            s = 0
            for r in range(2):
                for j in range(2):
                    for m in range(2):
                        s += T[r][r1] * S[j1][j] * S[m1][m] * A[r][j][m]
            B[r1][j1][m1] = s

print("[", end="")
for j in range(2):
    for r in range(2):
        for m in range(2):
            print(B[r][j][m], end=("; " if r == 1 and m == 1 else ", "))
print("]")