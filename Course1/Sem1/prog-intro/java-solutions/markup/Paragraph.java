package markup;

import java.util.List;

public class Paragraph implements Element {

    private final List<NotParagraph> muElementList;

    public Paragraph(List<NotParagraph> muElementList) {
        this.muElementList = muElementList;
    }

    @Override
    public void toMarkdown(StringBuilder sb) {
        for (NotParagraph notParagraph : muElementList) {
            notParagraph.toMarkdown(sb);
        }
    }

    @Override
    public void toBBCode(StringBuilder sb) {
        for (NotParagraph notParagraph : muElementList) {
            notParagraph.toBBCode(sb);
        }
    }
}
