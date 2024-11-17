# task5

from itertools import *


# a[j][i][p][q]

def parity(mas):
    c = 0
    for i in range(len(mas) - 1):
        for j in range(i + 1, len(mas)):
            if mas[i] > mas[j]:
                c += 1
    return 1 if c % 2 == 0 else -1


A = [
    [
        [[0, -2, -6],
         [-1, -2, -5],
         [-4, -1, -1]],
        [[0, 3, 2],
         [-4, -5, -1],
         [0, 0, 2]],
        [[4, 0, 2],
         [-6, 1, 5],
         [-2, -5, 1]]
    ],
    [
        [[-1, -4, -4],
         [2, -5, -3],
         [-4, 5, 2]],
        [[-1, -1, 0],
         [1, -2, 4],
         [-4, -4, 0]],
        [[-5, -5, 0],
         [2, -1, -3],
         [6, -2, -2]]
    ],
    [
        [[6, -1, 4],
         [-2, 5, 2],
         [4, -6, 0]],
        [[1, 4, -5],
         [4, -2, -2],
         [-5, 1, -2]],
        [[-4, -3, -6],
         [-4, -1, 5],
         [-4, 0, 0]]
    ]

]

B = [[[[0 for _ in range(3)]
       for _ in range(3)]
      for _ in range(3)]
     for _ in range(3)]

for j in range(3):
    for i in range(3):
        for p in range(3):
            for q in range(3):
                s = 0
                T = [j, p, q]
                for perm in permutations([0, 1, 2]):
                    s += parity(perm) * A[T[perm[0]]][i][T[perm[1]]][T[perm[2]]]
                t = s / 6
                B[j][i][p][q] = int(t) if t == int(t) else round(t, 2)

print("[", end="")
for j in range(3):
    for p in range(3):
        for i in range(3):
            for q in range(3):
                print(str(B[j][i][p][q]) + ", ", end="")
        print("; ", end="")
print("]")
