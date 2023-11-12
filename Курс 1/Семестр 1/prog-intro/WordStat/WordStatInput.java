import java.io.*;
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
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(
                                    new File(args[0])
                            ), "UTF-8"
                    )
            );

            try {

                //Reading from file
                StringBuilder sb = new StringBuilder();
                char[] buffer = new char[1 << 16];
                int read;
                while ((read = br.read(buffer)) >= 0) {
                    sb.append(new String(buffer, 0, read));
                }

                //Text process
                String line = sb.toString().toLowerCase() + " ";
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
                BufferedWriter bw = new BufferedWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(
                                        new File(args[1])
                                ), "UTF-8"
                        )
                );

                try {

                    //Writing in file
                    for (Map.Entry<String, Integer> entry: dict.entrySet()) {
                        if (!entry.getKey().equals("")) {
                            bw.write(entry.getKey() + " " + entry.getValue() + "\n");
                        }
                    }

                //BufferedWriter closing
                } finally {
                    bw.close();
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
