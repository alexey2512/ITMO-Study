import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

class SameScanner {

    private final Reader inputStream;
    private int currentIndex, bufferLength, countOfLines = 1;
    private boolean isAvailable = true;
    private boolean hasNextWordIsUsed = false;
    private boolean hasNextWordBool = true;
    private int afterHasNextWord;
    private final StringBuilder hashedNextWord = new StringBuilder();
    private final char[] buffer = new char[1 << 10];
    private final char lineSeparator;

    {
        if (System.lineSeparator().equals("\r")) {
            lineSeparator = '\r';
        } else {
            lineSeparator = '\n';
        }
    }


    public SameScanner(Reader rd) throws IOException {
        inputStream = rd;
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
        if (isAvailable && buffer[currentIndex] == lineSeparator) {
            countOfLines++;
        }
    }

    private boolean isWordSymbol(char sym) {
        return (Character.isLetter(sym) ||
                Character.getType(sym) == Character.DASH_PUNCTUATION ||
                sym == '\'');
    }

    public void setPointerToWord() throws IOException {
        while (isAvailable && !isWordSymbol(buffer[currentIndex])) {
            moveIndex();
        }
    }

    public int getCountOfLines() {
        return countOfLines;
    }

    public String nextWord() throws IOException {
        if (hasNextWordIsUsed) {
            hasNextWordIsUsed = false;
            countOfLines = afterHasNextWord;
            return hashedNextWord.toString();
        } else {
            StringBuilder word = new StringBuilder();
            while (isAvailable && !isWordSymbol(buffer[currentIndex])) {
                moveIndex();
            }
            while (isAvailable && isWordSymbol(buffer[currentIndex])) {
                word.append(buffer[currentIndex]);
                moveIndex();
            }
            while (isAvailable && !isWordSymbol(buffer[currentIndex])) {
                moveIndex();
            }
            return word.toString();
        }
    }

    public boolean hasNextWord() throws IOException {
        if (!hasNextWordIsUsed) {
            int beforeHasNextWord = countOfLines;
            hashedNextWord.setLength(0);
            hashedNextWord.append(nextWord());
            hasNextWordIsUsed = true;
            hasNextWordBool = (!hashedNextWord.isEmpty());
            afterHasNextWord = countOfLines;
            countOfLines = beforeHasNextWord;
        }
        return hasNextWordBool;
    }

    public void close() throws IOException {
        inputStream.close();
    }
}



class Pair {

    private final int first;
    private int second;
    private boolean isNew;

    public Pair(int f, int s) {
        first = f;
        second = s;
        isNew = true;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int a) {
        second = a;
    }

    public boolean isNew() {
        return isNew;
    }

    public void makeOld() {
        isNew = false;
    }

    public String getPair() {
        return first + ":" + second;
    }

}

class PairIntList {

    private int length = 0;
    private final List<Pair> pairs;

    public PairIntList() {
        pairs = new ArrayList<>();
    }

    public void addPair(Pair p) {
        pairs.add(p);
        length++;
    }

    public String joinToString() {
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < length; i++) {
            ans.append(pairs.get(i).getPair()).append(" ");
        }
        if (!ans.isEmpty()) {
            ans.deleteCharAt(ans.length() - 1);
        }
        return ans.toString();
    }

    public Pair get(int i) {
        return pairs.get(i);
    }

    public int getLength() {
        return length;
    }
}



public class WsppSortedPosition {

    public static void main(String[] args) {

        try {

            SameScanner br = new SameScanner(
                    new InputStreamReader(
                            new FileInputStream(
                                    args[0]
                            ), StandardCharsets.UTF_8
                    )
            );

            try {

                int numberOfLine, numberOfWord = 0, iter;
                StringBuilder wrd = new StringBuilder();
                LinkedHashMap<String, PairIntList> dict = new LinkedHashMap<>();

                br.setPointerToWord();

                while (br.hasNextWord()) {

                    numberOfLine = br.getCountOfLines();
                    wrd.append(br.nextWord().toLowerCase());

                    Pair p = new Pair(numberOfLine, numberOfWord);
                    PairIntList tempList = dict.getOrDefault(wrd.toString(), new PairIntList());
                    tempList.addPair(p);
                    dict.put(wrd.toString(), tempList);

                    wrd.setLength(0);
                    numberOfWord++;

                    if (br.getCountOfLines() > numberOfLine || !br.hasNextWord()) {

                        for (Map.Entry<String, PairIntList> entry: dict.entrySet()) {

                            PairIntList val = entry.getValue();
                            iter = val.getLength() - 1;

                            while (iter >= 0 && val.get(iter).isNew()) {

                                Pair temp = val.get(iter);
                                temp.setSecond(numberOfWord - temp.getSecond());
                                temp.makeOld();
                                iter--;

                            }

                            dict.put(entry.getKey(), val);
                        }

                        numberOfWord = 0;

                    }
                }

                List<String> keys = new ArrayList<>(Arrays.asList(dict.keySet().toArray(new String[0])));
                Collections.sort(keys);


                BufferedWriter bw = new BufferedWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(
                                        args[1]
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
                } bw.close();

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
