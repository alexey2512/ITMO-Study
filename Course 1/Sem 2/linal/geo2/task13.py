A = [

    [
        [[6, -3, -4],
         [2, 6, -2],
         [-3, 0, -2]],

        [[5, 6, 4],
         [1, 3, 1],
         [5, 3, 2]],

        [[3, 4, 4],
         [5, 4, -2],
         [1, -5, 2]]
    ],

    [
        [[4, -1, -5],
         [2, 3, -1],
         [-2, 4, -1]],

        [[-1, 3, 1],
         [-5, 5, 5],
         [-3, -6, 1]],

        [[0, 2, -2],
         [-6, 0, -4],
         [3, -1, -1]]
    ],

    [
        [[4, -6, -3],
         [-2, -1, 4],
         [3, -4, -5]],

        [[-2, 5, -4],
         [-2, -3, 1],
         [-4, -2, 0]],

        [[-4, 2, 3],
         [2, 4, -6],
         [1, -4, 5]]
    ]

]

B = [[[[0 for _ in range(3)]
       for _ in range(3)]
      for _ in range(3)]
     for _ in range(3)]

for j in range(3):
    for m in range(3):
        for i in range(3):
            for q in range(3):
                t = (A[j][m][i][q] + A[j][i][m][q] + A[m][j][i][q] + A[m][i][j][q] + A[i][j][m][q] + A[i][m][j][q])
                s = t / 6
                r = int(s) if int(s) == s / 6 else round(s, 2)
                B[j][m][i][q] = r

print("[", end="")
for j in range(3):
    for i in range(3):
        for m in range(3):
            for q in range(3):
                print(B[j][m][i][q], end=("; " if m == 2 and q == 2 else ", "))
print("]")