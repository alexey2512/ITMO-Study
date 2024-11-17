# task2

# a[k][i][l]

A = [
    [[0, -3, 5],
     [3, -5, 5],
     [-6, 2, -2]],
    [[-5, 5, 3],
     [2, 1, -3],
     [-4, 2, 3]],
    [[-5, -1, 1],
     [-5, 5, -6],
     [-3, 1, -4]]
]

B = [[[0 for _ in range(3)]
      for _ in range(3)]
     for _ in range(3)]

for k in range(3):
    for i in range(3):
        for ll in range(3):
            t = (A[k][i][ll] + A[ll][i][k]) / 2
            B[k][i][ll] = int(t) if int(t) == t else round(t, 2)

print("[", end="")
for i in range(3):
    for k in range(3):
        for ll in range(3):
            print(str(B[k][i][ll]) + (", " if k != 2 or ll != 2 else "; "), end="")
print("]")
