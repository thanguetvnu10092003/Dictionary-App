package Screen;

import base.DictionaryManagement;
import base.Word;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DictionaryGUI extends JFrame {
    private JTextField searchBar;  // search bar
    private JPanel buttonPanel;    // button

    public DictionaryGUI() {
        super("Dictionary VI EN");
        setSize(1200, 800);
        addGuiComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close
        setVisible(true);
    }

    private void addGuiComponents() {
        DictionaryManagement.setInputPath("C:\\Users\\Admin\\Desktop\\Dict\\src\\base\\test.txt");
        DictionaryManagement.insertFromFile();
        ArrayList<Word> words = DictionaryManagement.oldWord;

        // Menu
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0x859BF5));
        headerPanel.setPreferredSize(new Dimension(1200, 50));
        JLabel headerLabel = new JLabel("Menu");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(headerLabel);
        this.add(headerPanel, BorderLayout.NORTH);

        // Searchbar
        setupSearchBar();

        // In word
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(0x4B5081));
        for (Word word : words) {
            JLabel titleLabel = new JLabel(word.getSearching() + " " + word.getMeaning());
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
//            titleLabel.setForeground(Color.WHITE);
            panel.add(titleLabel);
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(scrollPane, BorderLayout.CENTER);

        getContentPane().setBackground(new Color(0x4B5081));
    }

    private void setupSearchBar() {
        // Search bar
        searchBar = new JTextField(20);  // Initially set the columns of the text field
        searchBar.setFont(new Font("Arial", Font.PLAIN, 18));
        searchBar.setPreferredSize(new Dimension(100, 10));  // Set the preferred size

        // Button panel
        buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0x4B5081));

        // Adding buttons with icons
        addIconButton("edit.png", "Edit");
        addIconButton("favourite.png", "Favorite");
        addIconButton("history.png", "History");
        addIconButton("remove.png", "Remove");
        addIconButton("reset.png", "Reset");
        addIconButton("save.png", "Save");
        addIconButton("search.png", "Search");
        addIconButton("setting.png", "Settings");
        addIconButton("speak.png", "Speak");

        // Adding components to the top
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchBar, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.EAST);
        this.add(searchPanel, BorderLayout.NORTH);
    }

    private void addIconButton(String iconName, String tooltip) {
        String imagePath = "C:\\Users\\Admin\\Desktop\\Dict\\resource\\media\\hover\\" + iconName;
        ImageIcon icon = new ImageIcon(imagePath);
        JButton button = new JButton(icon);
        button.setToolTipText(tooltip);
        buttonPanel.add(button);
    }
}
