bwt_string = input()
sorted_string = sorted(bwt_string)
n = len(bwt_string)

mas = [""] * n

for i in range(n):
    for j in range(n):
        mas[j] = bwt_string[j] + mas[j]
    mas.sort()

print(mas[0])