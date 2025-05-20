package main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuFrame extends JFrame {
    public MenuFrame() {
        setTitle("Quiz App - Menu"); // Tiêu đề cho cửa sổ
        setSize(500, 500); // Chiều cao cửa sổ
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout()); // Cần để dùng NORTH, CENTER,...

        // Tiêu đề
        JLabel titleLabel = new JLabel("Chọn chủ đề bài kiểm tra", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(new EmptyBorder(25, 0, 0, 0)); // padding: top, left, bottom, right
        
        // Tạo panel chính chứa các nút
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10)); // khoảng cách giữa các nút
        buttonPanel.setBorder(new EmptyBorder(30, 20, 30, 20)); // padding: top, left, bottom, right
        
        // Nút chọn chủ đề
        JButton mathButton = new JButton("Toán");
        JButton historyButton = new JButton("Lịch sử");
        JButton techButton = new JButton("Công nghệ");
        JButton englishButton = new JButton("Tiếng anh");

        // Thêm sự kiện cho các nút
        mathButton.addActionListener(e -> openQuizFrame("Toán"));
        historyButton.addActionListener(e -> openQuizFrame("Lịch sử"));
        techButton.addActionListener(e -> openQuizFrame("Công nghệ"));
        englishButton.addActionListener(e -> openQuizFrame("Tiếng anh"));

        // Thêm nút vào giao diện
        buttonPanel.add(mathButton);
        buttonPanel.add(historyButton);
        buttonPanel.add(techButton);
        buttonPanel.add(englishButton);

     // Thêm tiêu đề và panel vào frame
        add(titleLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

    }

    private void openQuizFrame(String subject) {
        // Đóng cửa sổ hiện tại và mở QuizFrame
        this.dispose();
        QuizFrame quizFrame = new QuizFrame(subject);
        quizFrame.setVisible(true);
    }
}