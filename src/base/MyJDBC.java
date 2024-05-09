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
            String sql = "INSERT INTO dictionary (id, word, meaning) VALUES (?, ?, ?)";

            // Tạo một PreparedStatement để thêm dữ liệu vào câu lệnh SQL
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, oldWord.size());
                pstmt.setString(2, englishWord);
                pstmt.setString(3, vietnameseMeaning);

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

    public static void removeDuplicatesFromDatabase() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            // Tạo một câu lệnh SQL để lấy ra tất cả các từ trùng lặp
            String sql = "SELECT word FROM dictionary GROUP BY word HAVING COUNT(*) > 1";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet resultSet = pstmt.executeQuery();

                while (resultSet.next()) {
                    String duplicateWord = resultSet.getString("word");
                    // Xóa tất cả các bản sao trừ bản gốc đầu tiên
                    String deleteSql = "DELETE FROM dictionary WHERE word = ? AND id NOT IN (SELECT MIN(id) FROM (SELECT id FROM dictionary WHERE word = ?) AS t)";
                    try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                        deleteStmt.setString(1, duplicateWord);
                        deleteStmt.setString(2, duplicateWord);
                        deleteStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void modifyWordInDatabase(String originalWord, String alteredWord, String alteredMeaning) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            // Tạo một câu lệnh SQL để tìm từ cần thay đổi
            String selectSql = "SELECT * FROM dictionary WHERE word = ?";

            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                selectStmt.setString(1, originalWord);

                // Thực thi câu lệnh SQL để tìm từ cần thay đổi
                ResultSet resultSet = selectStmt.executeQuery();

                if (resultSet.next()) {
                    // Nếu tìm thấy từ cần thay đổi, lấy id của từ đó
                    int wordId = resultSet.getInt("id");

                    // Tạo một câu lệnh SQL để cập nhật từ và nghĩa mới
                    String updateSql = "UPDATE dictionary SET word = ?, meaning = ? WHERE id = ?";

                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setString(1, alteredWord);
                        updateStmt.setString(2, alteredMeaning);
                        updateStmt.setInt(3, wordId);

                        // Thực thi câu lệnh SQL để cập nhật từ và nghĩa mới
                        int rowsAffected = updateStmt.executeUpdate();

                        if (rowsAffected > 0) {
                            System.out.println("Từ đã được sửa đổi thành công.");
                        } else {
                            System.out.println("Không có từ nào được sửa đổi trong cơ sở dữ liệu.");
                        }
                    }
                } else {
                    System.out.println("Không tìm thấy từ cần sửa đổi trong cơ sở dữ liệu.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

