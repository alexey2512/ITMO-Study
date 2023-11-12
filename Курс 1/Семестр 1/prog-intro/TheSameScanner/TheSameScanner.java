import java.io.*;

public class TheSameScanner {

    private final Reader inputStream;
    private int currentIndex, bufferLength;
    private boolean isAvailable = true, lineExists;
    private boolean hasNextIsUsed = false, hasNextLineIsUsed = false;
    private boolean hasNextBool = true, hasNextLineBool = true;
    private final StringBuilder hashedNext = new StringBuilder();
    private final StringBuilder hashedNextLine = new StringBuilder();
    private final char[] buffer = new char[1 << 16];
    private final char[] alpha = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};


    public TheSameScanner(InputStream in, String encoding) throws IOException {
        inputStream = new InputStreamReader(in, encoding);
        readNextBuffer();
    }

    public TheSameScanner(String in) throws IOException {
        inputStream = new StringReader(in);
        readNextBuffer();
    }

    public TheSameScanner(File in, String encoding) throws IOException {
        inputStream = new InputStreamReader(new FileInputStream(in), encoding);
        readNextBuffer();
    }

    private void readNextBuffer() throws IOException {
        bufferLength = inputStream.read(buffer);
        if (bufferLength < 0) {
            isAvailable = false;
        }
        currentIndex = 0;
    }

    private void moveIndex() throws IOException {
        currentIndex++;
        if (currentIndex == bufferLength) {
            readNextBuffer();
        }
    }

    private boolean isSpace(char c) {
        return (Character.isWhitespace(c) ||
                Character.isSpaceChar(c) ||
                c == '\n');
    }

    public String nextLine() throws IOException {
        if (hasNextLineIsUsed) {
            hasNextLineIsUsed = false;
            return hashedNextLine.toString();
        } else {
            StringBuilder line = new StringBuilder();
            while (isAvailable && buffer[currentIndex] != '\n') {
                line.append(buffer[currentIndex]);
                moveIndex();
            }
            hasNextLineBool = (isAvailable || !line.isEmpty());
            if (isAvailable) {
                moveIndex();
            }
            return line.toString();
        }
    }

    public boolean hasNextLine() throws IOException {
        if (!hasNextLineIsUsed) {
            hashedNextLine.setLength(0);
            hashedNextLine.append(nextLine());
            hasNextLineIsUsed = true;
        }
        return hasNextLineBool;
    }

    public String next() throws IOException {
        if (hasNextIsUsed) {
            hasNextIsUsed = false;
            return hashedNext.toString();
        } else {
            StringBuilder word = new StringBuilder();
            while (isAvailable && isSpace(buffer[currentIndex])) {
                moveIndex();
            }
            while (isAvailable && !isSpace(buffer[currentIndex])) {
                word.append(buffer[currentIndex]);
                moveIndex();
            }
            while (isAvailable && isSpace(buffer[currentIndex])) {
                moveIndex();
            }
            return word.toString();
        }
    }

    public boolean hasNext() throws IOException {
        if (!hasNextIsUsed) {
            hashedNext.setLength(0);
            hashedNext.append(next());
            hasNextIsUsed = true;
            hasNextBool = (!hashedNext.isEmpty());
        }
        return hasNextBool;
    }

    public int nextInt() throws IOException, NumberFormatException {
        return Integer.parseInt(next());
    }

    public boolean hasNextInt() throws IOException {
        return hasNext();
    }

    private int getIndex(char ch) throws NumberFormatException {
        for (int i = 0; i < 10; i++) {
            if (alpha[i] == ch) {
                return i;
            }
        }
        throw new NumberFormatException();
    }

    public int nextAbc() throws IOException, NumberFormatException {
        String str = next();
        StringBuilder num = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '-') {
                num.append('-');
            } else {
                num.append(getIndex(str.charAt(i)));
            }
        }
        return Integer.parseInt(num.toString());
    }

    public void close() throws IOException {
        inputStream.close();
    }
}