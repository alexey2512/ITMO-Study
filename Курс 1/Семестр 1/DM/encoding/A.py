n = int(input())
A = list(map(int, input().split()))
Freqs = A.copy()
Indexes = []
for i in range(n):
    Indexes.append([i])

Code = [0] * n

for j in range(n-1):
    f_min_i = 0
    s_min_i = 1
    for i in range(1, n - j):
        if Freqs[i] < Freqs[f_min_i]:
            s_min_i = f_min_i
            f_min_i = i
        elif Freqs[f_min_i] <= Freqs[i] < Freqs[s_min_i]:
            s_min_i = i
    Freqs[min(f_min_i, s_min_i)] = Freqs[f_min_i] + Freqs[s_min_i]
    Freqs.pop(max(f_min_i, s_min_i))
    merged = Indexes[f_min_i] + Indexes[s_min_i]
    Indexes = (Indexes[:min(f_min_i, s_min_i)] +
               [merged] +
               Indexes[min(f_min_i, s_min_i) + 1: max(f_min_i, s_min_i)] +
               Indexes[max(f_min_i, s_min_i) + 1:])
    for index in merged:
        Code[index] += 1

sm = 0
for i in Indexes[0]:
    sm += A[i] * Code[i]

print(sm)




