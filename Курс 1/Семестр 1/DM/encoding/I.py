n = int(input())
powers = []
for i in range(18):
    powers.append(1 << i)

if n == 1:
    string = input()
    vector = [0] * 200000
    curIndex = 3
    strIndex = 0
    while strIndex < len(string):
        if curIndex in powers:
            curIndex += 1
        vector[curIndex] = int(string[strIndex])
        strIndex += 1
        curIndex += 1
    for i in powers:
        xor = 0
        for index in range(curIndex):
            if index & i == i:
                xor = (xor + vector[index]) % 2
        vector[i] = xor
    print("".join(map(str, vector[1:curIndex])))

elif n == 2:
    string = input()
    vector = [0] * 200000
    n = len(string)
    for i in range(n):
        vector[i + 1] = int(string[i])
    toInvert = 0
    for i in powers:
        xor = 0
        for index in range(1, n + 1):
            if index & i == i:
                xor = (xor + vector[index]) % 2
        toInvert += xor * i
    vector[toInvert] = (1 + vector[toInvert]) % 2
    ans=""
    for index in range(1, n + 1):
        if index not in powers:
            # print(vector[index], end="", flush=True)
            ans += str(vector[index])
    print(ans)
