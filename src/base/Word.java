package base;

import java.util.Objects;

public class Word implements Comparable<Word> {
    private String searching;
    private String meaning;
    public String getSearching() {
        return searching;
    }

    public void setSearching(String searching) {
        this.searching = searching;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public Word() {
        searching = "";
        meaning = "";
    }

    public Word(String searching, String meaning) {
        this.searching = searching;
        this.meaning = meaning;
    }

    @Override
    public int compareTo(Word o) {
        return this.searching.compareToIgnoreCase(o.searching);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word)) return false;
        Word word = (Word) o;
        return Objects.equals(getSearching(), word.getSearching());
    }

    @Override
    public int hashCode() {
        return Objects.hash(searching);
    }
}
