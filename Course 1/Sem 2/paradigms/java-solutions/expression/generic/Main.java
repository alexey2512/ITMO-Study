package expression.generic;

import expression.exceptions.IncorrectExpressionException;


public class Main {
    public static void main(String[] args) {
        Tabulator tabulator = new GenericTabulator();
        String mode = args[0].substring(1);
        String expression = args[1];
        try {
            Object[][][] result = tabulator.tabulate(mode, expression, -2, 2, -2, 2, -2, 2);
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    for (int k = 0; k < 5; k++) {
                        System.out.println("for x = " + (i - 2) + ", y = " + (j - 2) + ", z = " + (k - 2) + ": " + result[i][j][k]);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
