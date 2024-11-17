import numpy as np

A = [[[[[0 for _ in range(2)]
        for _ in range(2)]
       for _ in range(2)]
      for _ in range(2)]
     for _ in range(2)]

for p in range(2):
    for t in range(2):
        for m in range(2):
            for r in range(2):
                for k in range(2):
                    A[p][t][m][r][k] = 5 * (k + 1) - 3 * (m + 1) + 3 * (t + 1) - 5 * (p + 1)

for t in range(2):
    for r in range(2):
        for p in range(2):
            for m in range(2):
                for k in range(2):
                    s = str(A[p][t][r][m][k])
                    s = ' ' * (3 - len(s)) + s
                    print(s, end=" ")
        print()

