A = [[3, -3],
     [0, -3]]

B = [

    [[3, 1],
     [2, 4]],

    [[0, -1],
     [-2, -2]]

]

C = [[[[[0 for _ in range(2)] for _ in range(2)] for _ in range(2)] for _ in range(2)] for _ in range(2)]

for p in range(2):
    for n in range(2):
        for i in range(2):
            for j in range(2):
                for t in range(2):
                    C[p][n][i][j][t] = A[j][i] * B[p][t][n]

print("[", end="")
for n in range(2):
    for j in range(2):
        for p in range(2):
            for i in range(2):
                for t in range(2):
                    print(C[p][n][i][j][t], end=("; " if p == 1 and i == 1 and t == 1 else ", "))
print("]")