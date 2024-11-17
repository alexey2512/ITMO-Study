A = [-5, 0, 0, -1]

B = [[-1, 5, 4, 3],
     [-4, 3, -4, 4],
     [4, 0, 3, -3],
     [-5, 3, -4, 6]]

C = [[[0 for _ in range(4)] for _ in range(4)] for _ in range(4)]

for n in range(4):
    for l in range(4):
        for i in range(4):
            C[i][n][l] = A[n] * B[l][i]

D = [[[0 for _ in range(4)] for _ in range(4)] for _ in range(4)]
for i in range(4):
    for n in range(4):
        for l in range(4):
            D[i][n][l] = (C[i][n][l] - C[i][l][n] - C[n][i][l] + C[n][l][i] + C[l][i][n] - C[l][n][i]) / 2

print("[", end="")
for i in range(4):
    for n in range(4):
        for l in range(4):
            print(D[n][i][l], end=("; " if n == 3 and l == 3 else ", "))
print("]")

