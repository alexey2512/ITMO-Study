string = input()

mas = [string]

for i in range(1, len(string)):
    mas.append(string[i:] + string[:i])

mas.sort()

for i in mas:
    print(i[-1], end="")


