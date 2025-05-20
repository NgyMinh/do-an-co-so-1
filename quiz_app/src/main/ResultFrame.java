package main;

import javax.swing.*;
import java.awt.*;

public class ResultFrame extends JFrame {
    public ResultFrame(int score, int totalQuestions) {
        setTitle("Kết quả");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel resultLabel = new JLabel("Bạn trả lời đúng " + score + " / " + totalQuestions + " câu.");
        resultLabel.setFont(new Font("Serif", Font.BOLD, 18));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton backButton = new JButton("Quay lại menu chính");
        backButton.addActionListener(e -> {
            this.dispose();
            new MenuFrame().setVisible(true);
        });

        setLayout(new BorderLayout());
        add(resultLabel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
    }
}