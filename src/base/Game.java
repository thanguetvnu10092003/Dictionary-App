package base;

import Screen.TitleScreenGui;

import javax.swing.*;

public class Game {
    public static void play() {
        // ensures
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // create and display the title screen gui window
                new TitleScreenGui().setVisible(true);
            }
        });
    }
}
