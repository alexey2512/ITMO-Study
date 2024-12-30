from itertools import *


def increase(a):
    return a + 1


n = int(input())
A = list(map(int, input().split()))
S = sum(A)
I0 = list(range(n))

if n % 3 == 0:
    f = False
    for c1 in combinations(I0, n // 3):
        I1 = []
        s = 0
        for i in I0:
            if i in c1:
                s += A[i]
            else:
                I1.append(i)
        if s == S / 3:
            C1 = c1
            for c2 in combinations(I1, n // 3):
                I2 = []
                s = 0
                for i in I1:
                    if i in c2:
                        s += A[i]
                    else:
                        I2.append(i)
                if s == S / 3:
                    C2 = c2
                    C3 = I2
                    f = True
                    break
        if f:
            break
    if f:
        print(n // 3)
        print(*list(map(increase, C1)))
        print(n // 3)
        print(*list(map(increase, C2)))
        print(n // 3)
        print(*list(map(increase, C3)))
    else:
        print(-1)
else:
    print(-1)
