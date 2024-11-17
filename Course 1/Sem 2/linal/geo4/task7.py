A = [

    [[-4, -3, -2],
     [-6, -1, 1],
     [-6, 3, -6]],

    [[-5, 0, 0],
     [6, -3, 3],
     [-3, -3, 4]],

    [[-4, -5, 5],
     [3, -4, 1],
     [-5, -4, -2]]

]

B = [[[0 for _ in range(3)] for _ in range(3)] for _ in range(3)]

for k in range(3):
    for p in range(3):
        for i in range(3):
            B[k][p][i] =(A[k][p][i] + A[p][k][i]) / 2

print("[", end="")
for p in range(3):
    for k in range(3):
        for i in range(3):
            print(B[k][p][i], end=("; " if k == 2 and i == 2 else ", "))
print("]")
