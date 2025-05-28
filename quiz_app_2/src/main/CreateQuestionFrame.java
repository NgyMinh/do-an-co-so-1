package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import model.Question;
import utils.QuestionLoader;

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

	public static int examDuration;

	static {
	    try {
	        File configFile = new File("config.txt");
	        if (configFile.exists()) {
	            java.util.Scanner scanner = new java.util.Scanner(configFile);
	            if (scanner.hasNextInt()) {
	                examDuration = scanner.nextInt();
	            } else {
	                examDuration = 30; // fallback
	            }
	            scanner.close();
	        } else {
	            examDuration = 30; // fallback nếu chưa có file
	        }
	    } catch (Exception e) {
	        examDuration = 30; // fallback nếu lỗi
	    }
	}


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
		subCategoryComboBox.addActionListener(e -> loadQuestionsForSelectedCategory());
		updateSubCategories(); // Cập nhật lĩnh vực ban đầu
		generalInfoPanel.add(subCategoryComboBox);

		// Thời gian làm bài
		generalInfoPanel.add(new JLabel("Thời gian làm bài (phút):"));
		durationComboBox = new JComboBox<>(new String[] { "5", "15", "30", "45", "60", "90", "120" });
		durationComboBox.setSelectedItem(String.valueOf(examDuration));
		generalInfoPanel.add(durationComboBox);

		add(generalInfoPanel, BorderLayout.NORTH);

		// Panel Thông tin câu hỏi
		JPanel questionInfoPanel = new JPanel(new GridLayout(7, 2, 10, 10));
		questionInfoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin câu hỏi"));

		// Thứ tự câu hỏi
		questionInfoPanel.add(new JLabel("Thứ tự câu hỏi:"));
		currentQuestionLabel = new JLabel("Câu 1", SwingConstants.CENTER);
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
		
		durationComboBox.setSelectedItem(String.valueOf(examDuration));


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

		// Đảm bảo `currentQuestionLabel` đã được khởi tạo trước khi gán sự kiện
		if (currentQuestionLabel != null) {
			subCategoryComboBox.addActionListener(e -> {
				loadQuestionsForSelectedCategory();
			});
		}
	}

	private void loadQuestionsForSelectedCategory() {
		String selectedSubCategory = (String) subCategoryComboBox.getSelectedItem();
		if (selectedSubCategory != null) {
			temporaryQuestions = QuestionLoader.loadQuestionsFromDatabase(selectedSubCategory);

			// Kiểm tra null trước khi gọi setText
			if (currentQuestionLabel != null) {
				currentQuestionIndex = temporaryQuestions.size() + 1;
				currentQuestionLabel.setText("Câu " + currentQuestionIndex);
			}
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

	private class SaveAllQuestionsListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Lấy tên lĩnh vực từ giao diện
			String subCategory = (String) subCategoryComboBox.getSelectedItem();

			if (subCategory == null || subCategory.isEmpty()) {
				JOptionPane.showMessageDialog(CreateQuestionFrame.this, "Vui lòng chọn lĩnh vực cụ thể.", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Lưu tất cả các câu hỏi vào cơ sở dữ liệu
			for (Question question : temporaryQuestions) {
				try {
					QuestionLoader.saveQuestionToDatabase(subCategory, question); // Lưu câu hỏi vào cơ sở dữ liệu
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(CreateQuestionFrame.this, "Lỗi khi lưu câu hỏi: " + ex.getMessage(),
							"Lỗi", JOptionPane.ERROR_MESSAGE);
					return;
				}
				String selectedDuration = (String) durationComboBox.getSelectedItem();
				if (selectedDuration != null) {
				    examDuration = Integer.parseInt(selectedDuration);
				}
				try (BufferedWriter writer = new BufferedWriter(new FileWriter("config.txt"))) {
				    writer.write(String.valueOf(examDuration));
				} catch (IOException ex) {
				    ex.printStackTrace();
				}


			}

			// Thêm lĩnh vực vào MenuFrame
			MenuFrame.addSubject(subCategory); // Thêm lĩnh vực mới vào danh sách


			// Làm mới MenuFrame để hiển thị danh sách cập nhật
			SwingUtilities.invokeLater(() -> {
				MenuFrame menuFrame = new MenuFrame();
				menuFrame.setVisible(true); // Hiển thị MenuFrame đã cập nhật
			});

			// Hiển thị thông báo thành công
			JOptionPane.showMessageDialog(CreateQuestionFrame.this,
					"Tất cả câu hỏi đã được lưu và lĩnh vực đã được thêm vào Menu!", "Thành công",
					JOptionPane.INFORMATION_MESSAGE);
//			Thời gian
			String selectedDuration = (String) durationComboBox.getSelectedItem();
			if (selectedDuration != null) {
			    examDuration = Integer.parseInt(selectedDuration); // Cập nhật thời gian thi vào biến static
			}


			// Đóng giao diện tạo câu hỏi
			dispose();
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

		// Tạo nút xóa câu hỏi
		JButton deleteButton = new JButton("Xóa câu hỏi");
		deleteButton.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow >= 0 && selectedRow < temporaryQuestions.size()) {
				int confirm = JOptionPane.showConfirmDialog(tableFrame, "Bạn có chắc muốn xóa câu hỏi này?", "Xác nhận",
						JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					Question removed = temporaryQuestions.remove(selectedRow);

					// Xóa câu hỏi khỏi database
					try {
						String subCategory = (String) subCategoryComboBox.getSelectedItem();
						QuestionLoader.deleteQuestionFromDatabase(subCategory, removed);
						tableModel.removeRow(selectedRow);
						JOptionPane.showMessageDialog(tableFrame, "Đã xóa câu hỏi thành công!", "Thành công",
								JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(tableFrame,
								"Lỗi khi xóa câu hỏi trong cơ sở dữ liệu: " + ex.getMessage(), "Lỗi",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			} else {
				JOptionPane.showMessageDialog(tableFrame, "Vui lòng chọn một câu hỏi để xóa.", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		// Tạo nút cập nhật câu hỏi
		JButton updateButton = new JButton("Cập nhật câu hỏi");
		updateButton.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow >= 0 && selectedRow < temporaryQuestions.size()) {
				try {
					// Lấy dữ liệu đã chỉnh sửa từ bảng
					String updatedContent = (String) table.getValueAt(selectedRow, 1);
					String[] updatedOptions = new String[4];
					for (int i = 0; i < 4; i++) {
						updatedOptions[i] = (String) table.getValueAt(selectedRow, i + 2);
					}
					String correctAnswer = (String) table.getValueAt(selectedRow, 6);

					// Kiểm tra dữ liệu chỉnh sửa
					if (updatedContent == null || updatedContent.trim().isEmpty()) {
						JOptionPane.showMessageDialog(tableFrame, "Nội dung câu hỏi không được để trống!", "Lỗi",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					for (String option : updatedOptions) {
						if (option == null || option.trim().isEmpty()) {
							JOptionPane.showMessageDialog(tableFrame, "Mỗi đáp án không được để trống!", "Lỗi",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					if (correctAnswer == null || correctAnswer.trim().isEmpty() || correctAnswer.length() != 1
							|| correctAnswer.charAt(0) < 'A' || correctAnswer.charAt(0) > 'D') {
						JOptionPane.showMessageDialog(tableFrame,
								"Đáp án đúng không hợp lệ! Vui lòng nhập một ký tự từ A đến D.", "Lỗi",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					int correctAnswerIndex = correctAnswer.charAt(0) - 'A';

					// Lấy câu hỏi gốc
					// Lấy câu hỏi gốc
					Question originalQuestion = temporaryQuestions.get(selectedRow);
					// update nội dung
					originalQuestion.setContent(updatedContent);
					originalQuestion.setOptions(updatedOptions);
					originalQuestion.setCorrectAnswerIndex(correctAnswerIndex);

					String subCategory = (String) subCategoryComboBox.getSelectedItem();
					QuestionLoader.updateQuestionInDatabase(subCategory, originalQuestion.getId(), originalQuestion);

					// Hiển thị thông báo thành công
					JOptionPane.showMessageDialog(tableFrame, "Cập nhật câu hỏi thành công!", "Thành công",
							JOptionPane.INFORMATION_MESSAGE);

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(tableFrame, "Lỗi khi cập nhật câu hỏi: " + ex.getMessage(), "Lỗi",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(tableFrame, "Vui lòng chọn một câu hỏi để cập nhật.", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bottomPanel.add(deleteButton);
		bottomPanel.add(updateButton); // Thêm nút cập nhật vào giao diện
		tableFrame.add(bottomPanel, BorderLayout.SOUTH);

		tableFrame.setVisible(true);
	}

	private void clearQuestionFields() {
		questionTextArea.setText("Nhập nội dung câu hỏi tại đây...");
		questionTextArea.setForeground(Color.GRAY);
		for (JTextField answerField : answerFields) {
			answerField.setText("");
		}
		correctAnswerGroup.clearSelection();
	}
	// Getter để lấy thời gian làm bài từ các class khác
	public static int getExamDuration() {
	    return examDuration;
	}

}