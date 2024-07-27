package expression.parser;

public class BaseParser {

    private CharSource source;
    private char ch = 0xffff;
    private char preCh = 0xffff;
    public static final char END = '\0';

    protected BaseParser(CharSource source) {
        this.source = source;
        take();
    }

    protected boolean test(char expected) {
        return ch == expected;
    }

    protected char take() {
        preCh = ch;
        ch = source.hasNext() ? source.next() : END;
        return preCh;
    }

    protected boolean take(char expected) {
        if (test(expected)) {
            take();
            return true;
        }
        return false;
    }

    protected IllegalArgumentException error(String message) {
        return source.error(message);
    }

    protected void expect(char expected) {
        if (!take(expected)) {
            throw error("Expected '" + expected + "', found '" + ch + "'");
        }
    }

    protected void expect(String string) {
        for (char c: string.toCharArray()) {
            expect(c);
        }
    }

    protected boolean eof() {
        return test(END);
    }

    protected boolean between(char l, char r) {
        return l <= ch && ch <= r;
    }

    protected void setSource(CharSource source) {
        this.source = source;
        take();
    }

    protected boolean skipWhitespaces() {
        boolean flag = false;
        while (Character.isWhitespace(ch)) {
            take();
            flag = true;
        }
        return flag;
    }

    protected char getCurrentChar() {
        return ch;
    }

    protected char getPreviousChar() {
        return preCh;
    }

    protected int getPos() {
        return source.getPos();
    }

}
