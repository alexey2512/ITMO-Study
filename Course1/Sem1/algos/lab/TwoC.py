from math import *

def is_possible(A, B, n):
    D = (A - B)**2 + 1 - 2*(A + B)
    h_n = (n - 1)*B - (n - 2)*A + (n - 1)*(n - 2)
    if D < 0:
        return True, h_n
    elif D == 0:
        k = (A + 3 - B) / 2
        if int(k) == k and k >= 1 and k <= n:
            return False, 0
        else:
            return True, h_n

    else:
        i1 = (A + 3 - B - D**0.5) / 2
        i2 = (A + 3 - B + D ** 0.5) / 2
        if any(k >= 1 and k <= n for k in range(ceil(i1), floor(i2) + 1, 1)):
            return False, 0
        else:
            return True, h_n



n, A = map(float, input().split())
n = int(n)

first_B = 1
isposib, h = is_possible(A, first_B, n)
while not isposib:
    first_B *= 2
    isposib, h = is_possible(A, first_B, n)
else:
    r = first_B
    l = first_B / 2

while r - l > 0.00001:
    m = (r + l) / 2
    isposib, h = is_possible(A, m, n)
    if isposib:
        r = m
    else:
        l = m

isposib, h = is_possible(A, r, n)
print(round(h, 2))



