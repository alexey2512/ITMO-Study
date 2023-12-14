alphabet = "abcdefghijklmnopqrstuvwxyz"
mp = []

for i in range(26):
    mp.append(alphabet[i])

n = int(input())
A = list(map(int, input().split()))

cnt = 26

print(mp[A[0]], end="")
mp.append(mp[A[0]])

for i in range(1, len(A)):
    mp[cnt] = mp[cnt] + mp[A[i]][0]
    cnt += 1
    print(mp[A[i]], end="")
    mp.append(mp[A[i]])




