import java.util.Scanner;

public class ThreeA {
    public static void main(String[] args) {
        int[] stack = new int[1 << 20];
        int size = 0;
        Scanner sc = new Scanner(System.in);
        label: while (true) {
            String command = sc.next();
            switch (command) {
                case "push":
                    int n = sc.nextInt();
                    stack[size++] = n;
                    System.out.println("ok");
                    break;
                case "pop":
                    System.out.println(stack[--size]);
                    break;
                case "back":
                    System.out.println(stack[size - 1]);
                    break;
                case "size":
                    System.out.println(size);
                    break;
                case "clear":
                    size = 0;
                    System.out.println("ok");
                    break;
                case "exit":
                    System.out.println("bye");
                    break label;
            }
        }
    }
}
