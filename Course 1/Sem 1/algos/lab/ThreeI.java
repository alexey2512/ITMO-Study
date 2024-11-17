import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ThreeI {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int N = Integer.parseInt(br.readLine());
            long[] individual = new long[300000];
            long[] common = new long[300000];
            long sum = 0;
            int front = 0;
            int size = 0;
            int type;
            long a, x, y;
            for (int i = 0; i < N; i++) {
                String[] tmp = br.readLine().split(" ");
                type = Integer.parseInt(tmp[0]);
                switch (type) {
                    case 1:
                        a = Integer.parseInt(tmp[1]);
                        individual[front + size] = a;
                        size++;
                        break;
                    case 2:
                        x = Integer.parseInt(tmp[1]);
                        y = Integer.parseInt(tmp[2]);
                        individual[front] += x;
                        common[front + 1] += y;
                        common[front + size] -= y;
                        break;
                    case 3:
                        sum += common[front];
                        System.out.println(sum + individual[front]);
                        front++;
                        size--;
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
