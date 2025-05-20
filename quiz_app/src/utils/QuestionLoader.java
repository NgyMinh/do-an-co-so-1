package utils;

import model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionLoader {
    public static List<Question> loadQuestionsFromDatabase(String subject) {
        // Tạm thời dùng dữ liệu mẫu
        List<Question> questions = new ArrayList<>();

        if (subject.equals("Toán")) {
            questions.add(new Question(
                "5 + 3 bằng bao nhiêu?",
                new String[]{"6", "7", "8", "9"},
                2 // Đáp án đúng là "8"
            ));
            questions.add(new Question(
                "12 - 7 bằng bao nhiêu?",
                new String[]{"2", "3", "4", "5"},
                3 // Đáp án đúng là "5"
            ));
        } else if (subject.equals("Lịch sử")) {
            questions.add(new Question(
                "Ai là người lãnh đạo phong trào Cần Vương?",
                new String[]{"Trương Định", "Nguyễn Ái Quốc", "Phan Đình Phùng", "Nguyễn Huệ"},
                2 // Đáp án đúng là "Phan Đình Phùng"
            ));
            questions.add(new Question(
                "Chiến dịch Điện Biên Phủ diễn ra vào năm nào?",
                new String[]{"1945", "1954", "1960", "1975"},
                1 // Đáp án đúng là "1954"
            ));
        } else if (subject.equals("Công nghệ")) {
            questions.add(new Question(
                "Java là gì?",
                new String[]{"Ngôn ngữ lập trình", "Hệ điều hành", "Trình duyệt web", "Phần mềm đồ họa"},
                0 // Đáp án đúng là "Ngôn ngữ lập trình"
            ));
            questions.add(new Question(
                "HTML viết tắt của?",
                new String[]{"HyperText Markup Language", "HighText Machine Language", "HyperText Machine Language", "None of the above"},
                0 // Đáp án đúng là "HyperText Markup Language"
            ));
        }

        return questions;
    }
}