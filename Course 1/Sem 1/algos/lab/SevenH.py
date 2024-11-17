from itertools import *

n = int(input())
A = list(map(int, input().split()))
S0 = sum(A)
I0 = list(range(n))

f = False

for r1 in range(1, len(I0) - 2):
    for c1 in combinations(I0, r1):
        I1 = []
        S1 = 0
        for i in I0:
            if i in c1:
                S1 += A[i]
            else:
                I1.append(i)
        if S1 == S0 / 4:
            C1 = c1
            for r2 in range(1, len(I1) - 1):
                for c2 in combinations(I1, r2):
                    I2 = []
                    S2 = 0
                    for i in I1:
                        if i in c2:
                            S2 += A[i]
                        else:
                            I2.append(i)
                    if S2 == S1:
                        C2 = c2
                        for r3 in range(1, len(I2)):
                            for c3 in combinations(I2, r3):
                                I3 = []
                                S3 = 0
                                for i in I2:
                                    if i in c3:
                                        S3 += A[i]
                                    else:
                                        I3.append(i)
                                if S3 == S2:
                                    C3 = c3
                                    f = True
                                    C4 = []
                                    for i in I0:
                                        if i not in C1 and i not in C2 and i not in C3:
                                            C4.append(i)
                                    break
                            if f:
                                break
                    if f:
                        break
                if f:
                    break
        if f:
            break
    if f:
        break

if f:
    print(len(C1))
    for i in C1:
        print(i + 1, end=" ")
    print()
    print(len(C2))
    for i in C2:
        print(i + 1, end=" ")
    print()
    print(len(C3))
    for i in C3:
        print(i + 1, end=" ")
    print()
    print(len(C4))
    for i in C4:
        print(i + 1, end=" ")
    print()
else:
    print(-1)




