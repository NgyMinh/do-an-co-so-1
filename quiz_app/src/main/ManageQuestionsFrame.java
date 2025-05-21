package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageQuestionsFrame extends JFrame {
    public ManageQuestionsFrame() {
        setTitle("Quản lý câu hỏi");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 1, 10, 10)); // 2 hàng, khoảng cách giữa các nút

        // Nút "Tạo câu hỏi"
        JButton createQuestionButton = new JButton("Tạo câu hỏi");
        createQuestionButton.setFont(new Font("Arial", Font.PLAIN, 16));
        createQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mở giao diện CreateQuestionFrame
                new CreateQuestionFrame(null).setVisible(true);
            }
        });

        // Nút "Xem danh sách câu hỏi đã tạo"
        JButton viewCreatedQuestionsButton = new JButton("Xem danh sách câu hỏi đã tạo");
        viewCreatedQuestionsButton.setFont(new Font("Arial", Font.PLAIN, 16));
        viewCreatedQuestionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mở lại MenuFrame
                new MenuFrame().setVisible(true);
                dispose(); // Đóng cửa sổ hiện tại
            }
        });

        // Thêm các nút vào giao diện
        add(createQuestionButton);
        add(viewCreatedQuestionsButton);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageQuestionsFrame().setVisible(true));
    }
}