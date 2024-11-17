from itertools import permutations


def parity(a):
    res = 0
    for i in range(len(a) - 1):
        for c in range(i + 1, len(a)):
            if a[i] > a[c]:
                res += 1
    return 1 if res % 2 == 0 else -1


A = [

    [

        [
            [[2, -3],
             [4, 1]],

            [[3, -6],
             [2, 1]]
        ],

        [
            [[2, 4],
             [3, 4]],

            [[-3, -1],
             [0, 3]]
        ]

    ],

    [
        [
            [[-2, -6],
             [1, -6]],

            [[5, -4],
             [1, -5]]
        ],

        [
            [[-5, 3],
             [2, -2]],

            [[5, 1],
             [-2, 3]]
        ]

    ]

]

B = [[[[[0 for _ in range(2)]
        for _ in range(2)]
       for _ in range(2)]
      for _ in range(2)]
     for _ in range(2)]

for j in range(2):
    for q in range(2):
        for l in range(2):
            for p in range(2):
                for m in range(2):
                    s = 0
                    t = [j, l, p, m]
                    perms = list(permutations([0, 1, 2, 3]))
                    for perm in perms:
                        s += parity(perm) * A[t[perm[0]]][q][t[perm[1]]][t[perm[2]]][t[perm[3]]]
                    B[j][q][l][p][m] = s // 24 if int(s / 24) == s / 24 else round(s / 24, 2)


print("[", end="")
for q in range(2):
    for p in range(2):
        for j in range(2):
            for l in range(2):
                for m in range(2):
                    print(B[j][q][l][p][m], end=("; " if j == 1 and l == 1 and m == 1 else ", "))
print("]")
