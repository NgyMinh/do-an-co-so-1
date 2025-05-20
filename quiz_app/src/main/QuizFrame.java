package main;

import model.Question;

import javax.swing.border.EmptyBorder;
import utils.QuestionLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class QuizFrame extends JFrame {
	private List<Question> questions;
	private int currentQuestionIndex = 0;
	private int score = 0;

	private JLabel questionLabel;
	private JRadioButton[] answerButtons;
	private ButtonGroup answerGroup;
	private JButton submitButton; // Nút chọn đáp án
	private JButton backButton; // Nút "Trở về"

	public QuizFrame(String subject) {
		setTitle("Quiz - " + subject);
		setSize(600, 350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		// Load câu hỏi
		questions = QuestionLoader.loadQuestionsFromDatabase(subject); // Gọi Questionloader để tải danh sách câu hỏi
																		// tương ứng với subject

		// Tạo Giao diện hiển thị câu hỏi ********
		JPanel questionPanel = new JPanel(new BorderLayout());
		questionPanel.setBorder(new EmptyBorder(20, 30, 20, 30)); // Tạo padding cho Panel;
		questionLabel = new JLabel("Câu hỏi sẽ hiển thị ở đây", SwingConstants.CENTER); // Tiêu đề Câu hỏi
		questionLabel.setFont(new Font("Arial", Font.BOLD, 18)); // set Font, cỡ chữ cho câu hỏi
		questionPanel.add(questionLabel, BorderLayout.NORTH); // Thêm tiêu đề câu hỏi vào trên

		// Layout phần đáp án A, B, C, D
		JPanel answerPanel = new JPanel(new GridLayout(4, 1));
		answerButtons = new JRadioButton[4];
		answerGroup = new ButtonGroup();
		for (int i = 0; i < 4; i++) {
			answerButtons[i] = new JRadioButton();
			answerGroup.add(answerButtons[i]);
			answerPanel.add(answerButtons[i]);
		}
		questionPanel.add(answerPanel, BorderLayout.CENTER);

		add(questionPanel, BorderLayout.CENTER); // Thêm answerPanel vào giữa questionPanel

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

		// Hiển thị câu hỏi đầu tiên
		displayQuestion();
	}

	private void displayQuestion() {
		if (currentQuestionIndex < questions.size() && currentQuestionIndex >= 0) {
			Question question = questions.get(currentQuestionIndex);
			questionLabel.setText("Câu hỏi: " + question.getContent());
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