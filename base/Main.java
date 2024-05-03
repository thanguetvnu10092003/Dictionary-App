package base;

import Screen.DictionaryGUI;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
//        MyJDBC.importDatabase();
//        DictionaryCommandline.dictionaryAdvance();
        DictionaryGUI dictionaryGUI = new DictionaryGUI();
        dictionaryGUI.setVisible(true);
    }
}
