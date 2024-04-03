import java.util.Scanner;

public class J {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int[][] matrix = new int[n][];

        for (int i = 0; i < n; i++) {
            String string = sc.next();
            int[] arr = new int[n];
            for (int j = 0; j < n; j++) {
                arr[j] = Integer.parseInt(String.valueOf(string.charAt(j)));
            }
            matrix[i] = arr;
        }

        for (int k = 0; k < n; k++) {
            for (int j = 0; j < n; j++) {
                if (matrix[k][j] > 0) {
                    System.out.print(1);
                    for (int i = 0; i < n; i++) {
                        matrix[k][i] = (matrix[k][i] - matrix[j][i] + 10) % 10;
                    }
                } else {
                    System.out.print(0);
                }
            }
            System.out.println();
        }
    }
}
