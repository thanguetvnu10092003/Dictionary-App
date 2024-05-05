package Screen;

import base.*;
import constants.CommonConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DictionaryGUI extends JFrame {
    private JTextField searchBar;  // search bar
    private JPanel buttonPanel;    // button

    private JPanel wordListPanel;
    private ArrayList<Word> favoriteWords;
    private static final double MAX_HISTORY_SIZE = 1000;
    private static final String FAVOURITE_FILE = "src/base/favourite.txt";
    private ArrayList<Word> historyWords = new ArrayList<>();


    public DictionaryGUI() {
        super("Dictionary VI EN");
        setSize(1200, 800);
        addGuiComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close
        setResizable(false);
        setVisible(true);
        favoriteWords = new ArrayList<>();
        updateWordToList(DictionaryManagement.oldWord);
        readFavoriteWordsFromFile();
    }


    public void updateWordToList(ArrayList<Word> words) {
        wordListPanel.removeAll();
        int i = 0;
        for (Word word : words) {
            displayComponent(word, wordListPanel, i);
            i++;
        }
        wordListPanel.revalidate();
        wordListPanel.repaint();
    }

    public void displayComponent(Word word,JPanel panel,int x){
        panel.setLayout(null);

        JButton ENMeaning = new JButton(word.getSearching());
        ENMeaning.setFont(new Font("Arial", Font.BOLD, 24));
        ENMeaning.setBounds(10, 30 * x, 250, 30);
        ENMeaning.setForeground(Color.DARK_GRAY);
        ENMeaning.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextArea vietnameseFrame = new JTextArea();
                vietnameseFrame.setBackground(CommonConstants.DARK_BLUE);
                vietnameseFrame.setSize(500,500);
                vietnameseFrame.setFont(new Font("Arial",Font.BOLD,55));
                vietnameseFrame.setLineWrap(true);
                vietnameseFrame.setEditable(false);
                vietnameseFrame.setWrapStyleWord(true);
                vietnameseFrame.setBounds(300,0,1000,3000);
                vietnameseFrame.setForeground(Color.pink);

                ImageIcon icon1 = new ImageIcon("src/resource/media/resource/speak.png");
                JButton voiceEnglishButton = new JButton(icon1);
                voiceEnglishButton.setSize(50,50);
                voiceEnglishButton.setBounds(0,650,50,50);
                voiceEnglishButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            Voice.speakWord(word.getSearching());
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });

                ImageIcon icon2 = new ImageIcon("src/resource/media/resource/favourite.png");
                JButton favouriteButton = new JButton(icon2);
                favouriteButton.setSize(50,50);
                favouriteButton.setBounds(0, 500,50,50);
                favouriteButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ENMeaning.setForeground(Color.red);
                        toggleFavoriteWord(word,ENMeaning);
                    }
                });
                vietnameseFrame.add(favouriteButton);
                vietnameseFrame.add(voiceEnglishButton);

                Component[] components = panel.getComponents();
                for (Component component : components) {
                    if (component instanceof JTextArea) {
                        panel.remove(component);
                    }
                }

                try {
                    vietnameseFrame.setText("\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "English to Vietnamese:\n" + "\n" +
                            word.getSearching() + " = " + API.googleTranslate("en","vi",word.getSearching()));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                panel.add(vietnameseFrame);
                panel.revalidate();
                panel.repaint();


                // Thêm từ vào lịch sử
                historyWords.add(word);
                // Giới hạn số lượng từ trong lịch sử

                if (historyWords.size() > MAX_HISTORY_SIZE) {
                    historyWords.remove(0); // Loại bỏ từ cũ nhất
                }
                removeDuplicatesHistoryWords();
            }
        });
        panel.add(ENMeaning);
    }

    private void addGuiComponents() {
        // import from text file
        DictionaryManagement.setInputPath("src/base/test.txt");
        DictionaryManagement.insertFromFile();

        // import from MySQL database
        MyJDBC.importDatabase();

        ArrayList<Word> words = DictionaryManagement.oldWord;

        // Menu
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.cyan);
        headerPanel.setPreferredSize(new Dimension(1200, 50));
        JLabel headerLabel = new JLabel("Menu");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(headerLabel);
        this.add(headerPanel, BorderLayout.NORTH);

        // Searchbar
        setupSearchBar();

        // In word
        wordListPanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                int height = super.getComponentCount() * 30; // Assuming each word has a height of 30 pixels
                return new Dimension(1000,height);
            }
        };
        wordListPanel.setLayout(new BoxLayout(wordListPanel, BoxLayout.Y_AXIS));
        wordListPanel.setBackground(Color.cyan);
        int i = 0;
        for (Word word : words) {
            displayComponent(word, wordListPanel,i);
            i++;
        }

        JScrollPane scrollPane = new JScrollPane(wordListPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(scrollPane, BorderLayout.CENTER);

        getContentPane().setBackground(Color.cyan);
    }

    private void setupSearchBar() {
        // Search bar
        searchBar = new JTextField(20);  // Initially set the columns of the text field
        searchBar.setFont(new Font("Arial", Font.PLAIN, 18));
        searchBar.setPreferredSize(new Dimension(50, 10));  // Set the preferred size

        // Button panel
        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.cyan);

        // Adding buttons with icons
        addIconButton("search.png", "Search");
        addIconButton("edit.png", "Edit");
        addIconButton("game.jpg", "Game");
        addIconButton("history.png", "History");
        addIconButton("remove.png", "Remove");
        addIconButton("home.png", "Home");
        addIconButton("save.png", "Save as");
        addIconButton("import.png", "Import file text");

        // Adding components to the top
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchBar, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.EAST);
        this.add(searchPanel, BorderLayout.NORTH);
    }

    private void addIconButton(String iconName, String tooltip) {
        String imagePath = "src/resource/media/resource/" + iconName;
        ImageIcon icon = new ImageIcon(imagePath);
        JButton button = new JButton(icon);
        button.setToolTipText(tooltip);
        button.addActionListener(e -> {
            switch (tooltip) {
                case "Edit":
                    openEditDialog();
                    break;
                case "Import file text":
                    openFileChooseDialog();
                    break;
                case "Game":
                    Game.play();
                    break;
                case "Search":
                    // Handling search action
                    String searchQuery = searchBar.getText();
                    if (!searchQuery.isEmpty()) {
                        searchAndUpdateResults(searchQuery);
                    } else {
                        JOptionPane.showMessageDialog(null, "Please enter a word to search.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case "History":
                    if (historyWords.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "There is no previous words.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else
                        displayHistoryWords();
                    break;
                case "Home":
                    resetState();
                    break;
                case "Remove":
                    openRemoveDialog();
                    break;
                case "Save as":
                    JFileChooser fileChooser = new JFileChooser();
                    int returnValue = fileChooser.showOpenDialog(this);

                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        String fileName = selectedFile.getName();

                        if (fileName.isEmpty()) {
                            JOptionPane.showConfirmDialog(this, "Please enter a file name.", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        }

                        if (!fileName.endsWith(".txt")) {
                            selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
                        }

                        saveToFile(DictionaryManagement.oldWord, selectedFile.getAbsolutePath());
                        JOptionPane.showMessageDialog(null, "Export successful text file.");
                    }
                    break;
            }
        });
        buttonPanel.add(button);
    }

    private void resetState() {
        searchBar.setText(""); // Clear the search bar

        ArrayList<Word> allWords = DictionaryManagement.oldWord;
        updateWordToList(allWords);
        // Revalidate and repaint the frame to reflect changes

    }

    private void openRemoveDialog() {
        JDialog removeDialog = new JDialog(this, "Remove Word", true);
        removeDialog.setLayout(new GridLayout(2, 1));  // Grid layout for labels, text fields, and buttons
        removeDialog.setSize(400, 150);

        // Label and text field for input
        JLabel wordLabel = new JLabel("Enter word to remove:");
        JTextField wordTextField = new JTextField();

        // Buttons for removing or cancelling
        JButton removeButton = new JButton("Remove");
        JButton cancelButton = new JButton("Cancel");

        // Adding components to the dialog
        removeDialog.add(wordLabel);
        removeDialog.add(wordTextField);
        removeDialog.add(removeButton);
        removeDialog.add(cancelButton);

        // Remove button
        removeButton.addActionListener(e -> {
            String wordToRemove = wordTextField.getText();
            boolean wordExists = false;
            for (Word word : DictionaryManagement.oldWord) {
                if (word.getSearching().equalsIgnoreCase(wordToRemove)) {
                    wordExists = true;
                    break;
                }
            }
            if (wordExists) {
                int response = JOptionPane.showConfirmDialog(removeDialog,
                        "Do you really want to remove the word: " + wordToRemove + "?", "Confirm Remove", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    DictionaryManagement.oldWord.removeIf(word -> word.getSearching().equalsIgnoreCase(wordToRemove));
                    JOptionPane.showMessageDialog(removeDialog, "Word removed successfully!");
                    updateWordToList(DictionaryManagement.oldWord);
                    MyJDBC.removeWordFromDatabase(wordToRemove);
                }
            } else {
                JOptionPane.showMessageDialog(removeDialog, "Word not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Cancel button
        cancelButton.addActionListener(e -> removeDialog.dispose());

        //Location window
        removeDialog.setLocationRelativeTo(this);
        removeDialog.setVisible(true);
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
                DictionaryManagement.setInputPath("src/base/default.txt");
                DictionaryManagement.addWord(englishWord, vietnameseMeaning);
                // Assuming a method to save directly to the file
                DictionaryManagement.exportToFile("src/base/1.txt");
                JOptionPane.showMessageDialog(editDialog, "Word added successfully!");
                updateWordToList(DictionaryManagement.oldWord);
                MyJDBC.insertWordToDatabase(englishWord, vietnameseMeaning);
            } else {
                JOptionPane.showMessageDialog(editDialog, "Please fill both fields!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action listener for the Cancel button
        cancelButton.addActionListener(e -> editDialog.dispose());

        editDialog.setLocationRelativeTo(this);
        editDialog.setVisible(true);
    }

    private void searchAndUpdateResults(String query) {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(Color.cyan);
        int count = 0;

        if (query.trim().isEmpty()) {
            // Nếu chuỗi tìm kiếm rỗng, hiển thị tất cả các từ
            for (Word word : DictionaryManagement.oldWord) {
                displayComponent(word, resultPanel, count++);
            }
        } else {
            // Tìm kiếm các từ khớp với chuỗi tìm kiếm
            query = query.trim().toLowerCase();
            for (Word word : DictionaryManagement.oldWord) {
                if (word.getSearching().toLowerCase().contains(query)) {
                    displayComponent(word, resultPanel, count++);
                }
            }
        }

        // Tạo và thiết lập JScrollPane mới chứa resultPanel
        JScrollPane scrollPane = new JScrollPane(resultPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Thay thế panel hiển thị kết quả tìm kiếm cũ
        if (this.getContentPane().getComponentCount() > 2) {
            this.getContentPane().remove(2); // Giả sử scrollPane là thành phần thứ 3 trong content pane
        }
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);
        this.getContentPane().revalidate(); // Yêu cầu JFrame validate lại layout sau khi thay đổi
        this.getContentPane().repaint(); // Yêu cầu JFrame vẽ lại để hiển thị các thay đổi
    }

    private void readFavoriteWordsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FAVOURITE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Tách từ và loại đánh dấu từ dòng đọc được từ tệp tin
                String[] parts = line.split("\t");
                if (parts.length >= 2) { // Kiểm tra xem dòng có ít nhất 2 phần tử không trước khi tiếp tục
                    String searching = parts[0];
                    boolean isFavorite = Boolean.parseBoolean(parts[1]);

                    // Tìm từ trong danh sách và đánh dấu màu nền tương ứng
                    for (Component comp : wordListPanel.getComponents()) {
                        if (comp instanceof JButton) {
                            JButton button = (JButton) comp;
                            if (button.getText().equals(searching)) {
                                if (isFavorite) {
                                    button.setBackground(CommonConstants.BRIGHT_YELLOW);
                                    button.setForeground(Color.red);
                                    favoriteWords.add(new Word(searching, "")); // Thêm từ vào danh sách yêu thích
                                } else {
                                    button.setBackground(null); // Đặt màu nền về mặc định nếu không phải từ yêu thích
                                }
                                break;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void writeFavoriteWordsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FAVOURITE_FILE))) {
            for (Word word : favoriteWords) {
                // Ghi từ và loại đánh dấu vào tệp tin
                writer.write(word.getSearching() + "\t" + word.getMeaning());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isFavoriteWord(Word word) {
        return favoriteWords.contains(word);
    }

    private void toggleFavoriteWord(Word word, JButton button) {
        // Thêm hoặc xóa từ khỏi danh sách yêu thích
        if (isFavoriteWord(word)) {
            favoriteWords.remove(word);
            button.setBackground(null); // Đặt màu nền về mặc định
        } else {
            favoriteWords.add(word);
            button.setBackground(CommonConstants.BRIGHT_YELLOW);
        }
        // Ghi danh sách từ yêu thích vào tệp tin
        writeFavoriteWordsToFile();
    }

    private void displayHistoryWords() {
        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        historyPanel.setBackground(Color.cyan);

        int x = 0;
        for (Word word : historyWords) {
            displayComponent(word, historyPanel, x); // Hiển thị từng từ trong lịch sử
            x++;
        }

        JScrollPane scrollPane = new JScrollPane(historyPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        if (this.getContentPane().getComponentCount() > 2) {
            this.getContentPane().remove(2); // Loại bỏ panel hiện tại nếu có
        }
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);
        this.getContentPane().revalidate();
        this.getContentPane().repaint();
        removeDuplicatesHistoryWords();
    }

    private void removeDuplicatesHistoryWords() {
        Set<Word> uniqueWord = new HashSet<>();
        List<Word> nonDuplicatesWord = new ArrayList<>();

        for (Word w : historyWords) {
            if (!uniqueWord.contains(w)) {
                uniqueWord.add(w);
                nonDuplicatesWord.add(w);
            }
        }

        historyWords.clear();
        historyWords.addAll(nonDuplicatesWord);
    }

    public static void saveToFile(ArrayList<Word> words, String directoryPath) {

        // Tạo một tệp mới trong thư mục đã chỉ định với tên tệp là fileName và đuôi là .txt
        File file = new File(directoryPath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Word word : words) {
                writer.write(word.getSearching() + "\t" + word.getMeaning());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Tách từ và nghĩa từ dòng đọc được từ tệp tin
                String[] parts = line.split("\t");
                if (parts.length >= 2) { // Kiểm tra xem dòng có ít nhất 2 phần tử không trước khi tiếp tục
                    String searching = parts[0];
                    String meaning = parts[1];

                    // Tạo một đối tượng Word mới và thêm vào danh sách từ điển
                    Word word = new Word(searching, meaning);
                    DictionaryManagement.oldWord.add(word);
                }
            }

            // Cập nhật giao diện người dùng để hiển thị danh sách từ điển mới
            updateWordToList(DictionaryManagement.oldWord);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openFileChooseDialog() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            importFromFile(selectedFile.getAbsolutePath());
        }
    }

}