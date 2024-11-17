# finding matrix T and T^(-1) using online calculator for solving systems of linear equations

S = [[-13, 1, -2, 9],
     [2, -1, -1, -3],
     [22, 6, 17, -5],
     [-17, -5, -14, 4]]

A = [[6, 4, 6, 5],
     [-5, -1, 3, -1],
     [4, 2, 3, 0],
     [1, -1, -1, 3]]

B = [[0 for _ in range(4)] for _ in range(4)]

for p1 in range(4):
    for l1 in range(4):
        s = 0
        for p in range(4):
            for l in range(4):
                s += S[p1][p] * S[l1][l] * A[p][l]
        B[p1][l1] = s

print("[", end="")
for p in range(4):
    for l in range(4):
        print(B[p][l], end=("; " if l == 3 else ", "))
print("]")
