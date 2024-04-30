package Screen;

import base.*;
import constants.CommonConstants;git add src/Screen/testGUI.java

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class testGUI extends JFrame {
    private JTextField searchBar;  // Search bar
    private JPanel buttonPanel;    // Button panel for search and other buttons
    private JPanel wordListPanel;  // Panel to hold the word buttons

    public testGUI() {
        super("Dictionary VI EN");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        addGuiComponents();
        setVisible(true);
    }

    private void addGuiComponents() {
        setupSearchBar();

        // Panel that will hold the word buttons with a vertical scrollbar
        wordListPanel = new JPanel();
        wordListPanel.setLayout(new BoxLayout(wordListPanel, BoxLayout.Y_AXIS));
        wordListPanel.setBackground(new Color(0x4B5081));

        JScrollPane scrollPane = new JScrollPane(wordListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(300, 750)); // Adjust width as needed

        // Remaining GUI components setup
        setupRemainingComponents();

        getContentPane().add(scrollPane, BorderLayout.WEST); // Adding the scroll pane on the left side
        getContentPane().setBackground(new Color(0x4B5081));
    }

    private void setupSearchBar() {
        // Search bar setup
        searchBar = new JTextField(20);  // Initially set the columns of the text field
        searchBar.setFont(new Font("Arial", Font.PLAIN, 18));
        searchBar.setPreferredSize(new Dimension(50, 10));  // Set the preferred size

        buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0x4B5081));
        addIconsToButtonPanel();

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchBar, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.EAST);
        this.add(searchPanel, BorderLayout.NORTH);
    }

    private void addIconsToButtonPanel() {
        // Add buttons with icons
        addIconButton("search.png", "Search");
        addIconButton("edit.png", "Edit");
        addIconButton("favourite.png", "Favorite");
        addIconButton("history.png", "History");
        addIconButton("remove.png", "Remove");
        addIconButton("reset.png", "Reset");
        addIconButton("save.png", "Save");
        addIconButton("setting.png", "Settings");
    }

    private void setupRemainingComponents() {
        // Import from text file
        DictionaryManagement.setInputPath("src/base/test.txt");
        DictionaryManagement.insertFromFile();

        ArrayList<Word> words = DictionaryManagement.oldWord;

        int i = 0;
        for (Word word : words) {
            displayComponent(word, i);
            i++;
        }
    }

    private void displayComponent(Word word, int x) {
        JButton ENMeaning = new JButton(word.getSearching());
        ENMeaning.setFont(new Font("Arial", Font.BOLD, 24));
        ENMeaning.setForeground(Color.DARK_GRAY);
        ENMeaning.setAlignmentX(Component.LEFT_ALIGNMENT);
        ENMeaning.addActionListener(e -> showTranslation(word));

        wordListPanel.add(ENMeaning);
    }

    private void showTranslation(Word word) {
        // This method should handle the display of translation and pronunciation without disrupting existing GUI elements
    }

    private void addIconButton(String iconName, String tooltip) {
        String imagePath = "src/resource/media/resource/" + iconName;
        ImageIcon icon = new ImageIcon(imagePath);
        JButton button = new JButton(icon);
        button.setToolTipText(tooltip);
        button.addActionListener(e -> {
            // Define actions for buttons here, possibly calling other methods
        });
        buttonPanel.add(button);
    }

    public static void main(String[] args) {
        new DictionaryGUI();
    }
}
