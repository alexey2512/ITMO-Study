from decimal import *

getcontext().prec = 2000
alphabet = "abcdefghijklmnopqrstuvwxyz"
n = int(input())
counts = list(map(int, input().split()))
length = sum(counts)
st = input()
num = Decimal(int(st, 2)) / Decimal(1 << len(st))
left_borders = []
cur_lefts = []
right_borders = []
cur_rights = []
sm = 0

for i in range(n):
    left_borders.append(Decimal(sm / length))
    cur_lefts.append(Decimal(sm / length))
    sm += Decimal(counts[i])
    right_borders.append(Decimal(sm / length))
    cur_rights.append(Decimal(sm / length))

for i in range(length):
    for j in range(n):
        if Decimal(cur_lefts[j]) <= Decimal(num) < Decimal(cur_rights[j]):
            print(alphabet[j], end="")
            delta = Decimal(cur_rights[j] - cur_lefts[j])
            cur_lefts[0] = Decimal(cur_lefts[j])
            for k in range(n - 1):
                cur_lefts[k + 1] = Decimal(Decimal(cur_lefts[0]) +
                                           Decimal(Decimal(delta) * Decimal(left_borders[k + 1])))
                cur_rights[k] = Decimal(Decimal(cur_lefts[0]) +
                                        Decimal(Decimal(delta) * Decimal(left_borders[k + 1])))
            cur_rights[-1] = cur_lefts[0] + delta
            break
