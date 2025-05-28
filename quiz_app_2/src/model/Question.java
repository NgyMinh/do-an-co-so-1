package model;

public class Question {
    private int id; // id câu hỏi trong database
    private String content;
    private String[] options;
    private int correctAnswerIndex;

    // Constructor có id (cho các câu hỏi đã có id từ DB)
    public Question(int id, String content, String[] options, int correctAnswerIndex) {
        this.id = id;
        this.content = content;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    // Constructor không có id (cho câu hỏi mới chưa có id)
    public Question(String content, String[] options, int correctAnswerIndex) {
        this.id = -1; // hoặc 0, nghĩa là chưa có id
        this.content = content;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    // Getter cho id
    public int getId() {
        return id;
    }

    // Setter cho id (nếu cần thiết, ví dụ khi lưu vào DB và nhận id trả về)
    public void setId(int id) {
        this.id = id;
    }

    // Getter và Setter còn lại giữ nguyên

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }
}
