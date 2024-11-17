import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class SevenE {
    private static final int MAX_LENGTH = 1000;
    private static final char[] CHARS;

    static {
        CHARS = new char[26];
        for (int j = 97; j <= 122; j++) {
            CHARS[j - 97] = (char) j;
        }
    }

    private static String generate(Random random, int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(CHARS[random.nextInt(CHARS.length)]);
        return sb.toString();
    }

    private static int hash(String s, int p, int q) {
        int hash = 0;
        for (int i = 0; i < s.length(); i++)
            hash = (int) (((long) hash * p + (long) s.charAt(i)) % (long) q);
        return hash;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int p = sc.nextInt();
        int q = sc.nextInt();
        Map<Integer, String> map = new HashMap<>();
        Random random = new Random();
        while (true) {
            String str = generate(random, random.nextInt(MAX_LENGTH) + 1);
            int hash = hash(str, p, q);
            if (map.containsKey(hash) && !str.equals(map.get(hash))) {
                System.out.println(map.get(hash) + "\n" + str);
                break;
            } else
                map.put(hash, str);
        }
    }
}
