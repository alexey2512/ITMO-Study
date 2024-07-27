import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WordStatInput {
    
    public static  boolean isWordSymbol(char sym) {
        return (Character.isLetter(sym) ||
                Character.getType(sym) == Character.DASH_PUNCTUATION ||
                sym == '\'');
    }

    public static void wordConsidering(LinkedHashMap<String, Integer> D, String S) {
        if (D.containsKey(S)) {
            int val = D.get(S);
            D.put(S, val + 1);
        } else {
            D.put(S, 1);
        }
    }

    public static void main(String[] args) {

        try {

            //BufferedReader initialization
            TheSameScanner br = new TheSameScanner(new File(args[0]), "UTF-8");

            try {

                //Reading from file
                StringBuilder sb = new StringBuilder();
                while (br.hasNext()) {
                    sb.append(br.next());
                    sb.append(" ");
                }

                //Text process
                String line = sb.toString().toLowerCase();
                LinkedHashMap<String, Integer> dict = new LinkedHashMap<>();
                int begin = 0;
                boolean flag = false;
                for (int i = 0; i < line.length(); i++) {
                    if (isWordSymbol(line.charAt(i)) && !flag) {
                        begin = i;
                        flag = true;
                    }
                    if (!isWordSymbol(line.charAt(i)) && flag) {
                        wordConsidering(dict, line.substring(begin, i));
                        flag = false;
                    }
                }

                //BufferedWriter initialization

                try (BufferedWriter bw = new BufferedWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(
                                        new File(args[1])
                                ), StandardCharsets.UTF_8
                        )
                )) {

                    //Writing in file
                    for (Map.Entry<String, Integer> entry : dict.entrySet()) {
                        if (!entry.getKey().isEmpty()) {
                            bw.write(entry.getKey() + " " + entry.getValue() + "\n");
                        }
                    }

                    //BufferedWriter closing
                }

                //BufferedReader closing
            } catch (UnsupportedEncodingException e) {
                System.err.println("Unsupported encoding: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Input-output exception: " + e.getMessage());
            } finally {
                br.close();
            }

            //Exception catching
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.err.println("Unsupported encoding: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Input-output exception: " + e.getMessage());
        }

    }
}
