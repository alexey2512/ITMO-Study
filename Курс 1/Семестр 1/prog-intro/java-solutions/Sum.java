public class Sum {
    public static void main(String[] args) {

        int sum = 0;
        int begin;
        boolean flag;

        for (String arg : args) {
            String cur = arg + " ";
            flag = false;
            begin = 0;
            for (int i = 0; i < cur.length(); i++) {
                if (Character.isWhitespace(cur.charAt(i)) && flag) {
                    flag = false;
                    sum += Integer.parseInt(cur.substring(begin, i));
                } else if (!Character.isWhitespace(cur.charAt(i)) && !flag) {
                    flag = true;
                    begin = i;
                }
            }
        }

        System.out.println(sum);
    }
}