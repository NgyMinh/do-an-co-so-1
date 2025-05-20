package model;

public class Question {
    private String content;
    private String[] options;
    private int correctAnswerIndex;

    public Question(String content, String[] options, int correctAnswerIndex) {
        this.content = content;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getContent() {
        return content;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
}