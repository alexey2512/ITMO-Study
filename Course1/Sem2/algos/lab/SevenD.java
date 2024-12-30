import java.util.Arrays;
import java.util.Scanner;

public class SevenD {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int k = sc.nextInt();
        char[] chars = new char[k];
        Arrays.fill(chars, 'S');
        System.out.println(new String(chars));
        for (int i = 0; i < k - 1; i++) {
            chars[i]--;
            chars[i + 1] += 31;
            System.out.println(new String(chars));
            chars[i]++;
            chars[i + 1] -= 31;
        }
    }
}
