package markup;

import java.util.List;

public abstract class MarkupElement extends NotParagraph {

    protected final List<NotParagraph> muElementList;

    public MarkupElement(List<NotParagraph> muElementList) {
        this.muElementList = muElementList;
    }

    @Override
    public void toMarkdown(StringBuilder sb) {
        sb.append(getMDTag());
        for (NotParagraph notParagraph : muElementList) {
            notParagraph.toMarkdown(sb);
        }
        sb.append(getMDTag());
    }

    @Override
    public void toBBCode(StringBuilder sb) {
        sb.append(getBBTagBegin());
        for (NotParagraph notParagraph : muElementList) {
            notParagraph.toBBCode(sb);
        }
        sb.append(getBBTagEnd());
    }

    protected abstract String getMDTag();

    protected abstract String getBBTagBegin();

    protected abstract String getBBTagEnd();
}

// [/i]
