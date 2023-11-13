import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

class TrueSameScanner {

    private final Reader inputStream;
    private final StringBuilder hashedNext = new StringBuilder();
    private final char[] buffer = new char[1 << 10];
    private int currentIndex, bufferLength;
    private boolean isAvailable = true;
    private boolean hasNextIsUsed = false;
    private boolean hasNextBool = true;

    public TrueSameScanner(File in, String encoding) throws IOException {
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
                c == '\n' ||
                Character.getType(c) == Character.LINE_SEPARATOR);
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

    public void close() throws IOException {
        inputStream.close();
    }

}


class IntList {

    private int length = 0;
    private int lastIndex = 0;
    private int[] arr;

    public IntList() {
        arr = new int[1];
        length = 1;
        lastIndex = -1;
    }

    private void arrayEnlarge() {
        length = length * 2 + 1;
        int[] temp = new int[length];
        System.arraycopy(arr, 0, temp, 0, lastIndex);
        arr = temp;
    }

    public void append(int val) {
        lastIndex++;
        if (lastIndex == length) {
            arrayEnlarge();
        }
        arr[lastIndex] = val;
    }

    public String joinToString() {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < lastIndex + 1; i++) {
            line.append(arr[i]).append(" ");
        }
        if (!line.isEmpty()) {
            line.deleteCharAt(line.length() - 1);
        }
        return line.toString();
    }

    public int getLength() {
        return lastIndex + 1;
    }
}


public class Wspp {

    public static boolean isWordSymbol(char sym) {
        return (Character.isLetter(sym) ||
                Character.getType(sym) == Character.DASH_PUNCTUATION ||
                sym == '\'');
    }

    public static void main(String[] args) {

        try {

            TrueSameScanner br = new TrueSameScanner(new File(args[0]), "UTF-8");

            try {

                StringBuilder sb = new StringBuilder();
                while (br.hasNext()) {
                    sb.append(br.next()).append(" ");
                }

                String stream = sb + " ";
                LinkedHashMap<String, IntList> dict = new LinkedHashMap<>();
                boolean flag = false;
                int begin = 0, count = 1;

                for (int i = 0; i < stream.length(); i++) {
                    if (!flag && isWordSymbol(stream.charAt(i))) {
                        begin = i;
                        flag = true;
                    }
                    if (flag && !isWordSymbol(stream.charAt(i))) {
                        String word = stream.substring(begin, i).toLowerCase();
                        IntList t = dict.getOrDefault(word, new IntList());
                        t.append(count);
                        dict.put(word, t);
                        flag = false;
                        count++;
                    }
                }

                String[] keys = dict.keySet().toArray(new String[0]);

                BufferedWriter bw = new BufferedWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(new File(args[1])
                                ), StandardCharsets.UTF_8
                        )
                );
                try {

                    for (String key : keys) {
                        if (!key.isEmpty()) {
                            bw.write(key + " " + dict.get(key).getLength() + " " + dict.get(key).joinToString() + "\n");
                        }
                    }

                } catch (IOException e) {
                    System.err.println("Input-output exception: " + e.getMessage());
                }
                bw.close();

            } catch (IOException e) {
                System.err.println("Input-output exception: " + e.getMessage());
            } finally {
                br.close();
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.err.println("Unsupported encoding: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Input-output exception: " + e.getMessage());
        }
    }
}