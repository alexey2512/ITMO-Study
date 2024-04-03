package queue;

public class Tester {
    private static int testNumber = 1;

    public static void printTest(Object actual, Object expected) {
        int countDigits = Integer.toString(testNumber).length();
        String testOutPrefix = "Test #" + "0".repeat(3 - countDigits) + testNumber;
        if (actual.equals(expected)) {
            System.out.println(testOutPrefix + " passed");
        } else {
            System.err.println(testOutPrefix + " failed: expected '" + expected + "', actual '" + actual + "'");
        }
        testNumber++;
    }
}
