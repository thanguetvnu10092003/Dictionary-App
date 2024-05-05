package base;

import java.sql.*;
import java.util.Collections;

import static base.Dictionary.oldWord;

public class MyJDBC {
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/word_schema";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "chutudu14061998";
    public static void importDatabase() {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL,USERNAME,PASSWORD);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM DICTIONARY");

            while (resultSet.next()) {
                Word new_word = new Word(resultSet.getString("word"), resultSet.getString("meaning"));
                oldWord.add(new_word);
            }
            Collections.sort(oldWord);
            DictionaryManagement.removeDuplicates();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertWordToDatabase(String englishWord, String vietnameseMeaning) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            // Tạo một câu lệnh SQL để chèn dữ liệu
            String sql = "INSERT INTO dictionary (word, meaning) VALUES (?, ?)";

            // Tạo một PreparedStatement để thêm dữ liệu vào câu lệnh SQL
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, englishWord);
                pstmt.setString(2, vietnameseMeaning);

                // Thực thi câu lệnh SQL
                pstmt.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeWordFromDatabase(String englishWord) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "DELETE FROM dictionary WHERE word = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, englishWord);

                pstmt.executeUpdate();
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

