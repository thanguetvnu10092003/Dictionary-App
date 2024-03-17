package base;

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
}
