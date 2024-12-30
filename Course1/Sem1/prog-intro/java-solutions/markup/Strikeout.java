package markup;

import java.util.List;

public class Strikeout extends MarkupElement {

    public Strikeout(List<NotParagraph> muElementList) {
        super(muElementList);
    }

    @Override
    protected String getMDTag() {
        return "~";
    }

    @Override
    protected String getBBTagBegin() {
        return "[s]";
    }

    @Override
    protected String getBBTagEnd() {
        return "[/s]";
    }

}
