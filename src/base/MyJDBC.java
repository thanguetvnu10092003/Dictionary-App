package base;

import java.sql.*;

import static base.Dictionary.oldWord;

public class MyJDBC {
    public static void importDatabase() {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/word_schema",
                    "root",
                    "chutudu14061998");

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM DICTIONARY");

            while (resultSet.next()) {
                Word new_word = new Word(resultSet.getString("word"), resultSet.getString("meaning"));
                oldWord.add(new_word);
            }
            DictionaryManagement.removeDuplicates();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

