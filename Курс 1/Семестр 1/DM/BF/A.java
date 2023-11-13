import java.util.Scanner;

public class A {

    public static int isReflexive(int[][] Matrix, int size) {
        int f = 1;
        for (int i = 0; i < size; i++) {
            if (Matrix[i][i] == 0) {
                f = 0;
                break;
            }
        }
        return f;
    }

    public static int isAntiReflexive(int[][] Matrix, int size) {
        int f = 1;
        for (int i = 0; i < size; i++) {
            if (Matrix[i][i] == 1) {
                f = 0;
                break;
            }
        }
        return f;
    }

    public static int isSymmetrical(int[][] Matrix, int size) {
        int f = 1;
        for (int i = 1; i < size; i++) {
            for (int j = 0; j < i; j++) {
                if (Matrix[i][j] != Matrix[j][i]) {
                    f = 0;
                    break;
                }
            }
        }
        return f;
    }

    public static int isAntiSymmetrical(int[][] Matrix, int size) {
        int f = 1;
        for (int i = 1; i < size; i++) {
            for (int j = 0; j < i; j++) {
                if (Matrix[i][j] == 1 && Matrix[j][i] == 1) {
                    f = 0;
                    break;
                }
            }
        }
        return f;
    }

    public static int isTransitive(int[][] Matrix, int size) {
        int f = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    if (Matrix[i][j] == 1 && Matrix[j][k] == 1 && Matrix[i][k] == 0) {
                        f = 0;
                        break;
                    }
                }
            }
         }
        return f;
    }

    public static int[][] matrixComposition(int[][] M1, int[][] M2, int size) {
        int[][] res = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    if (M1[i][k] == 1 && M2[k][j] == 1) {
                        res[i][j] = 1;
                        break;
                    }
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {

        Scanner sc_g = new Scanner(System.in);
        Scanner sc_l;
        int n = Integer.parseInt(sc_g.nextLine());
        int[][] R = new int[n][n], S = new int[n][n];

        //R init
        for (int i = 0; i < n; i++) {
            sc_l = new Scanner(sc_g.nextLine());
            for (int j = 0; j < n; j++) {
                R[i][j] = sc_l.nextInt();
            }
        }

        //S init
        for (int i = 0; i < n; i++) {
            sc_l = new Scanner(sc_g.nextLine());
            for (int j = 0; j < n; j++) {
                S[i][j] = sc_l.nextInt();
            }
        }

        System.out.print(isReflexive(R, n) + " " +
                         isAntiReflexive(R, n) + " " +
                         isSymmetrical(R, n) + " " +
                         isAntiSymmetrical(R, n) + " " +
                         isTransitive(R, n) + "\n");

        System.out.print(isReflexive(S, n) + " " +
                         isAntiReflexive(S, n) + " " +
                         isSymmetrical(S, n) + " " +
                         isAntiSymmetrical(S, n) + " " +
                         isTransitive(S, n) + "\n");

        int[][] com = matrixComposition(R, S, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 1; j++) {
                System.out.print(com[i][j] + " ");
            }
            System.out.print(com[i][n - 1] + "\n");
        }
    }
}