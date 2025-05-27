package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import model.Question;
import utils.QuestionLoader;

public class QuizFrame extends JFrame {
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;

    private JPanel answerPanel;
    private JLabel questionLabel;
    private JRadioButton[] answerButtons;
    private ButtonGroup answerGroup;
    private JButton submitButton; // Nút chọn đáp án
    private JButton backButton; // Nút "Trở về"

    public QuizFrame(String subject) {
        setTitle("Quiz - " + subject);
        setSize(550, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tải câu hỏi từ cơ sở dữ liệu
        questions = QuestionLoader.loadQuestionsFromDatabase(subject);

        if (questions == null || questions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có câu hỏi nào cho chủ đề này!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        // Panel chứa các nút số câu hỏi (hiển thị theo chiều ngang)
        JPanel questionListPanel = new JPanel();
        questionListPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Sắp xếp ngang, khoảng cách giữa các nút
        questionListPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Tạo nút cho từng câu hỏi
        for (int i = 0; i < questions.size(); i++) {
            JButton questionButton = new JButton("Câu " + (i + 1));
            questionButton.setFont(new Font("Arial", Font.PLAIN, 14));
            int questionIndex = i; // Lưu chỉ số câu hỏi hiện tại
            questionButton.addActionListener(e -> {
                currentQuestionIndex = questionIndex;
                displayQuestion(); // Hiển thị câu hỏi tương ứng
            });
            questionListPanel.add(questionButton);
        }

        // Panel chính chứa tiêu đề câu hỏi và các đáp án
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setBorder(new EmptyBorder(5, 30, 20, 30)); // Tạo padding cho Panel

        // Tiêu đề câu hỏi
        questionLabel = new JLabel("Câu hỏi sẽ hiển thị ở đây", SwingConstants.CENTER); // Tiêu đề Câu hỏi
        questionLabel.setFont(new Font("Arial", Font.BOLD, 15)); // set Font, cỡ chữ cho câu hỏi
        questionPanel.add(questionLabel, BorderLayout.NORTH); // Thêm tiêu đề câu hỏi vào trên

        // Tạo panel chứa các đáp án (căn giữa)
        JPanel answerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        answerPanel = new JPanel(new GridLayout(2, 2, 15, 10)); // Grid đáp án (2 dòng, 2 cột, có khoảng cách)

        answerButtons = new JRadioButton[4];
        answerGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            answerButtons[i] = new JRadioButton();
            answerButtons[i].setFont(new Font("Arial", Font.PLAIN, 14)); // Font rõ ràng
            answerGroup.add(answerButtons[i]);
            answerPanel.add(answerButtons[i]);
        }

        answerWrapper.add(answerPanel); // Đưa lưới đáp án vào wrapper
        questionPanel.add(answerWrapper, BorderLayout.CENTER); // Đặt wrapper vào giữa panel câu hỏi

        add(questionListPanel, BorderLayout.NORTH); // Thêm danh sách câu hỏi ở phía trên
        add(questionPanel, BorderLayout.CENTER); // Thêm câu hỏi + đáp án vào giữa

        // Panel chứa nút "Chọn đáp án" và "Trở về"
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(0, 10, 20, 10));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        backButton = new JButton("Trở về");
        backButton.addActionListener(new BackButtonListener());

        submitButton = new JButton("Chọn đáp án");
        submitButton.addActionListener(new SubmitButtonListener());

        // Kích thước các nút
        Dimension size = new Dimension(150, 30);
        backButton.setMinimumSize(size);
        backButton.setPreferredSize(size);
        backButton.setMaximumSize(size);
        submitButton.setMinimumSize(size);
        submitButton.setPreferredSize(size);
        submitButton.setMaximumSize(size);

        // Căn giữa các button vào panel
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(backButton);
        buttonPanel.add(Box.createHorizontalStrut(10)); // khoảng cách giữa 2 button
        buttonPanel.add(submitButton);
        buttonPanel.add(Box.createHorizontalGlue());

        add(buttonPanel, BorderLayout.SOUTH);
        displayQuestion(); // Hiển thị câu hỏi đầu tiên
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questions.size() && currentQuestionIndex >= 0) {
            Question question = questions.get(currentQuestionIndex);
            questionLabel.setText("Câu hỏi " + (currentQuestionIndex + 1) + ": " + question.getContent());
            String[] options = question.getOptions();

            // Xóa trạng thái được chọn của ButtonGroup
            answerGroup.clearSelection();

            // Cập nhật nội dung cho các đáp án
            for (int i = 0; i < options.length; i++) {
                answerButtons[i].setText(options[i]);
            }
        } else if (currentQuestionIndex >= questions.size()) {
            // Kết thúc bài kiểm tra
            JOptionPane.showMessageDialog(this, "Bạn đã hoàn thành bài kiểm tra! Điểm: " + score);
            this.dispose();
            new ResultFrame(score, questions.size()).setVisible(true);
        }
    }

    // Xử lí sự kiện nút chọn đáp án
    private class SubmitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentQuestionIndex < questions.size()) {
                Question question = questions.get(currentQuestionIndex);
                int selectedAnswer = -1;
                for (int i = 0; i < answerButtons.length; i++) {
                    if (answerButtons[i].isSelected()) {
                        selectedAnswer = i;
                        break;
                    }
                }
                if (selectedAnswer == question.getCorrectAnswerIndex()) {
                    score++;
                }
                currentQuestionIndex++;
                displayQuestion();
            }
        }
    }

    // Lớp xử lý sự kiện cho nút "Trở về"
    private class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--; // Quay về câu hỏi trước đó
                displayQuestion();
            } else {
                JOptionPane.showMessageDialog(QuizFrame.this, "Bạn đang ở câu hỏi đầu tiên!");
            }
        }
    }
}