package Screen;

import constants.CommonConstants;
import database.JDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreateQuestionScreenGui extends JFrame {
    private JTextArea questionTextArea;
    private JTextField categoryTextField;
    private JTextField[] answerTextFields;
    private ButtonGroup buttonGroup;
    private JRadioButton[] answerRatioButton;
    public CreateQuestionScreenGui() {
        super("Create a Question");
        setSize(851,565);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(CommonConstants.LIGHT_BLUE.darker());

        answerRatioButton = new JRadioButton[4];
        answerTextFields = new JTextField[4];
        buttonGroup = new ButtonGroup();

        addGuiComponents();
    }

    private void addGuiComponents() {
        JLabel titleLabel = new JLabel("Create your own Question");
        titleLabel.setFont(new Font("Arial",Font.BOLD,24));
        titleLabel.setBounds(50,15,310,29);
        titleLabel.setForeground(CommonConstants.BRIGHT_YELLOW);
        add(titleLabel);

        JLabel questionLabel = new JLabel("Question: ");
        questionLabel.setFont(new Font("Arial",Font.BOLD,16));
        questionLabel.setBounds(50,60,93,20);
        questionLabel.setForeground(CommonConstants.BRIGHT_YELLOW);
        add(questionLabel);

        // question text area
        questionTextArea = new JTextArea();
        questionTextArea.setFont(new Font("Arial",Font.BOLD,16));
        questionTextArea.setBounds(50,90,310,110);
        questionTextArea.setForeground(CommonConstants.BLACK);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        add(questionTextArea);

        // category label
        JLabel categoryLabel = new JLabel("Category: ");
        categoryLabel.setFont(new Font("Arial",Font.BOLD,16));
        categoryLabel.setBounds(50,250,93,20);
        categoryLabel.setForeground(CommonConstants.BRIGHT_YELLOW);
        add(categoryLabel);

        // category text area
        categoryTextField = new JTextField();
        categoryTextField.setFont(new Font("Arial",Font.BOLD,16));
        categoryTextField.setBounds(50,280,310,36);
        categoryTextField.setForeground(CommonConstants.BLACK);
        add(categoryTextField);


        addAnswerComponents();

        // submit button
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial",Font.BOLD,16));
        submitButton.setBounds(300,450,262,45);
        submitButton.setForeground(CommonConstants.DARK_BLUE);
        submitButton.setBackground(CommonConstants.BRIGHT_YELLOW);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    String question = questionTextArea.getText();
                    String category = categoryTextField.getText();
                    String[] answers = new String[answerTextFields.length];
                    int correctIndex = 0;
                    for (int i = 0; i < answerTextFields.length; i++) {
                        answers[i] = answerTextFields[i].getText();
                        if (answerRatioButton[i].isSelected()) {
                            correctIndex = i;
                        }
                    }

                    // update database
                    if (JDBC.saveQuestionCategoryAndAnswersToDatabase(question, category
                            , answers, correctIndex)) {
                        // update successful
                        JOptionPane.showMessageDialog(CreateQuestionScreenGui.this,
                                "Successfully Added Question!");

                        // reset fields
                        resetField();
                    } else {
                        // update failed
                        JOptionPane.showMessageDialog(CreateQuestionScreenGui.this,
                                "Failed to Add Question...");
                    }
                } else {
                    // invalid input
                    JOptionPane.showMessageDialog(CreateQuestionScreenGui.this,
                            "ErrorL Invalid Input");
                }
            }
        });
        add(submitButton);

        // go back label
        JLabel goBackLabel = new JLabel("Go Back");
        goBackLabel.setFont(new Font("Arial",Font.BOLD,16));
        goBackLabel.setBounds(300,500,262,20);
        goBackLabel.setForeground(CommonConstants.BRIGHT_YELLOW);
        goBackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        goBackLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // display title screen
                TitleScreenGui titleScreenGui = new TitleScreenGui();
                titleScreenGui.setLocationRelativeTo(CreateQuestionScreenGui.this);

                // dispose of this screen
                CreateQuestionScreenGui.this.dispose();

                // make title screen visable
                titleScreenGui.setVisible(true);
            }
        });
        add(goBackLabel);
    }

    private void addAnswerComponents() {
        // vertical spacing between each answer component
        int verticalSpacing = 100;

        // create 4 answer labels, 4 radio buttons, and 4 text input fields
        for (int i = 0; i < 4; i++) {
            JLabel answerLabel = new JLabel("Answer #" + (i + 1));
            answerLabel.setFont(new Font("Arial",Font.BOLD,16));
            answerLabel.setBounds(470,60 + (i * verticalSpacing), 93,20);
            answerLabel.setForeground(CommonConstants.BRIGHT_YELLOW);
            add(answerLabel);

            // ratio button
            answerRatioButton[i] = new JRadioButton();
            answerRatioButton[i].setBounds(440, 100 + (i * verticalSpacing), 21,21);
            answerRatioButton[i].setBackground(null);
            buttonGroup.add(answerRatioButton[i]);
            add(answerRatioButton[i]);

            // answer text input field
            answerTextFields[i] = new JTextField();
            answerTextFields[i].setBounds(470, 90 + (i * verticalSpacing), 310,36);
            answerTextFields[i].setFont(new Font("Arial",Font.BOLD,16));
            answerTextFields[i].setForeground(CommonConstants.DARK_BLUE);
            add(answerTextFields[i]);
        }

        // give a default value to the first radio button
        answerRatioButton[0].setSelected(true);
    }

    /**
     * true - valid input
     * false - invalid input
     */
    private boolean validateInput() {
        // make sure that question field is not empty
        if (questionTextArea.getText().replaceAll(" ","").length() <= 0) return false;

        // make sure that the category field is not empty
        if (categoryTextField.getText().replaceAll(" ","").length() <= 0) return false;

        // make sure all answer fields are not empty
        for (int i = 0; i < answerTextFields.length; i++) {
            if (answerTextFields[i].getText().replaceAll(" ","").length() <= 0)
                return false;
        }

        return true;
    }

    private void resetField() {
        questionTextArea.setText("");
        categoryTextField.setText("");
        for (int i = 0; i < answerTextFields.length; i++) {
            answerTextFields[i].setText("");
        }
    }
}
