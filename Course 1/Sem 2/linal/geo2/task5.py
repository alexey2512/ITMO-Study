A = [[[0 for _ in range(4)] for _ in range(4)] for _ in range(4)]

g1 = [2, -4, 0, -6]
g2 = [-2, 6, 0, 8]
g3 = [-2, 4, 2, 6]

for i in range(4):
    for j in range(4):
        for k in range(4):
            A[i][j][k] = g1[k] * g2[i] * g3[j]

B = [[[0 for _ in range(4)] for _ in range(4)] for _ in range(4)]

for i in range(4):
    for j in range(4):
        for k in range(4):
            B[i][j][k] = (A[i][j][k] - A[i][k][j] - A[j][i][k] + A[j][k][i] + A[k][i][j] - A[k][j][i])

for j in range(4):
    for i in range(4):
        for k in range(4):
            a = str(B[i][j][k])
            a = ' ' * (6 - len(a)) + a
            print(a, end=" ")
        print("|", end=" ")
    print()

