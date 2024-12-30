import java.sql.SQLOutput;
import java.util.Scanner;

public class ThreeB {
    public static void main(String[] args) {
        int[] queue = new int[1 << 20];
        Scanner sc = new Scanner(System.in);
        int front = 0;
        int size = 0;
        label: while (true) {
            String command = sc.next();
            switch (command) {
                case "push":
                    int n = sc.nextInt();
                    queue[(front + size++) % (1 << 20)] = n;
                    System.out.println("ok");
                    break;
                case "pop":
                    System.out.println(queue[front]);
                    front = (front + 1) % (1 << 20);
                    size--;
                    break;
                case "front":
                    System.out.println(queue[front]);
                    break;
                case "size":
                    System.out.println(size);
                    break;
                case "clear":
                    System.out.println("ok");
                    front = 0;
                    size = 0;
                    break;
                case "exit":
                    System.out.println("bye");
                    break label;
            }
        }
    }
}
