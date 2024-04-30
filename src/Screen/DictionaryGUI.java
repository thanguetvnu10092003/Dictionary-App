package Screen;

import base.DictionaryManagement;
import base.Word;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
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

    public void displayComponent(Word word,JPanel panel,int x){
        panel.setLayout(null);

        JLabel ENMeaning = new JLabel(word.getSearching());
        ENMeaning.setFont(new Font("Arial", Font.BOLD, 24));
        ENMeaning.setBounds(10, 30*x, 200, 30);
        ENMeaning.setForeground(Color.WHITE);
        panel.add(ENMeaning);
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
        int i = 0;
        for (Word word : words) {
            displayComponent(word, panel,i);
            i++;
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
        searchBar.setPreferredSize(new Dimension(50, 10));  // Set the preferred size

        // Button panel
        buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0x4B5081));

        // Adding buttons with icons
        addIconButton("search.png", "Search");
        addIconButton("edit.png", "Edit");
        addIconButton("favourite.png", "Favorite");
        addIconButton("history.png", "History");
        addIconButton("remove.png", "Remove");
        addIconButton("reset.png", "Reset");
        addIconButton("save.png", "Save");
        addIconButton("setting.png", "Settings");

        // Adding components to the top
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchBar, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.EAST);
        this.add(searchPanel, BorderLayout.NORTH);
    }

    private void addIconButton(String iconName, String tooltip) {
        String imagePath = "C:\\Users\\Admin\\Desktop\\Project\\src\\resource\\media\\resource\\" + iconName;
        ImageIcon icon = new ImageIcon(imagePath);
        JButton button = new JButton(icon);
        button.setToolTipText(tooltip);
        button.addActionListener(e -> {
            if (tooltip.equals("Edit")) {
                openEditDialog();
            } else if (tooltip.equals("Settings")) {
                openSettingsDialog();
            }
        });
        buttonPanel.add(button);
    }



    private void openSettingsDialog() {
        JDialog settingsDialog = new JDialog(this, "Settings", true);
        settingsDialog.setSize(300, 200);
        settingsDialog.setLayout(new GridLayout(2, 5)); // Grid layout with 2 rows and 5 columns

        ActionListener buttonListener = e -> {
            JButton clickedButton = (JButton) e.getSource();
            System.out.println("Button clicked: " + clickedButton.getText());
        };

        for (int i = 0; i < 10; i++) {
            JButton button = new JButton(Integer.toString(i));
            button.addActionListener(buttonListener);
            settingsDialog.add(button);
        }

        settingsDialog.setLocationRelativeTo(this); // Center the dialog relative to the main frame
        settingsDialog.setVisible(true);
    }

    private void openEditDialog() {
        JDialog editDialog = new JDialog(this, "Add New Word", true);
        editDialog.setLayout(new GridLayout(3, 2));  // Grid layout for labels and text fields
        editDialog.setSize(400, 200);

        // Labels and text fields for input
        JLabel englishLabel = new JLabel("English Word:");
        JTextField englishTextField = new JTextField();
        JLabel vietnameseLabel = new JLabel("Vietnamese Meaning:");
        JTextField vietnameseTextField = new JTextField();

        // Buttons for submitting or cancelling
        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");

        // Adding components to the dialog
        editDialog.add(englishLabel);
        editDialog.add(englishTextField);
        editDialog.add(vietnameseLabel);
        editDialog.add(vietnameseTextField);
        editDialog.add(addButton);
        editDialog.add(cancelButton);

        // Action listener for the Add button
        addButton.addActionListener(e -> {
            String englishWord = englishTextField.getText();
            String vietnameseMeaning = vietnameseTextField.getText();
            if (!englishWord.isEmpty() && !vietnameseMeaning.isEmpty()) {
                // Set the input path before adding the word
                DictionaryManagement.setInputPath("C:\\Users\\Admin\\Desktop\\Project\\src\\base\\test.txt");
                DictionaryManagement.addWord(englishWord, vietnameseMeaning);
                // Assuming a method to save directly to the file
                DictionaryManagement.exportToFile("C:\\Users\\Admin\\Desktop\\Project\\src\\base\\test.txt");
                JOptionPane.showMessageDialog(editDialog, "Word added successfully!");
            } else {
                JOptionPane.showMessageDialog(editDialog, "Please fill both fields!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action listener for the Cancel button
        cancelButton.addActionListener(e -> editDialog.dispose());

        editDialog.setLocationRelativeTo(this);
        editDialog.setVisible(true);
    }


    public void eventClick(Word word,JPanel panel,int x){
        JLabel VIMeaning = new JLabel(word.getMeaning());
        VIMeaning.setFont(new Font("Arial", Font.BOLD, 24));
        VIMeaning.setBounds(220, 30*x, 200, 30);
        VIMeaning.setForeground(Color.WHITE);
        panel.add(VIMeaning);
    }
}
