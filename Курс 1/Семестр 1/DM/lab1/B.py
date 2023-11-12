from math import *

def comb(a, b):
    return factorial(a) // factorial(b) // factorial(a - b)

def isNotZeroPres(l, s):
    if s[0] == "0":
        return 0
    else:
        return 1

def isNotOnePres(l, s):
    if s[-1] == "1":
        return 0
    else:
        return 1

def isNotSelfDual(l, s):
    if l == 0:
        return 1
    else:
        f = 0
        for i in range(2**l // 2):
            if s[i] == s[2**l - i - 1]:
                f = 1
                break
        return f

def isNotMonotonous(l, s):
    f = 0
    for i in range(2**l):
        for j in range(i + 1):
            if s[i] == "0" and s[j] == "1" and comb(i, j) % 2 == 1:
                f = 1
                break
    return f

def isNotLinear(l, s):
    f = 0
    P = [0]
    for i in range(l):
        P.append(2**i)
    for i in range(2**l):
        c = 0
        for j in range(i + 1):
            if s[j] == "1" and comb(i, j) % 2 == 1:
                c += 1
        if c % 2 == 1 and not i in P:
            f = 1
            break
    return f


n = int(input())
A = []
for i in range(n):
    cnt, string = input().split()
    A.append([int(cnt), string])

Post = [0, 0, 0, 0, 0]
for i in range(n):
    a, b = A[i][0], A[i][1]
    Post[0] += isNotZeroPres(a, b)
    Post[1] += isNotOnePres(a, b)
    Post[2] += isNotLinear(a, b)
    Post[3] += isNotMonotonous(a, b)
    Post[4] += isNotSelfDual(a, b)

if 0 in Post:
    print("NO")
else:
    print("YES")





