package expression.exceptions;

import java.util.*;
import expression.*;
import expression.parser.BaseParser;
import expression.parser.StringSource;

public class ExpressionParser extends BaseParser implements TripleParser, ListParser {

    private char curBinOp, curUnOp;
    private final static Map<Character, String> unaryOperations = Map.of(
            '-', "-",
            '~', "~",
            'l', "log2",
            'p', "pow2"
    );
    private static final Map<Character, Character> openToCloseParens = Map.of(
            '(', ')',
            '[', ']',
            '{', '}'
    );
    private static final Map<Character, Character> closeToOpenParens = Map.of(
            ')', '(',
            ']', '[',
            '}', '{'
    );

    public ExpressionParser() {
        super(new StringSource(""));
    }

    @Override
    public CommonExpression parse(String expression) throws Exception {
        return parse(expression, List.of("x", "y", "z"));
    }

    @Override
    public CommonExpression parse(String expression, List<String> variables) throws Exception {
        setSource(new StringSource(expression));
        VariablesController.setList(variables);
        return parse();
    }

    private CommonExpression parse() {
        CommonExpression answer = parseBitOr();
        skipWhitespaces();
        if (isClosingParen()) {
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

    private CommonExpression parseBitOr() {
        curBinOp = END;
        curUnOp = END;
        CommonExpression temp = parseBitXor();
        while (true) {
            if (take('|')) {
                curBinOp = '|';
                temp = new BitOr(temp, parseBitXor());
                curBinOp = END;
            } else {
                return temp;
            }
        }
    }

    private CommonExpression parseBitXor() {
        CommonExpression temp = parseBitAnd();
        while (true) {
            if (take('^')) {
                curBinOp = '^';
                temp = new BitXor(temp, parseBitAnd());
                curBinOp = END;
            } else {
                return temp;
            }
        }
    }

    private CommonExpression parseBitAnd() {
        CommonExpression temp = parseAddSubtract();
        while (true) {
            if (take('&')) {
                curBinOp = '&';
                temp = new BitAnd(temp, parseAddSubtract());
                curBinOp = END;
            } else {
                return temp;
            }
        }
    }

    private CommonExpression parseAddSubtract() {
        CommonExpression temp = parseMultiplyDivide();
        while (true) {
            if (take('+')) {
                curBinOp = '+';
                temp = new CheckedAdd(temp, parseMultiplyDivide());
                curBinOp = END;
            } else if (take('-')) {
                curBinOp = '-';
                temp = new CheckedSubtract(temp, parseMultiplyDivide());
                curBinOp = END;
            } else {
                return temp;
            }
        }
    }

    private CommonExpression parseMultiplyDivide() {
        skipWhitespaces();
        CommonExpression temp = parseFirstPriority();
        skipWhitespaces();
        while (true) {
            if (take('*')) {
                curBinOp = '*';
                skipWhitespaces();
                temp = new CheckedMultiply(temp, parseFirstPriority());
                skipWhitespaces();
                curBinOp = END;
            } else if (take('/')) {
                curBinOp = '/';
                skipWhitespaces();
                temp = new CheckedDivide(temp, parseFirstPriority());
                skipWhitespaces();
                curBinOp = END;
            } else {
                return temp;
            }
        }
    }

    private CommonExpression parseFirstPriority() {
        CommonExpression temp;
        if (take('-')) {
            curUnOp = '-';
            if (isOpeningParen()) {
                char paren = take();
                temp = new CheckedNegate(parseBitOr());
                if (!take(openToCloseParens.get(paren))) {
                    throw closPar(paren);
                }
            } else if (skipWhitespaces()) {
                temp = new CheckedNegate(parseFirstPriority());
            } else {
                temp = parseVarConst("-");
                temp = (temp instanceof Const) ? temp : new CheckedNegate(temp);
            }
            curUnOp = END;
            return temp;
        } else if (isOpeningParen()) {
            char paren = take();
            temp = parseBitOr();
            if (!take(openToCloseParens.get(paren))) {
                throw closPar(paren);
            }
            return temp;
        } else if (take('~')) {
            temp = parseUnary('~', "", false);
            return new Not(temp);
        } else if (take('l')) {
            temp = parseUnary('l', "og2", true);
            return new CheckedLog2(temp);
        } else if (take('p')) {
            temp = parseUnary('p', "ow2", true);
            return new CheckedPow2(temp);
        } else {
            temp = parseVarConst("");
            return temp;
        }
    }

    private CommonExpression parseUnary(char taken, String expected, boolean spaceAfter) {
        curUnOp = taken;
        try {
            expect(expected);
        } catch (Exception e) {
            throw lostSym(taken);
        }
        if (!skipWhitespaces() && spaceAfter && !isOpeningParen() && !eof()) {
            throw lostSym(take());
        }
        CommonExpression temp = parseFirstPriority();
        skipWhitespaces();
        curUnOp = END;
        return temp;
    }

    private CommonExpression parseVarConst(String prefix) {
        StringBuilder sb = new StringBuilder();
        if (between('0', '9')) {
            sb.append(prefix);
            while (between('0', '9')) {
                sb.append(take());
            }
            try {
                int num = Integer.parseInt(sb.toString());
                return new Const(num);
            } catch (NumberFormatException e) {
                throw new OverflowException("overflow in constant " + sb);
            }
        } else if (VariablesController.isBeginOfVariable(getCurrentChar())) {
            while (!isPrimitiveOperation() && !eof() && !Character.isWhitespace(getCurrentChar())
            && !isOpeningParen() && !isClosingParen()) {
                sb.append(take());
            }
            for (int i = 0; i < VariablesController.variables.size(); i++) {
                if (VariablesController.variables.get(i).contentEquals(sb)) {
                    return new Variable(i);
                }
            }
            throw lostSym(getPreviousChar());
        } else if (test('-')) {
            return parseFirstPriority();
        } else if (isClosingParen()) {
            char paren = take();
            if (curBinOp == END) {
                throw new IncorrectBracketSequenceException("empty brackets " + closeToOpenParens.get(paren) + paren + " at position: " + getPos());
            }
            throw misArg(" 2nd ", curBinOp);
        }  else if (isPrimitiveOperation() || eof()) {
            if (curUnOp != END) {
                throw misArg(" ", unaryOperations.get(curUnOp));
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



    private boolean isOpeningParen() {
        return test('(') || test('[') || test('{');
    }

    private boolean isClosingParen() {
        return test(')') || test(']') || test('}');
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



    private RuntimeException closPar(char paren) {
        if (eof()) {
            throw new IncorrectBracketSequenceException("no paired closing parenthesis found for '" + paren + "'" + " at position: " + getPos());
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

    private RuntimeException lostSym(char sym) {
        if (isOpeningParen()) {
            return new IncorrectBracketSequenceException("unexpected opening bracket '" + sym + "' found" + " at position: " + getPos());
        } else if (isClosingParen()) {
            return new IncorrectBracketSequenceException("no paired opening parenthesis found for '" + sym + "'" + " at position: " + getPos());
        }
        return new UnknownSymbolException("unexpected symbol '" + sym + "' found" + " at position: " + getPos());
    }

    private MissingOperatorException misOp() {
        return new MissingOperatorException("missing operator between operands" + " at position: " + getPos());
    }



    public static class VariablesController {

        private static List<String> variables = List.of("x", "y", "z");
        private static List<Character> firsts = List.of('x', 'y', 'z');

        private static void setList(List<String> variables) {
            VariablesController.variables = variables;
            firsts = new ArrayList<>();
            for (String variable: variables) {
                firsts.add(variable.charAt(0));
            }
        }

        public static String get(int index) {
            return variables.get(index);
        }

        public static int indexOf(String variable) {
            return variables.indexOf(variable);
        }

        public static int size() {
            return variables.size();
        }

        public static boolean isBeginOfVariable(Character ch) {
            return firsts.contains(ch);
        }
    }

}
