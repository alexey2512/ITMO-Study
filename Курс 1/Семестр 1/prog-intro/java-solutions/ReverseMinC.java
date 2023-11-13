import java.util.Arrays;
import java.util.Scanner;

public class ReverseMinC {
    public static void main(String[] args) {

        //var declaration and initialization
        Scanner sc_global = new Scanner(System.in);
        int[] arr_local = new int[1000000];
        int[][] arr_global = new int[1000000][];
        int len_local, el, max_len_local = 0, len_global = 0;

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
            if (len_local > max_len_local) {
                max_len_local = len_local;
            }
            len_global += 1;
        }

        //massive of minimums initialization
        int[] min_mas = new int[max_len_local];
        for (int i = 0; i < max_len_local; i++) {
            min_mas[i] = Integer.MAX_VALUE;
        }

        //matrix output
        for (int a = 0; a < len_global; a++) {
            for (int b = 0; b < arr_global[a].length; b++) {
                if (arr_global[a][b] < min_mas[b]) {
                    min_mas[b] = arr_global[a][b];
                }
                System.out.print(min_mas[b] + " ");
            }
            System.out.println();
        }
    }
}
