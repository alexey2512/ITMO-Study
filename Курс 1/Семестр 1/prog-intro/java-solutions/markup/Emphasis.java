package markup;

import java.util.List;

public class Emphasis extends MarkupElement {

    public Emphasis(List<NotParagraph> muElementList) {
        super(muElementList);
    }

    @Override
    protected String getMDTag() {
        return "*";
    }

    @Override
    protected String getBBTagBegin() {
        return "[i]";
    }

    @Override
    protected String getBBTagEnd() {
        return "[/i]";
    }
}
