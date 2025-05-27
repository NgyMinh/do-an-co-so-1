package main;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import quiz_app.utils.SubjectStorage; // Thay đổi package nếu cần

public class MenuFrame extends JFrame {
    private static List<String> subjects = SubjectStorage.loadSubjects(); // Tải danh sách lĩnh vực từ tệp
    private JPanel buttonPanel; // Panel chứa các nút

    public MenuFrame() {
        setTitle("Quiz App - Menu");
        setSize(600, 400); // Kích thước mặc định cho 2 hàng, mỗi hàng 3 lĩnh vực
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tiêu đề
        JLabel titleLabel = new JLabel("Chọn chủ đề bài kiểm tra", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(new EmptyBorder(20, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Panel chứa các nút
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 3, 10, 10)); // 3 lĩnh vực trên mỗi hàng
        buttonPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Thêm thanh cuộn cho buttonPanel
        JScrollPane scrollPane = new JScrollPane(buttonPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        // Hiển thị các lĩnh vực ban đầu
        refreshSubjects();

        // Panel chứa các nút thêm và xóa lĩnh vực
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new GridLayout(1, 2, 10, 0)); // Hai nút nằm trên cùng một hàng

        // Nút "Thêm lĩnh vực"
        addAddButton(actionPanel);

        // Nút "Xóa lĩnh vực"
        addRemoveButton(actionPanel);

        add(actionPanel, BorderLayout.SOUTH); // Thêm Panel chứa nút vào phía dưới giao diện
    }

    // Phương thức thêm lĩnh vực mới
    public static void addSubject(String subject) {
        if (!subjects.contains(subject)) {
            subjects.add(subject); // Thêm lĩnh vực vào danh sách
            SubjectStorage.saveSubjects(subjects); // Lưu danh sách vào tệp
        }
    }

    // Phương thức xóa lĩnh vực
    public static void removeSubject(String subject) {
        if (subjects.contains(subject)) {
            subjects.remove(subject);
            SubjectStorage.saveSubjects(subjects); // Lưu danh sách vào tệp ngay lập tức
        }
    }

    // Phương thức làm mới danh sách lĩnh vực
    public void refreshSubjects() {
        buttonPanel.removeAll(); // Xóa tất cả các nút hiện tại

        // Tạo lại các nút dựa trên danh sách lĩnh vực
        for (String subject : subjects) {
            JButton subjectButton = new JButton(subject);
            subjectButton.addActionListener(e -> openQuizFrame(subject));
            buttonPanel.add(subjectButton);
        }

        buttonPanel.revalidate(); // Cập nhật giao diện
        buttonPanel.repaint();    // Vẽ lại giao diện
    }

    // Nút thêm lĩnh vực
    private void addAddButton(JPanel actionPanel) {
        JButton addButton = new JButton("Thêm lĩnh vực");
        addButton.addActionListener(e -> {
            String newSubject = JOptionPane.showInputDialog(this, "Nhập tên lĩnh vực mới:");
            if (newSubject != null && !newSubject.trim().isEmpty()) {
                addSubject(newSubject.trim()); // Thêm lĩnh vực vào danh sách và lưu
                refreshSubjects();             // Làm mới giao diện
                JOptionPane.showMessageDialog(this, "Lĩnh vực '" + newSubject + "' đã được thêm.");
            }
        });

        // Thêm nút vào Panel hành động
        actionPanel.add(addButton);
    }

    // Nút xóa lĩnh vực
    private void addRemoveButton(JPanel actionPanel) {
        JButton removeButton = new JButton("Xóa lĩnh vực");
        removeButton.addActionListener(e -> {
            // Hiển thị danh sách lĩnh vực để chọn
            String subjectToRemove = (String) JOptionPane.showInputDialog(
                this,
                "Chọn lĩnh vực cần xóa:",
                "Xóa lĩnh vực",
                JOptionPane.PLAIN_MESSAGE,
                null,
                subjects.toArray(), // Danh sách lĩnh vực
                subjects.isEmpty() ? null : subjects.get(0) // Giá trị mặc định
            );

            // Nếu người dùng chọn lĩnh vực, tiến hành xóa
            if (subjectToRemove != null) {
                removeSubject(subjectToRemove); // Xóa lĩnh vực khỏi danh sách và lưu
                refreshSubjects(); // Làm mới giao diện
                JOptionPane.showMessageDialog(this, "Đã xóa lĩnh vực: " + subjectToRemove);
            }
        });

        // Thêm nút vào Panel hành động
        actionPanel.add(removeButton);
    }

    // Mở giao diện bài kiểm tra
    private void openQuizFrame(String subject) {
        QuizFrame quizFrame = new QuizFrame(subject);
        quizFrame.setVisible(true); // Hiển thị giao diện bài kiểm tra
        this.dispose(); // Đóng MenuFrame nếu không cần thiết
    }
}