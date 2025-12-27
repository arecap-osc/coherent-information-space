package org.hkrdi.eden.ggm.vaadin.console.service.semanticmap;

public class LinkBean {

    private WordBean fromWord;

    private WordBean toWord;

    public LinkBean() {
    }

    public LinkBean(WordBean fromWord, WordBean toWord) {
        this.fromWord = fromWord;
        this.toWord = toWord;
    }

    public WordBean getFromWord() {
        return fromWord;
    }

    public void setFromWord(WordBean fromWord) {
        this.fromWord = fromWord;
    }

    public WordBean getToWord() {
        return toWord;
    }

    public void setToWord(WordBean toWord) {
        this.toWord = toWord;
    }

    public LinkBean transpose() {
        return new LinkBean(toWord, fromWord);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        LinkBean linkBean = (LinkBean) o;

        if (fromWord != null ? !fromWord.equals(linkBean.fromWord) : linkBean.fromWord != null)
            return false;
        return toWord != null ? toWord.equals(linkBean.toWord) : linkBean.toWord == null;
    }

    @Override
    public int hashCode() {
        int result = fromWord != null ? fromWord.hashCode() : 0;
        result = 31 * result + (toWord != null ? toWord.hashCode() : 0);
        return result;
    }
}
