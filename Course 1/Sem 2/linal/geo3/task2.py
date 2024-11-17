# finding matrix T using online calculator for solving systems of linear equations

T = [[6, -2, -7],
     [5, -3, -7],
     [6, -3, -8]]

A = [[5, -4, 4],
     [-6, 4, 5],
     [-4, -5, -5]]

B = [[0 for _ in range(3)] for _ in range(3)]

for i1 in range(3):
    for m1 in range(3):
        s = 0
        for i in range(3):
            for m in range(3):
                s += T[i][i1] * T[m][m1] * A[i][m]
        B[i1][m1] = s

print("[", end="")
for i in range(3):
    for j in range(3):
        print(B[i][j], end=("; " if j == 2 else ", "))
print("]")
