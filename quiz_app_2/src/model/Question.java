package model;

public class Question {
    private String content;
    private String[] options;
    private int correctAnswer;

    public Question(String content, String[] options, int correctAnswer) {
        this.content = content;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getContent() {
        return content;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswer;
    }
}