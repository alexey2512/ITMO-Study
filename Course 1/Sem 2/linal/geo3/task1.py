# finding matrix T and T^(-1) using online calculator for solving systems of linear equations

T = [[4, -2, -9],
     [2, -1, -4],
     [1, -1, -1]]
S = [[-3, 7, -1],
     [-2, 5, -2],
     [-1, 2, 0]]

A = [
    [[-5, 2, 6],
     [3, -2, -1],
     [6, -7, 8]],

    [[8, 1, 3],
     [1, -7, -6],
     [-2, 2, -1]],

    [[-1, 3, 6],
     [-3, 5, 1],
     [1, -2, 0]]
]

B = [[[0 for _ in range(3)] for _ in range(3)] for _ in range(3)]

for i1 in range(3):
    for j1 in range(3):
        for m1 in range(3):
            s = 0
            for i in range(3):
                for j in range(3):
                    for m in range(3):
                        s += T[i][i1] * S[j1][j] * S[m1][m] * A[i][j][m]
            B[i1][j1][m1] = s

print("[", end="")
for j in range(3):
    for i in range(3):
        for m in range(3):
            print(B[i][j][m], end=("; " if i == 2 and m == 2 else ", "))
print("]")
