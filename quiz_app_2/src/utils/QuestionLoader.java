package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Question;

public class QuestionLoader {

    // Bản đồ ánh xạ giữa lĩnh vực và tên bảng trong cơ sở dữ liệu
    private static final Map<String, String> SUBJECT_TABLE_MAP = new HashMap<>();

    static {
        SUBJECT_TABLE_MAP.put("Toán", "Toan");
        SUBJECT_TABLE_MAP.put("Lịch sử", "LichSu");
        SUBJECT_TABLE_MAP.put("Công nghệ", "CongNghe");
        SUBJECT_TABLE_MAP.put("Y học", "YHoc");
        SUBJECT_TABLE_MAP.put("Ngữ văn", "NguVan");
        SUBJECT_TABLE_MAP.put("Địa lý", "DiaLy");
        SUBJECT_TABLE_MAP.put("GDCD", "GDCD");
        SUBJECT_TABLE_MAP.put("Vật lý", "Ly");
        SUBJECT_TABLE_MAP.put("Hóa học", "Hoa");
    }

    // Xác định tên bảng cơ sở dữ liệu dựa trên lĩnh vực
    private static String getTableName(String subject) {
        if (!SUBJECT_TABLE_MAP.containsKey(subject)) {
            throw new IllegalArgumentException("Chủ đề không hợp lệ: " + subject);
        }
        return SUBJECT_TABLE_MAP.get(subject);
    }

    // Tải danh sách câu hỏi từ cơ sở dữ liệu theo lĩnh vực
    public static List<Question> loadQuestionsFromDatabase(String subject) {
        List<Question> questions = new ArrayList<>();
        String tableName = getTableName(subject);
        String query = "SELECT question, answer_a, answer_b, answer_c, answer_d, correct_answer FROM " + tableName;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String content = rs.getString("question");
                String[] options = {
                    rs.getString("answer_a"),
                    rs.getString("answer_b"),
                    rs.getString("answer_c"),
                    rs.getString("answer_d")
                };
                int correctAnswer = rs.getString("correct_answer").charAt(0) - 'A';

                questions.add(new Question(content, options, correctAnswer));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tải câu hỏi: " + e.getMessage());
            throw new RuntimeException("Lỗi khi tải câu hỏi từ cơ sở dữ liệu.", e);
        }

        return questions;
    }

    // Lưu câu hỏi vào cơ sở dữ liệu theo lĩnh vực
    public static void saveQuestionToDatabase(String subject, Question question) {
        String tableName = getTableName(subject);
        String query = "INSERT INTO " + tableName + " (question, answer_a, answer_b, answer_c, answer_d, correct_answer) VALUES (?, ?, ?, ?, ?, ?)";

        // Kiểm tra tính hợp lệ của câu hỏi và các đáp án
        if (question.getContent() == null || question.getContent().isEmpty()) {
            throw new IllegalArgumentException("Nội dung câu hỏi không được để trống.");
        }

        for (String option : question.getOptions()) {
            if (option == null || option.isEmpty()) {
                throw new IllegalArgumentException("Mỗi đáp án không được để trống.");
            }
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, question.getContent());
            stmt.setString(2, question.getOptions()[0]);
            stmt.setString(3, question.getOptions()[1]);
            stmt.setString(4, question.getOptions()[2]);
            stmt.setString(5, question.getOptions()[3]);
            stmt.setString(6, String.valueOf((char) ('A' + question.getCorrectAnswerIndex())));

            System.out.println("Executing query: " + stmt);
            stmt.executeUpdate();
            System.out.println("Câu hỏi đã được lưu thành công!");

        } catch (SQLException e) {
            System.err.println("Lỗi khi lưu câu hỏi vào cơ sở dữ liệu: " + e.getMessage());
            throw new RuntimeException("Lỗi khi lưu câu hỏi vào cơ sở dữ liệu.", e);
        }
    }
}