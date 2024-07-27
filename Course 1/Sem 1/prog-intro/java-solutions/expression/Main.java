package expression;

import expression.parser.ExpressionParser;
import expression.parser.StringSource;

public class Main {
    public static void main(String[] args) {

        ExpressionParser parser = new ExpressionParser();
        System.out.println(parser.parse("- ~ -193419584"));

    }
}