B = [

    [
        [
            [[4, -2],
             [-2, 2]],
            [[0, 1],
             [3, 1]]
        ],

        [
            [[-1, 0],
             [-1, -3]],
            [[3, 1],
             [-3, -3]]
        ]
    ],

    [
        [
            [[0, 1],
             [2, 4]],
            [[0, -3],
             [-3, 2]]
        ],

        [
            [[1, 3],
             [3, -3]],
            [[-3, -1],
             [-3, -3]]
        ]
    ]

]

A = [-3, 3]

C = [[[[0 for _ in range(2)] for _ in range(2)] for _ in range(2)] for _ in range(2)]

for i in range(2):
    for m in range(2):
        for l in range(2):
            for j in range(2):
                C[i][m][l][j] = A[0] * B[i][m][j][0][l] + A[1] * B[i][m][j][1][l]

print("[", end="")
for i in range(2):
    for l in range(2):
        for m in range(2):
            for j in range(2):
                print(C[i][m][l][j], end=("; " if m == 1 and j == 1 else ", "))
print("]")