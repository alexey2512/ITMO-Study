# task1
from itertools import *
# a[l][m][k][j]
A = [
    [
        [[2, 1, 0],
         [2, 6, 1],
         [5, 4, 1]],
        [[-5, 1, -4],
         [1, 2, 4],
         [4, -4, 5]],
        [[-2, 5, 0],
         [6, 5, 5],
         [3, -5, -6]]
    ],
    [
        [[-6, 2, -1],
         [-3, 0, -1],
         [-6, - 1, -3]],
        [[-2, 0, 1],
         [-3, -4, -4],
         [2, -5, -4]],
        [[4, -3, 5],
         [4, 3, 4],
         [-3, 5, 6]]
    ],
    [
        [[6, 6, 0],
         [-2, 5, -4],
         [5, 3, -6]],
        [[-5, -3, 0],
         [-1, -2, 0],
         [3, -3, 5]],
        [[3, -2, 2],
         [1, 0, 5],
         [5, -3, -1]]
    ]
]

B = [[[[0 for _ in range(3)]
       for _ in range(3)]
      for _ in range(3)]
     for _ in range(3)]

for i in range(3):
    for m in range(3):
        for k in range(3):
            for j in range(3):
                s = 0
                for perm in permutations([i, k, j]):
                    s += A[perm[0]][m][perm[1]][perm[2]]
                B[i][m][k][j] = format(s / 6, '.2f') if int(s / 6) != s / 6 else int(s / 6)

print("[", end="")
for i in range(3):
    for m in range(3):
        for k in range(3):
            for j in range(3):
                print(str(B[i][k][m][j]) + (", " if k != 2 or j != 2 else ""), end="")
        print("; ", end="")
print("]")
