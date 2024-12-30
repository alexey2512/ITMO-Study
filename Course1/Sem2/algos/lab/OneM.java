import java.io.*;

public class OneM {

    public static final int size = 1 << 18;
    public static final int[] tree = new int[2 * size - 1];
    public static final int[] counts = new int[size];
    public static int sum;

    public static int get(int ql, int qr) {
        sum = 0;
        get(0, 0, size, ql, qr);
        return sum;
    }

    public static void get(int v, int l, int r, int ql, int qr) {
        if (l >= qr || r <= ql || v > 2 * size - 2) {
            return;
        }
        if (ql <= l && r <= qr) {
            sum += tree[v];
            return;
        }
        int m = (l + r) / 2;
        get(2 * v + 1, l, m, ql, qr);
        get(2 * v + 2, m, r, ql, qr);
    }


    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int n = Integer.parseInt(br.readLine());
            for (int i = 0; i < n; i++) {
                String[] str = br.readLine().split(" ");
                int x = Integer.parseInt(str[0]);
                int y = Integer.parseInt(str[1]);
                counts[get(0, x + 1)]++;
                int index = size - 1 + x;
                tree[index]++;
                while (index != 0) {
                    index = (index - 1) / 2;
                    tree[index] = tree[2 * index + 1] + tree[2 * index + 2];
                }
            }
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
            for (int i = 0; i < n; i++) {
                bw.write(counts[i] + "\n");
            }
            bw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
