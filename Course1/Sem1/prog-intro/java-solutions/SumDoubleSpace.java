public class SumDoubleSpace {
    public static void main(String[] args) {

        double sum = 0;
        int begin;
        boolean flag;

        for (String arg : args) {
            String cur = arg + " ";
            flag = false;
            begin = 0;
            for (int i = 0; i < cur.length(); i++) {
                if (Character.isSpaceChar(cur.charAt(i)) && flag) {
                    flag = false;
                    sum += Double.parseDouble(cur.substring(begin, i));
                } else if (!Character.isSpaceChar(cur.charAt(i)) && !flag) {
                    flag = true;
                    begin = i;
                }
            }
        }

        System.out.println(sum);
    }
}