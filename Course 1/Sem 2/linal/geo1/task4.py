# task4

# a[q][k][p][m][i]

A = [
    [
        [
            [[0, 0],
             [1, -5]],
            [[0, -2],
             [5, 5]]
        ],
        [
            [[-5, -3],
             [2, -5]],
            [[-3, 3],
             [-2, 1]]
        ]
    ],
    [
        [
            [[1, -4],
             [-3, -6]],
            [[3, -2],
             [-4, 0]]
        ],
        [
            [[-2, -5],
             [1, -3]],
            [[-5, 1],
             [-5, 0]]
        ]
    ]
]

B = [[[[[0 for _ in range(2)]
        for _ in range(2)]
       for _ in range(2)]
      for _ in range(2)]
     for _ in range(2)]

for q in range(2):
    for k in range(2):
        for p in range(2):
            for m in range(2):
                for i in range(2):
                    t = (A[q][k][p][m][i] + A[q][k][p][i][m]) / 2
                    B[q][k][p][m][i] = int(t) if int(t) == t else round(t, 2)

print("[", end="")
for k in range(2):
    for m in range(2):
        for q in range(2):
            for p in range(2):
                for i in range(2):
                    print(str(B[q][k][p][m][i]) + ", ", end="")
        print("; ", end="")
print("]")
