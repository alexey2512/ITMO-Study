
n = int(input())

Elements = []
cnt = 0

for i in range(n):

    F = list(map(int, input().split()))
    k = F[0]

    if k == 0:

        cnt += 1
        Elements.append([i, 0, [], [], 0, 0])

    else:

        S = list(map(int, input().split()))
        B = F[1:].copy()
        for j in range(len(B)):
            B[j] -= 1
        Elements.append([i, k, B, S, 0, 0])

max_depth_global = 0
res_vec = ""

for j in range(2**cnt):
    vec_j = bin(j)[2:]
    vec_j = "0" * (cnt - len(vec_j)) + vec_j
    c = 0
    last_val = 0
    max_depth_global = 0
    for i in range(n):
        if Elements[i][1] == 0:
            Elements[i][4] = int(vec_j[c])
            c += 1
        else:
            Args = Elements[i][2].copy()
            cur = ""
            max_depth_local = 0
            for k in Args:
                cur += str(Elements[k][4])
                max_depth_local = max(max_depth_local, Elements[k][5])
            num = int(cur, 2)
            Elements[i][4] = Elements[i][3][num]
            last_val = Elements[i][4]
            Elements[i][5] = max_depth_local + 1
            max_depth_global = max(max_depth_global, Elements[i][5])

    res_vec += str(last_val)

print(max_depth_global)
print(res_vec)









