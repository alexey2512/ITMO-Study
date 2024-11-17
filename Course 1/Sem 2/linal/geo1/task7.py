# task7

# a[j][i][q][m][k]

A = [
    [
        [
            [[3, -2],
             [-1, -1]],
            [[5, -1],
             [3, 4]]
        ],
        [
            [[0, 2],
             [-5, 2]],
            [[-3, -1],
             [5, -3]]
        ]
    ],
    [
        [
            [[3, 3],
             [-2, 0]],
            [[-6, 0],
             [2, 1]]
        ],
        [
            [[-4, -5],
             [-6, 3]],
            [[4, 0],
             [-1, 0]]
        ]
    ]
]

B = [[[[[0 for _ in range(2)]
        for _ in range(2)]
       for _ in range(2)]
      for _ in range(2)]
     for _ in range(2)]

for j in range(2):
    for i in range(2):
        for q in range(2):
            for m in range(2):
                for k in range(2):
                    t = (A[j][i][q][m][k] - A[j][k][q][m][i]) / 2
                    B[j][i][q][m][k] = int(t) if int(t) == t else round(t, 2)

print("[", end="")
for i in range(2):
    for m in range(2):
        for j in range(2):
            for q in range(2):
                for k in range(2):
                    print(str(B[j][i][q][m][k]) + ", ", end="")
        print("; ", end="")
print("]")
