A = [

    [
        [[1, 1, 2],
         [-3, -3, 2],
         [1, -3, -2]],

        [[-3, -3, 4],
         [-1, -2, 1],
         [6, 4, 3]],

        [[3, 4, -5],
         [1, 1, -6],
         [1, -3, 2]]
    ],

    [
        [[-5, -5, 1],
         [2, 3, -1],
         [2, 0, -6]],

        [[5, 2, -2],
         [4, 3, 2],
         [-1, 1, 3]],

        [[-1, -1, -4],
         [5, -4, -3],
         [-2, -3, -1]]
    ],

    [
        [[-3, 1, 0],
         [3, -4, -2],
         [-5, 2, -1]],

        [[0, -3, 6],
         [3, -2, 1],
         [0, -5, 0]],

        [[5, -3, -2],
         [3, -5, 2],
         [3, 1, 6]]
    ]

]

B = [[[[0 for _ in range(3)]
       for _ in range(3)]
      for _ in range(3)]
     for _ in range(3)]

for k in range(3):
    for p in range(3):
        for l in range(3):
            for j in range(3):
                t = (A[k][p][l][j] - A[k][p][j][l] - A[l][p][k][j] + A[l][p][j][k] + A[j][p][k][l] - A[j][p][l][k])
                s = t / 6
                r = int(s) if int(s) == s / 6 else round(s, 2)
                B[k][p][l][j] = r

print("[", end="")
for k in range(3):
    for l in range(3):
        for p in range(3):
            for j in range(3):
                print(B[k][p][l][j], end=("; " if p == 2 and j == 2 else ", "))
print("]")

