import java.util.*;

public class TwoA {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        List<Integer> N = new ArrayList<>();
        int[] K = new int[k];

        N.add(Integer.MIN_VALUE);
        for (int i = 0; i < n; i++) {
            N.add(sc.nextInt());
        }
        N.add(Integer.MAX_VALUE);

        for (int i = 0; i < k; i++) {
            K[i] = sc.nextInt();
        }

        Collections.sort(N);

        int l, r, m;
        for (int key: K) {
            l = 0;
            r = n + 1;
            while (r - l > 1) {
                m = l + (r - l) / 2;
                if (key < N.get(m)) {
                    r = m;
                } else {
                    l = m;
                }
            }
            if (N.get(l) == key) {
                System.out.println("YES");
            } else {
                System.out.println("NO");
            }
        }
    }
}
