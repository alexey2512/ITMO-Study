package markup;

import java.util.List;

public class Strong extends MarkupElement {

    public Strong(List<NotParagraph> muElementList) {
        super(muElementList);
    }

    @Override
    protected String getMDTag() {
        return "__";
    }

    @Override
    protected String getBBTagBegin() {
        return "[b]";
    }

    @Override
    protected String getBBTagEnd() {
        return "[/b]";
    }

}
