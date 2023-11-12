import java.io.IOException;
import java.util.Arrays;

public class Reverse {
    public static void main(String[] args) {

        try {

            //var declaration and initialization
            TheSameScanner sc_global = new TheSameScanner(System.in, "UTF-8");
            int[] arr_local = new int[1000000];
            int[][] arr_global = new int[1000000][];
            int len_local, el, len_global = 0;

            //matrix input
            while (sc_global.hasNextLine()) {
                TheSameScanner sc_local = new TheSameScanner(sc_global.nextLine());
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

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
