package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField nameField;
    private JTextField ageField;
    private JTextField emailField;
    private JCheckBox confirmationCheckBox;
    private JButton startButton;
    private JButton exitButton;
    private JButton manageQuestionsButton; // Nút mới để mở ManageQuestionsFrame

    public LoginFrame() {
        setTitle("Quiz");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tiêu đề
        JLabel titleLabel = new JLabel("ỨNG DỤNG TẠO TRẮC NGHIỆM KIẾN THỨC", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Panel nhập liệu chính
        JPanel inputPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        inputPanel.add(createInputPanel("Nhập tên của bạn:", nameField = new JTextField()));
        inputPanel.add(createInputPanel("Nhập tuổi của bạn:", ageField = new JTextField()));
        inputPanel.add(createInputPanel("Nhập email của bạn:", emailField = new JTextField()));

        // Sửa phần checkbox: bọc vào panel để căn chỉnh đều hơn
        JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        confirmationCheckBox = new JCheckBox("Bạn chắc chắn muốn làm bài test này chứ?");
        checkboxPanel.add(confirmationCheckBox);
        inputPanel.add(checkboxPanel);

        add(inputPanel, BorderLayout.CENTER);

        // Panel nút
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10)); // Thay đổi thành 1 hàng, 3 cột
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        startButton = new JButton("Tiếp tục");
        startButton.addActionListener(new StartButtonListener());

        exitButton = new JButton("Thoát");
        exitButton.addActionListener(e -> System.exit(0));

        manageQuestionsButton = new JButton("Quản lý câu hỏi"); // Nút mới
        manageQuestionsButton.addActionListener(e -> {
            dispose(); // Đóng LoginFrame
            new ManageQuestionsFrame().setVisible(true); // Mở ManageQuestionsFrame
        });

        buttonPanel.add(startButton);
        buttonPanel.add(manageQuestionsButton); // Thêm nút mới vào panel
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createInputPanel(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new GridLayout(2, 1, 3, 3));
        JLabel label = new JLabel(labelText);
        panel.add(label);
        panel.add(textField);
        return panel;
    }

    private class StartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userName = nameField.getText().trim();
            String age = ageField.getText().trim();
            String email = emailField.getText().trim();
            boolean isConfirmed = confirmationCheckBox.isSelected();

            if (userName.isEmpty() || age.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(LoginFrame.this, "Vui lòng nhập đầy đủ thông tin.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!isConfirmed) {
                JOptionPane.showMessageDialog(LoginFrame.this, "Vui lòng xác nhận trước khi bắt đầu.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, "Welcome, " + userName + "!");
                dispose();
                MenuFrame menuFrame = new MenuFrame();
                menuFrame.setVisible(true);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}