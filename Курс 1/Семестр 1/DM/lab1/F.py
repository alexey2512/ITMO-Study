def insert(mas, vec):
    s = ""
    c = 0
    for i in range(len(mas)):
        if mas[i] >= 0:
            s += str(mas[i])
        else:
            s += vec[c]
            c += 1
    return s

n, k = map(int, input().split())
M = []
for i in range(k):
    A = list(map(int, input().split()))
    cnt_of_absents = 0
    for j in range(n):
        if A[j] == 1:
            A[j] = 0
        elif A[j] == 0:
            A[j] = 1
        elif A[j] == -1:
            cnt_of_absents += 1
    M.append([A, cnt_of_absents])

C = [0] * 2**n
for i in range(k):
    for j in range(2**M[i][1]):
        if M[i][1] != 0:
            vec_j = bin(j)[2:]
            vec_j = "0" * (M[i][1] - len(vec_j)) + vec_j
            num = int(insert(M[i][0], vec_j), 2)
            C[num] = 1
        else:
            num = int(insert(M[i][0], ""), 2)
            C[num] = 1

if 0 in C:
    print("NO")
else:
    print("YES")

