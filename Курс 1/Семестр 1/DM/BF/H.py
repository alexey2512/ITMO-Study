def f(n):
    if n == 1:
        return "((A0|B0)|(A0|B0))"
    else:
        a = "A" + str(n-1)
        b = "B" + str(n-1)
        return "((" + f(n-1) + "|((" + a + "|" + a + ")|(" + b + "|" + b + ")))|(" + a + "|" + b + "))"

k = int(input())
print(f(k))