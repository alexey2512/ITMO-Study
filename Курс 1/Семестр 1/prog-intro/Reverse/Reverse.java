import java.util.Arrays;
import java.util.Scanner;

public class Reverse {
    public static void main(String[] args) {

            //var declaration and initialization
            Scanner sc_global = new Scanner(System.in);
            int[] arr_local = new int[1000000];
            int[][] arr_global = new int[1000000][];
            int len_local, el, len_global = 0;

            //matrix input
            while (sc_global.hasNextLine()) {
                Scanner sc_local = new Scanner(sc_global.nextLine());
                len_local = 0;
                while (sc_local.hasNextInt()) {
                    el = sc_local.nextInt();
                    arr_local[len_local] = el;
                    len_local++;
                }
                arr_global[len_global] = Arrays.copyOfRange(arr_local, 0, len_local);
                len_global++;
            }

            //matrix output
            for (int a = len_global - 1; a > -1; a--) {
                for (int b = arr_global[a].length - 1; b > -1; b--) {
                    System.out.print(arr_global[a][b] + " ");
                }
                System.out.println();
            }
    }
}
