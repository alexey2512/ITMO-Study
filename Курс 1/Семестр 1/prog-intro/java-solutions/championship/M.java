import java.util.*;

public class M {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int t = sc.nextInt();
        int n, count;

        for (int q = 0; q < t; q++) {

            n = sc.nextInt();
            count = 0;
            int[] nums = new int[n];
            for (int j = 0; j < n; j++) {
                nums[j] = sc.nextInt();
            }

            Map<Integer, Integer> map = new HashMap<>();
            map.put(nums[n-1], 1);

            for (int j = n - 2; j > 0; j--) {
                for (int i = 0; i < j; i++) {
                    count += map.getOrDefault(2 * nums[j] - nums[i], 0);
                }
                map.put(nums[j], map.getOrDefault(nums[j], 0) + 1);
            }

            System.out.println(count);

        }

        sc.close();

    }
}
