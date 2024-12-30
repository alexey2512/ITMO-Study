import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OneF {

    private static final long MOD = 1000000007;
    private static final long INF = 1000000001;
    private static final int CHUNK_SIZE = 316;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int[] elements = new int[n + 1];
        String[] input = br.readLine().split(" ");
        for (int i = 1; i <= n; i++)
            elements[i] = Integer.parseInt(input[i - 1]);
        List<List<Integer>> seqLists = new ArrayList<>(n + 1);
        List<List<Long>> seqSums = new ArrayList<>(n + 1);
        List<List<Pair>> chunkMaxes = new ArrayList<>(n + 1);
        for (int i = 0; i <= n; i++) {
            seqLists.add(new ArrayList<>());
            seqSums.add(new ArrayList<>());
            chunkMaxes.add(new ArrayList<>());
        }
        int maxLength = 0;
        seqLists.get(0).add((int) -INF);
        for (int i = 1; i <= n; i++)
            seqLists.get(i).add((int) INF);
        for (int i = 1; i <= n; i++) {
            int left = 0;
            int right = n;
            int mid = (right + left) / 2;
            int ans = 0;
            while (left <= right) {
                if (seqLists.get(mid).get(seqLists.get(mid).size() - 1) >= elements[i]) {
                    right = mid - 1;
                    ans = mid;
                } else {
                    left = mid + 1;
                }
                mid = (right + left) / 2;
            }
            if (ans > maxLength)
                maxLength = ans;
            if (seqLists.get(ans).get(0) == INF)
                seqLists.get(ans).set(0, elements[i]);
            else
                seqLists.get(ans).add(elements[i]);
            int chunkIdx = (seqLists.get(ans).size() - 1) / CHUNK_SIZE;
            if (chunkIdx == chunkMaxes.get(ans).size())
                chunkMaxes.get(ans).add(new Pair(elements[i], 0L));
            if (chunkMaxes.get(ans).get(chunkIdx).first < elements[i])
                chunkMaxes.get(ans).get(chunkIdx).first = elements[i];
            int curChunk = chunkMaxes.get(ans - 1).size() - 1;
            long locSum = 0;
            while (curChunk >= 0 && chunkMaxes.get(ans - 1).get(curChunk).first < elements[i]) {
                locSum += chunkMaxes.get(ans - 1).get(curChunk).second;
                locSum %= MOD;
                curChunk--;
            }
            if (curChunk >= 0) {
                for (int j = Math.min(seqLists.get(ans - 1).size() - 1, (curChunk + 1) * CHUNK_SIZE - 1); j >= 0; j--) {
                    if (seqLists.get(ans - 1).get(j) >= elements[i]) break;
                    locSum += seqSums.get(ans - 1).get(j);
                    locSum %= MOD;
                }
            }
            if (ans == 1)
                locSum = 1;
            seqSums.get(ans).add(locSum);
            chunkMaxes.get(ans).get(chunkIdx).second += locSum;
        }

        long result = 0;
        for (long sum : seqSums.get(maxLength)) {
            result += sum;
            result %= MOD;
        }
        System.out.println(result);
    }

    private static class Pair {
        int first;
        long second;

        Pair(int first, long second) {
            this.first = first;
            this.second = second;
        }
    }
}
