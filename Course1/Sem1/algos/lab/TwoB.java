import java.awt.*;
import java.util.*;
import java.util.List;

public class TwoB {
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

        int l1, r1, m1, l2, r2, m2;
        for (int cur: K) {
            l1 = 0;
            r1 = n + 1;
            while (r1 - l1 > 1 && N.get(l1) != cur) {
                m1 = l1 + (r1 - l1) / 2;
                if (cur > N.get(m1)) {
                    l1 = m1;
                } else {
                    r1 = m1;
                }
            }
            l2 = 0;
            r2 = n + 1;
            while (r2 - l2 > 1 && N.get(r2) != cur) {
                m2 = l2 + (r2 - l2) / 2;
                if (cur >= N.get(m2)) {
                    l2 = m2;
                } else {
                    r2 = m2;
                }
            }
            if (N.get(l1) == cur || N.get(r1) == cur || N.get(l2) == cur || N.get(r2) == cur) {
                if (N.get(l1) == cur) {
                    System.out.print(l1 + " ");
                } else {
                    System.out.print(r1 + " ");
                }
                if (N.get(r2) == cur) {
                    System.out.println(r2);
                } else {
                    System.out.println(l2);
                }
            } else {
                System.out.println(0);
            }
        }

    }
}

