import java.util.Scanner;

public class SixH {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = sc.nextInt();
        int[][] fingers = new int[n][5];
        int[] max = new int[n];
        int[] min = new int[n];
        for (int i = 0; i < 5; i++) fingers[0][i] = 1;
        max[0] = 4;
        min[0] = 0;
        if (n == 1) {
            System.out.println(1);
        } else {
            boolean f = true;
            int lastCount = 5;
            for (int i = 1; i < n; i++) {
                if (arr[i - 1] < arr[i]) {
                    min[i] = min[i - 1] + 1;
                    max[i] = 4;
                    lastCount = 5 - min[i];
                    for (int k = min[i]; k < 5; k++) fingers[i][k] = 1;
                    if (lastCount == 0) {
                        f = false;
                        break;
                    }
                } else if (arr[i - 1] > arr[i]) {
                    max[i] = max[i - 1] - 1;
                    min[i] = 0;
                    lastCount = max[i] + 1;
                    for (int k = 0; k < max[i] + 1; k++) fingers[i][k] = 1;
                    if (lastCount == 0) {
                        f = false;
                        break;
                    }
                } else {
                    if (lastCount == 1) {
                        for (int k = 0; k < 5; k++) {
                            if (fingers[i - 1][k] == 0) {
                                fingers[i][k] = 1;
                            }
                        }
                        lastCount = 4;
                        min[i] = fingers[i][0] == 0 ? 1 : 0;
                        max[i] = fingers[i][4] == 0 ? 3 : 4;
                    } else {
                        for (int k = 0; k < 5; k++) {
                            fingers[i][k] = 1;
                        }
                        min[i] = 0;
                        max[i] = 4;
                        lastCount = 5;
                    }
                }
            }
            if (f) {
                int[] ans = new int[n];
                ans[n - 1] = min[n - 1] + 1;
                for (int i = n - 2; i >= 0; i--) {
                    if (arr[i] < arr[i + 1]) {
                        for (int t = min[i]; t <= max[i]; t++) {
                            if (fingers[i][t] == 1 && t + 1 < ans[i + 1]) {
                                ans[i] = t + 1;
                                break;
                            }
                        }
                    } else if (arr[i] > arr[i + 1]) {
                        for (int t = min[i]; t <= max[i]; t++) {
                            if (fingers[i][t] == 1 && t + 1 > ans[i + 1]) {
                                ans[i] = t + 1;
                                break;
                            }
                        }
                    } else {
                        for (int t = min[i]; t <= max[i]; t++) {
                            if (fingers[i][t] == 1 && t + 1 != ans[i + 1]) {
                                ans[i] = t + 1;
                                break;
                            }
                        }
                    }
                }
                for (int i = 0; i < n; i++) {
                    System.out.print(ans[i] + " ");
                }
            } else {
                System.out.println(-1);
            }
        }
    }
}
