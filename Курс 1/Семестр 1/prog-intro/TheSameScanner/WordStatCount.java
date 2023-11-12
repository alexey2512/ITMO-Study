import java.io.*;
import java.util.*;

public class WordStatCount {

    public static  boolean isWordSymbol(char sym) {
        return (Character.isLetter(sym) ||
                Character.getType(sym) == Character.DASH_PUNCTUATION ||
                sym == '\'');
    }

    public static void wordConsidering(LinkedHashMap<String, Integer> di, String st) {
        di.put(st, di.getOrDefault(st, 0) + 1);
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
                BufferedWriter bw = new BufferedWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(
                                        new File(args[1])
                                ), "UTF-8"
                        )
                );

                try {

                    //Sorting
                    Set<String> ks = dict.keySet();
                    String[] keys = ks.toArray(new String[0]);
                    String temp;
                    for (int i = 0; i < keys.length; i++) {
                        for (int j = 0; j < keys.length - 1 - i; j++) {
                            if (dict.get(keys[j]) > dict.get(keys[j + 1])) {
                                temp = keys[j];
                                keys[j] = keys[j + 1];
                                keys[j + 1] = temp;
                            }
                        }
                    }

                    //Writing in file
                    for (String w: keys) {
                        bw.write(w + " " + dict.get(w) + "\n");
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
