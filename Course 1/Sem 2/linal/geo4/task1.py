
A = [[2, 5, -6],
     [-6, 3, -5],
     [5, 1, 4]]

S = [[-11, 9, -21],
     [6, -5, 12],
     [4, -3, 7]]

B = [[0 for i in range(3)] for j in range(3)]

for t1 in range(3):
    for p1 in range(3):
        s = 0
        for t in range(3):
            for p in range(3):
                s += S[t1][t] * S[p1][p] * A[t][p]
        B[t1][p1] = s

print(B)
