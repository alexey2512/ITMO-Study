from math import *

n, m, p = map(int, input().split())
A = list(map(int, input().split()))

honey_sum = 0
for i in range(n):
    t = A[i] // p
    if m > t:
        honey_sum += t * p
        m -= t
        A[i] = A[i] % p
    elif m == t:
        honey_sum += t * p
        m = 0
        break
    else:
        honey_sum += m * p
        m = 0
        break

if m != 0:
    A.sort(key=lambda x: -x)
    for Boch in A:
        honey_sum += Boch
        m -= 1
        if m == 0:
            break


print(honey_sum)

