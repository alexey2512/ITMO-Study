alphabet = "abcdefghijklmnopqrstuvwxyz"
map = {}

for i in range(26):
    map[alphabet[i]] = i

cnt = 26
string = input()

frontIndex = 0

while frontIndex < len(string):
    temp = frontIndex + 1
    while map.get(string[frontIndex:temp], "") != "" and temp < len(string):
        temp += 1
    if map.get(string[frontIndex:temp], "") != "" and temp == len(string):
        print(map[string[frontIndex:temp]])
        frontIndex = temp
    elif map.get(string[frontIndex:temp], "") == "" and temp < len(string):
        print(map[string[frontIndex:temp - 1]], end=" ")
        map[string[frontIndex:temp]] = cnt
        cnt += 1
        frontIndex = temp - 1
    else:
        print(map[string[frontIndex:temp - 1]], end=" ")
        print(map[string[temp - 1]])
        frontIndex = temp


