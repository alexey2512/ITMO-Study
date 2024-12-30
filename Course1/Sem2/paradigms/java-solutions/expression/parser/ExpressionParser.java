package expression.parser;

import expression.*;

public class ExpressionParser extends BaseParser implements TripleParser {

    public ExpressionParser() {
        super(new StringSource(""));
    }

    @Override
    public TripleExpression parse(String expression) {
        setSource(new StringSource(expression));
        return parseBitOr();
    }

    private CommonExpression parseBitOr() {
        CommonExpression temp = parseBitXor();
        while (true) {
            if (take('|')) {
                temp = new BitOr(temp, parseBitXor());
            } else {
                return temp;
            }
        }
    }

    private CommonExpression parseBitXor() {
        CommonExpression temp = parseBitAnd();
        while (true) {
            if (take('^')) {
                temp = new BitXor(temp, parseBitAnd());
            } else {
                return temp;
            }
        }
    }

    private CommonExpression parseBitAnd() {
        CommonExpression temp = parseThirdPriorityOperations();
        while (true) {
            if (take('&')) {
                temp = new BitAnd(temp, parseThirdPriorityOperations());
            } else {
                return temp;
            }
        }
    }

    private CommonExpression parseThirdPriorityOperations() {
        CommonExpression temp = parseSecondPriorityOperations();
        while (true) {
            if (take('+')) {
                temp = new Add(temp, parseSecondPriorityOperations());
            } else if (take('-')) {
                temp = new Subtract(temp, parseSecondPriorityOperations());
            } else {
                return temp;
            }
        }
    }

    private CommonExpression parseSecondPriorityOperations() {
        skipWhitespaces();
        CommonExpression temp = parseFirstPriorityOperations();
        skipWhitespaces();
        while (true) {
            if (take('*')) {
                skipWhitespaces();
                temp = new Multiply(temp, parseFirstPriorityOperations());
                skipWhitespaces();
            } else if (take('/')) {
                skipWhitespaces();
                temp = new Divide(temp, parseFirstPriorityOperations());
                skipWhitespaces();
            } else {
                return temp;
            }
        }
    }

    private CommonExpression parseFirstPriorityOperations() {
        CommonExpression temp;
        if (take('-')) {
            if (take('(')) {
                temp = new Negate(parseBitOr());
                expect(')');
                return temp;
            } else if (skipWhitespaces()) {
                temp = new Negate(parseFirstPriorityOperations());
                return temp;
            } else {
                temp = parseVarConst("-");
                if (temp instanceof Const) {
                    return temp;
                }
                return new Negate(temp);
            }
        } else if (take('(')) {
            temp = parseBitOr();
            expect(')');
            return temp;
        } else if (take('~')) {
            skipWhitespaces();
            temp = parseFirstPriorityOperations();
            skipWhitespaces();
            return new Not(temp);
        } else {
            temp = parseVarConst("");
            return temp;
        }
    }

    private CommonExpression parseVarConst(String prefix) {
        StringBuilder sb = new StringBuilder();
        if (between('0', '9')) {
            sb.append(prefix);
            sb.append(take());
            while (between('0', '9')) {
                sb.append(take());
            }
            return new Const(Integer.parseInt(sb.toString()));
        } else if (between('x', 'z')) {
            sb.append(take());
            return new Variable(sb.toString());
        } else if (between('-', '-')) {
            return parseFirstPriorityOperations();
        } else {
            throw error("Incorrect expression ");
        }

    }

}
