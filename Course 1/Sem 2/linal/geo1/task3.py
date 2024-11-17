# task3

from itertools import *

# a[m][p][k][l][j]

A = [
    [
        [
            [[1, 4],
             [-2, 1]],
            [[5, 5],
             [1, -3]]
        ],
        [
            [[-2, -6],
             [3, -4]],
            [[5, 0],
             [3, -4]]
        ]
    ],

    [
        [
            [[-4, 5],
             [1, 0]],
            [[0, -2],
             [0, 5]]
        ],
        [
            [[-3, -2],
             [2, -1]],
            [[2, 3],
             [1, -2]]
        ]
    ]
]

B = [[[[[0 for _ in range(2)]
        for _ in range(2)]
       for _ in range(2)]
      for _ in range(2)]
     for _ in range(2)]

for m in range(2):
    for p in range(2):
        for k in range(2):
            for ll in range(2):
                for j in range(2):
                    s = 0
                    for perm in permutations([m, p, k, j]):
                        s += A[perm[0]][perm[1]][perm[2]][ll][perm[3]]
                    t = s / 24
                    B[m][p][k][ll][j] = int(t) if int(t) == t else round(t, 2)

print("[", end="")
for p in range(2):
    for ll in range(2):
        for m in range(2):
            for k in range(2):
                for j in range(2):
                    print(str(B[m][p][k][ll][j]) + ", ", end="")
        print("; ", end="")
print("]")
