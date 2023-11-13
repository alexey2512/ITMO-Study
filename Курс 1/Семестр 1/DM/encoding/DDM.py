alphabet = "abcdefghijklmnopqrstuvwxyz"
map = {}

for i in range(26):
    map[alphabet[i]] = i + 1

string = input()

for sym in string:
    print(map[sym], end=" ")
    temp = map[sym]
    for a in alphabet:
        if map[a] < temp:
            map[a] = map[a] + 1
    map[sym] = 1
