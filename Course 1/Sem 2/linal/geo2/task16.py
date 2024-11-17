A = [

    [
        [[-6, -2, 2],
         [5, -2, -3],
         [4, -6, 6]],

        [[-4, 2, 0],
         [5, 3, 0],
         [4, -4, 1]],

        [[1, -3, 3],
         [-1, -4, -4],
         [5, -2, -6]]
    ],

    [
        [[2, 1, -3],
         [-2, 4, 6],
         [1, 0, -2]],

        [[-2, -6, -1],
         [3, 2, 5],
         [6, -4, -6]],

        [[5, 6, 1],
         [5, 6, 4],
         [3, 0, 3]]
    ],

    [
        [[5, -1, -3],
         [-2, 5, 1],
         [-4, -3, -1]],

        [[-5, 4, -4],
         [-5, 1, -6],
         [-5, 4, 1]],

        [[-2, -1, -1],
         [0, -1, 0],
         [6, -3, 1]]
    ]

]

# A[m][k][j][i]

B = [[[[0 for _ in range(3)] for _ in range(3)] for _ in range(3)] for _ in range(3)]

for m in range(3):
    for k in range(3):
        for j in range(3):
            for i in range(3):
                B[i][m][j][k] = A[m][k][j][i]

print("[", end="")
for m in range(3):
    for j in range(3):
        for k in range(3):
            for i in range(3):
                print(B[m][k][j][i], end=("; " if k == 2 and i == 2 else ", "))
print("]")
