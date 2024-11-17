n, k = map(int, input().split())
A = []

for _ in range(n):
    A.append(int(input()))

l = 0
r = 10**11 + 1

while r - l > 0.001:
    m = (r + l) / 2
    count = 0
    for i in A:
        count += int(i / m)
    if count >= k:
        l = m
    else:
        r = m

print(int(r))