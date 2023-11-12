import java.util.Scanner;

public class F {

    public static String insert(int[] mas, String vec) {
        StringBuilder s = new StringBuilder();
        int c = 0;
        for (int i = 0; i < mas.length; i++) {
            if (mas[i] >= 0) {
                s.append(mas[i]);
            } else {
                s.append(vec.charAt(c));
                c++;
            }
        }
        return s.toString();
    }

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
        int n = sc.nextInt(), k = sc.nextInt();
        int[][] M = new int[k][n];
        int[] cnt_of_absents = new int[k];
        int temp = 0;

        for (int i = 0; i < k; i++) {
            cnt_of_absents[i] = 0;
            for (int j = 0; j < n; j++) {
                temp = sc.nextInt();
                if (temp == 1) {
                    M[i][j] = 0;
                } else if (temp == 0) {
                    M[i][j] = 1;
                } else if (temp == -1) {
                    M[i][j] = -1;
                    cnt_of_absents[i]++;
                }
            }
        }

        int[] C = new int[1 << n];
        int t = 0;
        int num = 0;
        StringBuilder vec_j = new StringBuilder();
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < (1 << cnt_of_absents[i]); j++) {
                if (cnt_of_absents[i] != 0) {
                    vec_j.setLength(0);
                    vec_j.append(toBinary(j, cnt_of_absents[i]));
                    num = toDecimal(insert(M[i], vec_j.toString()));
                    if (C[num] == 0) {
                        C[num] = 1;
                        t++;
                    }
                } else {
                    num = toDecimal(insert(M[i], ""));
                    if (C[num] == 0) {
                        C[num] = 1;
                        t++;
                    }
                }
            }


        }
        if (t == (1 << n)) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }
    }
}
