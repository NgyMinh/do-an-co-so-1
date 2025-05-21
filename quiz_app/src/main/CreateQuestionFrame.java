package main;

import model.Question;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateQuestionFrame extends JFrame {
	private JTextField creatorNameField; // Họ và tên người tạo
	private JComboBox<String> durationComboBox; // Thời gian làm bài
	private JComboBox<String> mainCategoryComboBox; // Nhóm lĩnh vực
	private JComboBox<String> subCategoryComboBox; // Lĩnh vực cụ thể

	private JTextArea questionTextArea; // Nội dung câu hỏi
	private JTextField[] answerFields; // Đáp án A-D
	private ButtonGroup correctAnswerGroup; // Đáp án đúng
	private JLabel currentQuestionLabel; // Thứ tự câu hỏi

	private List<Question> questionList; // Danh sách câu hỏi chính
	private List<Question> temporaryQuestions; // Danh sách tạm thời
	private int currentQuestionIndex = 1; // Số thứ tự câu hỏi hiện tại

	public CreateQuestionFrame(List<Question> questionList) {
		this.questionList = questionList;
		this.temporaryQuestions = new ArrayList<>();

		setTitle("Tạo Câu Hỏi Mới");
		setSize(700, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		// Panel Thông tin chung
		JPanel generalInfoPanel = new JPanel(new GridLayout(4, 2, 10, 10));
		generalInfoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin chung"));

		// Họ và tên người tạo
		generalInfoPanel.add(new JLabel("Họ và tên người tạo:"));
		creatorNameField = new JTextField();
		generalInfoPanel.add(creatorNameField);

		// Nhóm lĩnh vực
		generalInfoPanel.add(new JLabel("Nhóm lĩnh vực:"));
		mainCategoryComboBox = new JComboBox<>(new String[] { "Khoa học Tự nhiên", "Khoa học Xã hội" });
		mainCategoryComboBox.addActionListener(e -> updateSubCategories());
		generalInfoPanel.add(mainCategoryComboBox);

		// Lĩnh vực cụ thể
		generalInfoPanel.add(new JLabel("Lĩnh vực:"));
		subCategoryComboBox = new JComboBox<>();
		updateSubCategories(); // Cập nhật lĩnh vực ban đầu
		generalInfoPanel.add(subCategoryComboBox);

		// Thời gian làm bài
		generalInfoPanel.add(new JLabel("Thời gian làm bài (phút):"));
		durationComboBox = new JComboBox<>(new String[] { "15", "30", "45", "60", "90", "120" });
		generalInfoPanel.add(durationComboBox);

		add(generalInfoPanel, BorderLayout.NORTH);

		// Panel Thông tin câu hỏi
		JPanel questionInfoPanel = new JPanel(new GridLayout(7, 2, 10, 10));
		questionInfoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin câu hỏi"));

		// Thứ tự câu hỏi
		questionInfoPanel.add(new JLabel("Thứ tự câu hỏi:"));
		currentQuestionLabel = new JLabel("Câu 1", SwingConstants.CENTER); // Căn giữa
		currentQuestionLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		questionInfoPanel.add(currentQuestionLabel);

		// Nội dung câu hỏi (JTextArea với JScrollPane)
		questionInfoPanel.add(new JLabel("Câu hỏi:"));
		questionTextArea = new JTextArea(3, 20);
		questionTextArea.setText("Nhập nội dung câu hỏi tại đây...");
		questionTextArea.setForeground(Color.GRAY);
		questionTextArea.addFocusListener(new java.awt.event.FocusAdapter() {
			@Override
			public void focusGained(java.awt.event.FocusEvent e) {
				if (questionTextArea.getText().equals("Nhập nội dung câu hỏi tại đây...")) {
					questionTextArea.setText("");
					questionTextArea.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				if (questionTextArea.getText().isEmpty()) {
					questionTextArea.setText("Nhập nội dung câu hỏi tại đây...");
					questionTextArea.setForeground(Color.GRAY);
				}
			}
		});
		JScrollPane questionScrollPane = new JScrollPane(questionTextArea);
		questionInfoPanel.add(questionScrollPane);

		// Đáp án A-D
		answerFields = new JTextField[4];
		correctAnswerGroup = new ButtonGroup();
		for (int i = 0; i < 4; i++) {
			questionInfoPanel.add(new JLabel("Đáp án " + (char) ('A' + i) + ":"));
			JPanel answerPanel = new JPanel(new BorderLayout());
			answerFields[i] = new JTextField();
			answerPanel.add(answerFields[i], BorderLayout.CENTER);

			JRadioButton correctButton = new JRadioButton("Đúng");
			correctButton.setActionCommand(String.valueOf(i));
			correctAnswerGroup.add(correctButton);
			answerPanel.add(correctButton, BorderLayout.EAST);

			questionInfoPanel.add(answerPanel);
		}

		add(questionInfoPanel, BorderLayout.CENTER);

		// Panel chứa các nút
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
		JButton addButton = new JButton("Thêm câu hỏi");
		addButton.addActionListener(new AddQuestionListener());
		JButton viewEditButton = new JButton("Xem / Sửa câu hỏi");
		viewEditButton.addActionListener(new ViewEditQuestionsListener());
		JButton exportButton = new JButton("Xuất File"); // Nút Xuất File
		exportButton.addActionListener(new ExportQuestionsListener());
		JButton saveButton = new JButton("Lưu tất cả");
		saveButton.addActionListener(new SaveAllQuestionsListener());
		JButton cancelButton = new JButton("Hủy");
		cancelButton.addActionListener(e -> dispose());

		buttonPanel.add(addButton);
		buttonPanel.add(viewEditButton);
		buttonPanel.add(exportButton); // Thêm nút Xuất File
		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void updateSubCategories() {
		String selectedMainCategory = (String) mainCategoryComboBox.getSelectedItem();
		subCategoryComboBox.removeAllItems();

		if ("Khoa học Tự nhiên".equals(selectedMainCategory)) {
			subCategoryComboBox.addItem("Toán");
			subCategoryComboBox.addItem("Vật lý");
			subCategoryComboBox.addItem("Hóa học");
			subCategoryComboBox.addItem("Công nghệ");
			subCategoryComboBox.addItem("Y học");
		} else if ("Khoa học Xã hội".equals(selectedMainCategory)) {
			subCategoryComboBox.addItem("Ngữ văn");
			subCategoryComboBox.addItem("Lịch sử");
			subCategoryComboBox.addItem("Địa lý");
			subCategoryComboBox.addItem("GDCD");
		}
	}

	private class AddQuestionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			saveTemporaryQuestion();
		}
	}

	private class ViewEditQuestionsListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			showQuestionsTable();
		}
	}

	private class SaveAllQuestionsListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			questionList.addAll(temporaryQuestions);
			JOptionPane.showMessageDialog(CreateQuestionFrame.this, "Tất cả câu hỏi đã được lưu!", "Thành công",
					JOptionPane.INFORMATION_MESSAGE);
			dispose();
		}
	}

	private class ExportQuestionsListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (temporaryQuestions.isEmpty()) {
				JOptionPane.showMessageDialog(CreateQuestionFrame.this, "Không có câu hỏi nào để xuất!", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Chọn nơi lưu file");
			int userSelection = fileChooser.showSaveDialog(CreateQuestionFrame.this);
			if (userSelection == JFileChooser.APPROVE_OPTION) {
				File fileToSave = fileChooser.getSelectedFile();
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
					for (int i = 0; i < temporaryQuestions.size(); i++) {
						Question question = temporaryQuestions.get(i);
						writer.write("Câu " + (i + 1) + ": " + question.getContent());
						writer.newLine();
						String[] options = question.getOptions();
						for (int j = 0; j < options.length; j++) {
							writer.write((char) ('A' + j) + ". " + options[j]);
							writer.newLine();
						}
						writer.write("Đáp án đúng: " + (char) ('A' + question.getCorrectAnswerIndex()));
						writer.newLine();
						writer.newLine();
					}
					JOptionPane.showMessageDialog(CreateQuestionFrame.this, "Xuất file thành công!", "Thành công",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(CreateQuestionFrame.this, "Lỗi khi ghi file!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	private void saveTemporaryQuestion() {
		String questionText = questionTextArea.getText().trim();
		if (questionText.isEmpty() || questionText.equals("Nhập nội dung câu hỏi tại đây...")) {
			JOptionPane.showMessageDialog(this, "Câu hỏi không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return;
		}

		String[] answers = new String[4];
		for (int i = 0; i < 4; i++) {
			answers[i] = answerFields[i].getText().trim();
		}

		if (correctAnswerGroup.getSelection() == null) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn đáp án đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return;
		}

		int correctAnswerIndex = Integer.parseInt(correctAnswerGroup.getSelection().getActionCommand());

		Question newQuestion = new Question(questionText, answers, correctAnswerIndex);

		if (currentQuestionIndex <= temporaryQuestions.size()) {
			temporaryQuestions.set(currentQuestionIndex - 1, newQuestion);
		} else {
			temporaryQuestions.add(newQuestion);
		}

		currentQuestionIndex++;
		currentQuestionLabel.setText("Câu " + currentQuestionIndex);
		clearQuestionFields();
	}

	private void clearQuestionFields() {
		questionTextArea.setText("Nhập nội dung câu hỏi tại đây...");
		questionTextArea.setForeground(Color.GRAY);
		for (JTextField answerField : answerFields) {
			answerField.setText("");
		}
		correctAnswerGroup.clearSelection();
	}

	private void showQuestionsTable() {
		if (temporaryQuestions.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Hiện chưa có câu hỏi nào!", "Thông báo",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		JFrame tableFrame = new JFrame("Danh sách câu hỏi");
		tableFrame.setSize(800, 400);
		tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		tableFrame.setLocationRelativeTo(null);

		String[] columnNames = { "STT", "Câu hỏi", "Đáp án A", "Đáp án B", "Đáp án C", "Đáp án D", "Đáp án đúng" };
		DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
		JTable table = new JTable(tableModel);

		for (int i = 0; i < temporaryQuestions.size(); i++) {
			Question question = temporaryQuestions.get(i);
			if (question != null) {
				String[] rowData = new String[7];
				rowData[0] = String.valueOf(i + 1);
				rowData[1] = question.getContent();
				String[] options = question.getOptions();
				for (int j = 0; j < options.length; j++) {
					rowData[j + 2] = options[j];
				}
				rowData[6] = String.valueOf((char) ('A' + question.getCorrectAnswerIndex()));
				tableModel.addRow(rowData);
			}
		}

		JScrollPane scrollPane = new JScrollPane(table);
		tableFrame.add(scrollPane, BorderLayout.CENTER);
		tableFrame.setVisible(true);
	}
}