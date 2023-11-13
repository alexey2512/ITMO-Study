arg_count = int(input())
Vectors = []
Values = []
for i in range(2**arg_count):
    a, b = input().split()
    Vectors.append(a)
    Values.append(int(b))

M = [Values]
P = [Values[0]]
for i in range(1, 2**arg_count):
    T = []
    for j in range(2**arg_count - i):
        T.append((M[-1][j] + M[-1][j+1]) % 2)
    M.append(T)
    P.append(T[0])

for i in range(2**arg_count):
    print(Vectors[i] + " " + str(P[i]))



