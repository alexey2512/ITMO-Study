def print_result(r: str):
    with open("check.out", "w") as write:
        write.write(r)
    exit(0)


def set_to_number(arg_set: set[int]):
    number = 0
    for i_ in range(1, 10):
        if i_ in arg_set:
            number += 2 ** (i_ - 1)
    return number


def get_nth(number: int, index: int):
    return (number >> index) & 1


def length(number: int):
    return sum(get_nth(number, i_) for i_ in range(10))


def set_minus(arg1: int, arg2: int):
    res = arg1
    for i_ in range(10):
        if get_nth(arg1, i_) == get_nth(arg2, i_) == 1:
            res -= 2 ** i_
    return res


with open("check.in", "r") as read:
    n, m = map(int, read.readline().split())
    sets = set()
    for _ in range(m):
        st = set(list(map(int, read.readline().split()))[1:])
        sets.add(set_to_number(st))

if 0 not in sets:
    print_result("NO\n")

for s in sets:
    if s == 0:
        continue
    for i in range(10):
        if get_nth(s, i) == 0:
            continue
        if s - 2 ** i not in sets:
            print_result("NO\n")

for a in sets:
    for b in sets:
        if length(a) <= length(b):
            continue
        exists = False
        dif = set_minus(a, b)
        for i in range(10):
            if get_nth(dif, i) == 0:
                continue
            if b + 2 ** i in sets:
                exists = True
                break
        if not exists:
            print_result("NO\n")

print_result("YES\n")
