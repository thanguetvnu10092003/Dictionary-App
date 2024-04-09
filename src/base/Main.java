package base;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        MyJDBC.importDatabase();
        DictionaryCommandline.dictionaryAdvance();
    }
}
