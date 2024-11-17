A = [

    [[-5, -3, 3],
     [2, 4, 2],
     [4, -1, -1]],

    [[5, -3, 0],
     [0, 0, -6],
     [-3, 2, 5]],

    [[-5, 0, 0],
     [-1, -2, 0],
     [-4, 6, -6]]

]

B = [[[0 for _ in range(3)] for _ in range(3)] for _ in range(3)]

for j in range(3):
    for i in range(3):
        for p in range(3):
            B[j][i][p] = (A[j][i][p] - A[p][i][j]) / 2

print("[", end="")
for i in range(3):
    for j in range(3):
        for p in range(3):
            print(B[j][i][p], end=("; " if j == 2 and p == 2 else ", "))
print("]")
