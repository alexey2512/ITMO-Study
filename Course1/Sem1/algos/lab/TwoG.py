from math import *

t = int(input())
A = []
for _ in range(t):
    A.append(list(map(int, input().split())))

for pair in A:
    n, m = pair[0], pair[1]
    k = m - (n*(n+1)/2)
    if k > 0:
        print("Crewmates")
        print(n)
    elif k == 0:
        print("Impostors")
        print(n)
    else:
        k = ((2*n + 1) - ((2*n + 1)**2 - 8*m)**0.5) / 2
        print("Impostors")
        print(ceil(k))
