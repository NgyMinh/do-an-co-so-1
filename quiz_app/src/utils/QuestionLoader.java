package utils;

import model.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionLoader {
    public static List<Question> loadQuestionsFromDatabase(String subject) {
        List<Question> questions = new ArrayList<>();
        String query = null;

        // Xác định bảng dựa trên subject
        switch (subject) {
            case "Toán":
                query = "SELECT question, optionA, optionB, optionC, optionD, correctAnswer FROM MathQuestions";
                break;
            case "Lịch sử":
                query = "SELECT question, optionA, optionB, optionC, optionD, correctAnswer FROM HistoryQuestions";
                break;
            case "Công nghệ":
                query = "SELECT question, optionA, optionB, optionC, optionD, correctAnswer FROM TechnologyQuestions";
                break;
            case "Tiếng Anh":
                query = "SELECT question, optionA, optionB, optionC, optionD, correctAnswer FROM EnglishQuestions";
                break;
            default:
                throw new IllegalArgumentException("Chủ đề không hợp lệ: " + subject);
        }

        // Thực hiện truy vấn
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String content = rs.getString("question");
                String[] options = {
                    rs.getString("optionA"),
                    rs.getString("optionB"),
                    rs.getString("optionC"),
                    rs.getString("optionD")
                };
                int correctAnswer = rs.getInt("correctAnswer");

                questions.add(new Question(content, options, correctAnswer));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tải câu hỏi: " + e.getMessage());
        }

        return questions;
    }
}