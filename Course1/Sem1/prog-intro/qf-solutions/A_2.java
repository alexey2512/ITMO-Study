import java.util.Scanner;
import java.lang.Math;

public class A {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        double a = sc.nextDouble();
        double b = sc.nextDouble();
        double n = sc.nextDouble();

        System.out.println(2 * (int) Math.ceil((n - b) / (b - a)) + 1);

    }
}