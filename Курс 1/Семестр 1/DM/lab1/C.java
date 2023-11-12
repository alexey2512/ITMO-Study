import java.util.Scanner;

public class C {

    public static String toBinary(int num, int w) {
        int d = num;
        StringBuilder ans = new StringBuilder();
        while (d != 0) {
            ans.append((d % 2));
            d = d / 2;
        }
        ans.append("0".repeat(w - ans.length()));
        return ans.reverse().toString();
    }

    public static int toDecimal(String num) {
        int ans = 0;
        for (int i = 0; i < num.length(); i++) {
            ans += Integer.parseInt(Character.toString(num.charAt(i))) * (1 << (num.length() - i - 1));
        }
        return ans;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] elArgCounts = new int[n];
        int[][] elSons = new int[n][];
        int[][] elValues = new int[n][];
        int[] elCurValues = new int[n];
        int[] elDepths = new int[n];
        int k, cnt = 0;

        for (int i = 0; i < n; i++) {
            k = sc.nextInt();
            if (k == 0) {
                cnt++;
                elArgCounts[i] = 0;
                elCurValues[i] = 0;
                elDepths[i] = 0;
            } else {
                elArgCounts[i] = k;
                elCurValues[i] = 0;
                elDepths[i] = 0;
                int[] tempSons = new int[k];
                int[] tempValues = new int[1 << k];
                for (int j = 0; j < k; j++) {
                    tempSons[j] = sc.nextInt() - 1;
                }
                for (int j = 0; j < (1 << k); j++) {
                    tempValues[j] = sc.nextInt();
                }
                elSons[i] = tempSons;
                elValues[i] = tempValues;
            }
        }

        int max_depth_global = 0, max_depth_local = 0, c = 0, last_val = 0;
        StringBuilder res_nec = new StringBuilder(), cur = new StringBuilder();

        for (int j = 0; j < (1 << cnt); j++) {
            String vec_j = toBinary(j, cnt);
            c = 0;
            last_val = 0;
            max_depth_global = 0;
            for (int i = 0; i < n; i++) {
                if (elArgCounts[i] == 0) {
                    elCurValues[i] = Integer.parseInt(Character.toString(vec_j.charAt(c)));
                    c++;
                } else {
                    cur.setLength(0);
                    max_depth_local = 0;
                    for (int d: elSons[i]) {
                        cur.append(elCurValues[d]);
                        if (elDepths[d] > max_depth_local) max_depth_local = elDepths[d];
                    }
                    elCurValues[i] = elValues[i][toDecimal(cur.toString())];
                    last_val = elCurValues[i];
                    elDepths[i] = max_depth_local + 1;
                    if (elDepths[i] > max_depth_global) max_depth_global = elDepths[i];
                }
            }
            res_nec.append(last_val);
        }
        System.out.println(max_depth_global);
        System.out.println(res_nec);
    }
}
