package expression.generic;

import expression.generic.bricks.*;
import expression.exceptions.*;
import expression.generic.calculators.Converter;
import expression.parser.BaseParser;
import expression.parser.StringSource;

public class ExpressionParser<T> extends BaseParser implements TripleParser<T> {

    private char curBinOp, curUnOp;
    private Converter<T> converter;
    private static final char[][] binOps = new char[][]{
            {'+', '-'},
            {'*', '/'}
    };

    public ExpressionParser() {
        super(new StringSource(""));
    }

    @Override
    public TripleExpression<T> parse(String expression, Converter<T> converter) throws IncorrectExpressionException {
        setSource(new StringSource(expression));
        this.converter = converter;
        TripleExpression<T> answer = parsePriority(0);
        skipWhitespaces();
        if (test(')')) {
            throw new IncorrectBracketSequenceException("no paired opening parenthesis found for '" + take() + "'" + " at position: " + getPos());
        } else if (isPrimitiveOperation()) {
            throw misBinArgs();
        } else if (isVarOrConst()) {
            throw misOp();
        } else if (!eof()) {
            throw lostSym(getCurrentChar());
        }
        return answer;
    }

    private TripleExpression<T> parsePriority(int priority) throws IncorrectExpressionException {
        TripleExpression<T> temp = switch (priority) {
            case 0 -> parsePriority(1);
            case 1 -> parseFirstPriority();
            default -> null;
        };
        while (true) {
            char op = '\0';
            for (int i = 0; i < binOps[priority].length; i++) {
                if (take(binOps[priority][i])) {
                    op = binOps[priority][i];
                }
            }
            curBinOp = op;
            temp = switch (op) {
                case '+' -> new Add<>(temp, parsePriority(1));
                case '-' -> new Subtract<>(temp, parsePriority(1));
                case '*' -> new Multiply<>(temp, parseFirstPriority());
                case '/' -> new Divide<>(temp, parseFirstPriority());
                default -> temp;
            };
            curBinOp = END;
            if (op == '\0') {
                return temp;
            }
        }

    }

    private TripleExpression<T> parseFirstPriority() throws IncorrectExpressionException {
        TripleExpression<T> temp;
        skipWhitespaces();
        if (take('-')) {
            curUnOp = '-';
            if (take('(')) {
                temp = new Negate<>(parsePriority(0));
                if (!take(')')) {
                    throw closPar();
                }
            } else if (skipWhitespaces()) {
                temp = new Negate<>(parseFirstPriority());
            } else {
                temp = parseVarConst("-");
                temp = (temp.getClass() == Const.class) ? temp : new Negate<>(temp);
            }
            curUnOp = END;
        } else if (take('(')) {
            temp = parsePriority(0);
            if (!take(')')) {
                throw closPar();
            }
        } else {
            temp = parseVarConst("");
        }
        skipWhitespaces();
        return temp;
    }

    private TripleExpression<T> parseVarConst(String prefix) throws IncorrectExpressionException {
        StringBuilder sb = new StringBuilder();
        if (between('0', '9')) {
            sb.append(prefix);
            while (between('0', '9') || test('.')) {
                sb.append(take());
            }
            try {
                return new Const<>(converter.convert(sb.toString()));
            } catch (NumberFormatException e) {
                throw new NonParsingConstantException("can not parse constant: " + sb);
            }
        } else if (between('x', 'z')) {
            return new Variable<>(take());
        } else if (test('-')) {
            return parseFirstPriority();
        } else if (test(')')) {
            if (curBinOp == END) {
                throw new IncorrectBracketSequenceException("empty brackets () at position: " + getPos());
            }
            throw misArg(" 2nd ", curBinOp);
        }  else if (isPrimitiveOperation() || eof()) {
            if (curUnOp != END) {
                throw misArg(" ", curUnOp);
            }
            if (curBinOp != END) {
                throw misArg(" 2nd ", curBinOp);
            }
            if (!eof()) {
                throw misBinArgs();
            }
        }
        throw lostSym(getCurrentChar());
    }


    private boolean isPrimitiveOperation() {
        char[] ops = new char[]{'~', '|', '^', '&', '+', '-', '*', '/'};
        boolean ans = false;
        for (char op : ops) {
            ans = ans || test(op);
        }
        return ans;
    }

    private boolean isVarOrConst() {
        return between('0', '9') || between('x', 'z') || test('-');
    }



    private IncorrectExpressionException closPar() throws IncorrectExpressionException {
        if (eof()) {
            throw new IncorrectBracketSequenceException("no paired closing parenthesis found for '" + ')' + "'" + " at position: " + getPos());
        } else if (isPrimitiveOperation()) {
            return misBinArgs();
        } else if (isVarOrConst()) {
            return misOp();
        }
        return lostSym(getCurrentChar());
    }

    private MissingArgumentException misBinArgs() {
        if (curBinOp == END) {
            return misArg(" 1st ", take());
        }
        return misArg(" 2nd ", curBinOp);
    }

    private MissingArgumentException misArg(Object argNum, Object op) {
        return new MissingArgumentException("missing" + argNum + "operand for operation '" + op + "'" + " at position: " + getPos());
    }

    private IncorrectExpressionException lostSym(char sym) {
        if (test('(')) {
            return new IncorrectBracketSequenceException("unexpected opening bracket '" + sym + "' found" + " at position: " + getPos());
        } else if (test(')')) {
            return new IncorrectBracketSequenceException("no paired opening parenthesis found for '" + sym + "'" + " at position: " + getPos());
        }
        return new UnknownSymbolException("unexpected symbol '" + sym + "' found" + " at position: " + getPos());
    }

    private MissingOperatorException misOp() {
        return new MissingOperatorException("missing operator between operands" + " at position: " + getPos());
    }

}
