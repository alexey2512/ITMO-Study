# task8

# a[i][l][m]

A = [

    [[-3, 3],
     [-5, 2]],

    [[1, 3],
     [-4, 3]]

]

B = [[[0 for _ in range(2)] for _ in range(2)] for _ in range(2)]

for i in range(2):
    for ll in range(2):
        for m in range(2):
            t = (A[i][ll][m] - A[ll][i][m]) / 2
            B[i][ll][m] = int(t) if int(t) == t else round(t, 2)

print("[", end="")
for ll in range(2):
    for i in range(2):
        for m in range(2):
            print(str(B[i][ll][m]) + ", ", end="")
    print("; ", end="")
print("]")
