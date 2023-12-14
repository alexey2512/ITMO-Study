from math import *
from decimal import *

getcontext().prec = 1000
alphabet = "abcdefghijklmnopqrstuvwxyz"
counts = {}
doli = {}

n = int(input())
string = input()

for i in range(n):
    counts[alphabet[i]] = 0
for sym in string:
    counts[sym] += 1

print(n)
for i in range(n):
    print(counts[alphabet[i]], end=" ")

sm = 0
for i in range(n):
    sm += Decimal(counts[alphabet[i]])
    doli[alphabet[i]] = Decimal(sm / len(string))

l = Decimal(0)
r = Decimal(1)
for sym in string:
    delta = Decimal(r - l)
    index = alphabet.find(sym)
    if index == 0:
        r = Decimal(l) + Decimal(delta) * Decimal(doli["a"])
    else:
        pre_sym = alphabet[index - 1]
        r = Decimal(l) + Decimal(delta) * Decimal(doli[sym])
        l = Decimal(l) + Decimal(delta) * Decimal(doli[pre_sym])

lg = Decimal(ceil(Decimal(log2(Decimal(1 / (r - l))))))
left_ans = Decimal(ceil(l * (2 ** lg) / 1))
right_ans = Decimal(floor(r * (2 ** lg) / 1))
if right_ans % 2 == 0:
    ans = right_ans
else:
    ans = left_ans
while ans % 2 == 0 and ans != 0:
    ans = ans // 2
    lg -= 1

s = bin(int(ans))[2:]
print()
print("0" * (int(lg - len(s)) + s))




