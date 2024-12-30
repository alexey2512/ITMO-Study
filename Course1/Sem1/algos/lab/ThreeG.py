first = list(map(int, input().split()))
second = list(map(int, input().split()))

moves = 0

while first != [] and second != []:
    f = first.pop(0)
    s = second.pop(0)
    if f == 0 and s == 9:
        first.append(f)
        first.append(s)
    elif f == 9 and s == 0:
        second.append(f)
        second.append(s)
    else:
        if f > s:
            first.append(f)
            first.append(s)
        else:
            second.append(f)
            second.append(s)
    moves += 1
    if moves > 1000000:
        break

if moves > 1000000:
    print('botva')
else:
    if len(second) == 0:
        print('first', moves)
    else:
        print('second', moves)
