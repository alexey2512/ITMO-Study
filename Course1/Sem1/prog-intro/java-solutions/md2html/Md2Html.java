package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Md2Html {

    public static void main(String[] args) {

        try {

            try (BufferedReader sc = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(
                                    new File(args[0])
                            ), StandardCharsets.UTF_8
                    )
            )) {

                try (BufferedWriter bw = new BufferedWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(
                                        new File(args[1])
                                ), StandardCharsets.UTF_8
                        )
                )) {

                    StringBuilder line = new StringBuilder();
                    StringBuilder paragraph = new StringBuilder();
                    boolean paragraphIsOpened = false;

                    while (sc.ready()) {
                        line.append(sc.readLine());
                        //
                        if (paragraphIsOpened && line.isEmpty()) {
                            paragraphIsOpened = false;
                            bw.write(Parser4M2H.parseToHtml(paragraph.toString()) + System.lineSeparator());
                            paragraph.setLength(0);
                        }
                        if (!line.isEmpty()) {
                            paragraphIsOpened = true;
                            paragraph.append(line).append("\n");
                        }
                        //
                        line.setLength(0);
                    }

                    if (paragraphIsOpened) {
                        bw.write(Parser4M2H.parseToHtml(paragraph.toString()));
                    }

                } catch (IOException e) {
                    System.err.println("Input-output exception: " + e.getMessage());
                }

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
