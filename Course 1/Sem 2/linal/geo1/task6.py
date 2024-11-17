# task6

from itertools import *

# a[j][m][p][k][i]

def parity(mas):
    c = 0
    for i in range(len(mas) - 1):
        for j in range(i + 1, len(mas)):
            if mas[i] > mas[j]:
                c += 1
    return 1 if c % 2 == 0 else -1


A = [
    [
        [
            [[0, -1],
             [-2, -3]],
            [[-6, -5],
             [0, 2]]
        ],
        [
            [[4, -1],
             [-4, -5]],
            [[0, 2],
             [-6, -1]]
        ]
    ],
    [
        [
            [[-3, 3],
             [4, -3]],
            [[4, -3],
             [5, 3]]
        ],
        [
            [[-1, 5],
             [3, 4]],
            [[3, 0],
             [-3, -5]]
        ]
    ]
]

B = [[[[[0 for _ in range(2)]
        for _ in range(2)]
       for _ in range(2)]
      for _ in range(2)]
     for _ in range(2)]

for j in range(2):
    for m in range(2):
        for p in range(2):
            for k in range(2):
                for i in range(2):
                    s = 0
                    T = [j, m, p, i]
                    for perm in permutations([0, 1, 2, 3]):
                        s += parity(perm) * A[T[perm[0]]][T[perm[1]]][T[perm[2]]][k][T[perm[3]]]
                    t = s / 24
                    B[j][m][p][k][i] = int(t) if int(t) == t else round(t, 2)

print("[", end="")
for m in range(2):
    for k in range(2):
        for j in range(2):
            for p in range(2):
                for i in range(2):
                    print(str(B[j][m][p][k][i]) + ", ", end="")
        print("; ", end="")
print("]")
