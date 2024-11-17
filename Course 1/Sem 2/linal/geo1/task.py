from itertools import permutations

def parity(a):
    c = 0
    for i in range(len(a) - 1):
        for j in range(i + 1, len(a)):
            if a[i] > a[j]:
                c += 1
    return 1 if c % 2 == 0 else -1


A = [[0, 10, -20],
     [-10, 0, 50],
     [20, -50, 0]]

B = [[0, 96, 0],
     [-96, 0, 48],
     [0, -48, 0]]

C = [[[[0 for _ in range(3)]
       for _ in range(3)]
      for _ in range(3)]
     for _ in range(3)]

for td in range(3):
    for l in range(3):
        for i in range(3):
            for m in range(3):
                C[td][l][i][m] = A[i][m] * B[l][td]

D = [[[[0.0 for _ in range(3)]
       for _ in range(3)]
      for _ in range(3)]
     for _ in range(3)]

for t in range(3):
    for l in range(3):
        for i in range(3):
            for m in range(3):
                s = 0
                td = [t, l, i, m]
                perms = list(permutations([0, 1, 2, 3]))
                for perm in perms:
                    s += parity(perm) * C[td[perm[0]]][td[perm[1]]][td[perm[2]]][td[perm[3]]]
                D[t][l][i][m] = s / 4

for t in range(3):
    for i in range(3):
        for l in range(3):
            for m in range(3):
                s = str(D[t][l][i][m])
                s = ' ' * (5 - len(s)) + s
                print(s, end=(" | " if m == 2 else " "))
        print()