read = open("schedule.in", "r")
write = open("schedule.out", "w")

tasks = []
n = int(read.readline())
for i in range(n):
    x = list(map(int, read.readline().split()))
    tasks.append(x)

read.close()

tasks.sort(key=lambda x: (-x[1], x[0]))

p = [i for i in range(n + 1)]
m = [i for i in range(n + 1)]
r = [0 for _ in range(n + 1)]


def root(o):
    if o == p[o]:
        return o
    p[o] = root(p[o])
    return p[o]


def unite(a, b):
    a = root(a)
    b = root(b)
    if a == b:
        return
    if r[a] >= r[b]:
        p[b] = a
        m[a] = min(m[a], m[b])
        if r[a] == r[b]:
            r[a] += 1
    else:
        p[a] = b
        m[b] = min(m[a], m[b])


fine = 0
for d, w in tasks:
    d = min(d, n)
    free = m[root(d)]
    if free == 0:
        fine += w
    else:
        unite(free, free - 1)

write.write(str(fine) + "\n")
write.close()
