package md2html;

import java.util.HashMap;
import java.util.Map;

public class Parser4M2H {

    private static final Map<String, String> markupTags = Map.of(
            "*", "em",
            "_", "em",
            "**", "strong",
            "__", "strong",
            "--", "s",
            "`", "code",
            "++", "u",
            "~", "mark",
            "''", "q",
            "%", "var"
    );

    private static final Map<Character, String> specialSymbols = Map.of(
            '<', "&lt;",
            '>', "&gt;",
            '&', "&amp;"
    );


    public static boolean isShielded(char c) {
        return (c == '_' || c == '*');
    }

    public static boolean isPartOfTag(char c) {
        return (c == '*' || c == '_' || c == '-' ||
                c == '`' || c == '+' || c == '~' ||
                c == '\'' || c == '%');
    }

    public static boolean isSpecialSymbol(char c) {
        return (c == '<' || c == '>' || c == '&');
    }


    public static String parseToHtml(String paragraph) {

        int frontIndex = 0;
        int order = 0;
        char frontSymbol, preFrontSymbol, prePreFrontSymbol;

        Map<String, Boolean> map = new HashMap<>();
        for (String key: markupTags.keySet()) {
            map.put(key, false);
        }

        StringBuilder outPar = new StringBuilder();

        // writing begin of paragraph
        while (paragraph.charAt(frontIndex) == '#') {
            frontIndex++;
        }
        if (frontIndex > 0 && Character.isWhitespace(paragraph.charAt(frontIndex))) {
            outPar.append("<h").append(frontIndex).append(">");
            order = frontIndex;
            frontIndex++;
        } else {
            outPar.append("<p>");
            outPar.append(paragraph, 0,
                    isPartOfTag(paragraph.charAt(frontIndex)) ? frontIndex : ++frontIndex);
        }
        frontIndex++;


        while (frontIndex < paragraph.length()) {

            String temp = paragraph.substring(frontIndex - 1, frontIndex + 1);
            frontSymbol = paragraph.charAt(frontIndex);
            preFrontSymbol = paragraph.charAt(frontIndex - 1);
            prePreFrontSymbol = frontIndex > 1 ? paragraph.charAt(frontIndex - 2) : ' ';

            // writing line separator
            if (preFrontSymbol == '\n') {
                outPar.append(System.lineSeparator());
                frontIndex++;
                continue;
            }

            // writing shielded symbols
            if (preFrontSymbol == '\\') {
                outPar.append(frontSymbol);
                frontIndex += 2;
                continue;
            }

            // writing single * and _
            if (isShielded(preFrontSymbol) &&
                    Character.isWhitespace(frontSymbol) &&
                    Character.isWhitespace(prePreFrontSymbol)) {
                outPar.append(temp);
                frontIndex += 2;
                continue;
            }

            // writing special symbols
            if (isSpecialSymbol(preFrontSymbol)) {
                outPar.append(specialSymbols.get(preFrontSymbol));
                frontIndex++;
                continue;
            }

            // writing markup tags with length 2
            String htmlTag2 = markupTags.getOrDefault(temp, "");
            if (!htmlTag2.isEmpty()) {
                outPar.append("<").append(map.get(temp) ? "/" : "").append(htmlTag2).append(">");
                map.put(temp, !map.get(temp));
                frontIndex += 2;
                continue;
            }

            // writing markup tags with length 1
            String htmlTag1 = markupTags.getOrDefault(Character.toString(preFrontSymbol), "");
            if (!htmlTag1.isEmpty()) {
                String pFS = Character.toString(preFrontSymbol);
                outPar.append("<").append(map.get(pFS) ? "/" : "").append(htmlTag1).append(">");
                map.put(pFS, !map.get(pFS));
                frontIndex++;
                continue;
            }

            // writing all other
            outPar.append(preFrontSymbol);
            frontIndex++;

        }

        // writing end of paragraph
        if (order == 0) {
            outPar.append("</p>");
        } else {
            outPar.append("</h").append(order).append(">");
        }

        return outPar.toString();
    }
}