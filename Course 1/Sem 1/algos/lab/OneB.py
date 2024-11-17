def sort(mas):
    if len(mas) <= 1:
        return mas, 0
    left, l = sort(mas[:len(mas) // 2])
    right, r = sort(mas[len(mas) // 2:])

    res = []
    i = j = count = 0
    n = len(left)
    m = len(right)
    while i < n and j < m:
        if left[i] <= right[j]:
            res.append(left[i])
            i += 1
        else:
            res.append(right[j])
            count += len(left) - i
            j += 1

    res.extend(left[i:])
    res.extend(right[j:])

    return res, l + r + count


k = int(input())
A = list(map(int, input().split()))
S = 0
Prefs = [0]
for i in range(k):
    S += A[i]
    Prefs.append(S)
A, cnt = sort(A)
print(cnt)
