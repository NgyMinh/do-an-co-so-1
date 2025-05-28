package main;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.Timer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import model.Question;
import utils.QuestionLoader;

public class QuizFrame extends JFrame {
	private List<Question> questions;
	private List<JButton> questionButtons;

	private int currentQuestionIndex = 0;
	private int score = 0;
	private Timer countdownTimer; // <-- sửa lại kiểu cho đúng
	private int timeRemaining; // Thời gian còn lại (tính bằng giây)
	private JLabel timerLabel;


	private JPanel answerPanel;
	private JLabel questionLabel;
	private JRadioButton[] answerButtons;
	private ButtonGroup answerGroup;
	private JButton backButton; // Nút "Trở về"
	private JPanel questionTitlePanel;
	private JButton nextButton;
	private JButton prevButton;
	private JButton submitQuizButton;


	public QuizFrame(String subject) {
		setTitle("Quiz - " + subject);
		setSize(1000, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		
		// Lấy thời gian làm bài từ CreateQuestionFrame (tính bằng phút) và chuyển sang giây
		 timeRemaining = CreateQuestionFrame.getExamDuration() > 0 
		            ? CreateQuestionFrame.getExamDuration() * 60 
		            : 10 * 60; // Mặc định là 10 phút

		// Tiêu đề lớn cho quiz
		JLabel quizTitle = new JLabel("Quiz lĩnh vực " + subject, SwingConstants.CENTER);
		quizTitle.setFont(new Font("Arial", Font.BOLD, 24));
		quizTitle.setBorder(new EmptyBorder(15, 10, 10, 10)); // Padding
		add(quizTitle, BorderLayout.PAGE_START);  // PAGE_START = trên cùng


		// Tải câu hỏi từ cơ sở dữ liệu
		questions = QuestionLoader.loadQuestionsFromDatabase(subject);

		if (questions == null || questions.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Không có câu hỏi nào cho chủ đề này!", "Error",
					JOptionPane.ERROR_MESSAGE);
			dispose();
			return;
		}

		// Panel chứa các nút số câu hỏi (hiển thị theo dạng lưới)
		JPanel questionListPanel = new JPanel();
		questionListPanel.setLayout(new GridLayout(0, 5, 10, 10)); // Hiển thị dạng lưới với 5 cột và khoảng cách giữa
																	// các nút
		questionListPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Tạo padding cho Panel

		// Tạo nút cho từng câu hỏi
		questionButtons = new java.util.ArrayList<>();

		for (int i = 0; i < questions.size(); i++) {
		    JButton questionButton = new JButton("Câu " + (i + 1));
		    questionButtons.add(questionButton);

			questionButton.setFont(new Font("Arial", Font.PLAIN, 14));
			int questionIndex = i; // Lưu chỉ số câu hỏi hiện tại
			questionButton.addActionListener(e -> {
				currentQuestionIndex = questionIndex;
				displayQuestion(); // Hiển thị câu hỏi tương ứng
				
				 
				
			});
			questionListPanel.add(questionButton);
			 
		}

		// Thêm Panel chứa danh sách câu hỏi vào JScrollPane để hỗ trợ cuộn
		JScrollPane scrollPane = new JScrollPane(questionListPanel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Chỉ cuộn theo chiều dọc
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Hiển thị thanh cuộn dọc nếu
																							// cần
		
		
		// Đặt chiều cao cố định cho JScrollPane bằng cách gói trong một JPanel
		JPanel scrollContainer = new JPanel(new BorderLayout());
		scrollContainer.setPreferredSize(new Dimension(1300, 150)); // Đặt chiều cao cố định cho khu vực danh sách câu
																	// hỏi
		scrollContainer.add(scrollPane, BorderLayout.CENTER);
		
		// Label hiển thị thời gian
		timerLabel = new JLabel("Thời gian còn lại: " + formatTime(timeRemaining), SwingConstants.RIGHT);
		timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
		timerLabel.setBorder(new EmptyBorder(10, 20, 10, 20));

		// Tạo topPanel chứa tiêu đề, danh sách câu hỏi và timer
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(quizTitle, BorderLayout.NORTH);
		topPanel.add(scrollContainer, BorderLayout.CENTER);
		topPanel.add(timerLabel, BorderLayout.EAST);

		add(topPanel, BorderLayout.NORTH);

		// Panel chính chứa tiêu đề câu hỏi và các đáp án
		JPanel questionPanel = new JPanel(new BorderLayout());
		questionPanel.setBorder(new EmptyBorder(5, 30, 20, 30));

		// Tiêu đề câu hỏi
		questionTitlePanel = new JPanel(new BorderLayout());
		questionTitlePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Câu hỏi 1")); // mặc định ban đầu
		questionLabel = new JLabel("Câu hỏi sẽ hiển thị ở đây", SwingConstants.CENTER);
		questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
		questionTitlePanel.add(questionLabel, BorderLayout.CENTER);
		questionPanel.add(questionTitlePanel, BorderLayout.NORTH);

		// Tạo panel chứa các đáp án (có khung viền rõ ràng)
		JPanel answerGroupPanel = new JPanel(new java.awt.GridBagLayout());
		answerGroupPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Chọn một đáp án:"));


		answerButtons = new JRadioButton[4];
		answerGroup = new ButtonGroup();

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new java.awt.Insets(10, 20, 10, 20); // padding giữa các nút

		for (int i = 0; i < 4; i++) {
		    answerButtons[i] = new JRadioButton();
		    answerButtons[i].setFont(new Font("Arial", Font.PLAIN, 14));
		    answerGroup.add(answerButtons[i]);

		    gbc.gridx = i % 2; // 0 hoặc 1 (cột)
		    gbc.gridy = i / 2; // 0 hoặc 1 (dòng)

		    answerGroupPanel.add(answerButtons[i], gbc);
		}


		questionPanel.add(answerGroupPanel, BorderLayout.CENTER);
		add(questionPanel, BorderLayout.CENTER); // Thêm toàn bộ panel vào giao diện chính

		// Panel chứa nút "Chọn đáp án" và "Trở về"
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(new EmptyBorder(0, 10, 20, 10));
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

		backButton = new JButton("Trở về");
		backButton.setBackground(java.awt.Color.decode("#f44336")); // Đỏ
		backButton.setForeground(java.awt.Color.WHITE);
		backButton.addActionListener(new BackButtonListener());

		
		prevButton = new JButton("Câu trước");
		prevButton.setBackground(java.awt.Color.decode("#2196F3")); // Xanh dương
		prevButton.setForeground(java.awt.Color.WHITE);
		prevButton.addActionListener(new PrevButtonListener());

		nextButton = new JButton("Câu tiếp theo");
		nextButton.setBackground(java.awt.Color.decode("#2196F3")); // Xanh dương
		nextButton.setForeground(java.awt.Color.WHITE);
		nextButton.addActionListener(new NextButtonListener());

		submitQuizButton = new JButton("Nộp bài");
		submitQuizButton.setBackground(java.awt.Color.decode("#FF9800")); // Cam
		submitQuizButton.setForeground(java.awt.Color.WHITE);
		submitQuizButton.addActionListener(new SubmitQuizListener());


		// Kích thước các nút
		Dimension size = new Dimension(150, 30);
		backButton.setMinimumSize(size);
		backButton.setPreferredSize(size);
		backButton.setMaximumSize(size);
		
		prevButton.setMinimumSize(size);
		prevButton.setPreferredSize(size);
		prevButton.setMaximumSize(size);

		nextButton.setMinimumSize(size);
		nextButton.setPreferredSize(size);
		nextButton.setMaximumSize(size);

		submitQuizButton.setMinimumSize(size);
		submitQuizButton.setPreferredSize(size);
		submitQuizButton.setMaximumSize(size);


		// Căn giữa các button vào panel
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(backButton);
		buttonPanel.add(Box.createHorizontalStrut(10));
		buttonPanel.add(prevButton);
		buttonPanel.add(Box.createHorizontalStrut(10));
		buttonPanel.add(nextButton);
		buttonPanel.add(Box.createHorizontalStrut(10));
		
		buttonPanel.add(Box.createHorizontalStrut(10));
		buttonPanel.add(submitQuizButton);
		buttonPanel.add(Box.createHorizontalGlue());


		add(buttonPanel, BorderLayout.SOUTH);
		displayQuestion(); // Hiển thị câu hỏi đầu tiên
		startCountdownTimer();
		
	}

	private void displayQuestion() {
		if (currentQuestionIndex < questions.size() && currentQuestionIndex >= 0) {
			Question question = questions.get(currentQuestionIndex);
			((javax.swing.border.TitledBorder) questionTitlePanel.getBorder())
					.setTitle("Câu hỏi " + (currentQuestionIndex + 1));
			questionLabel.setText(question.getContent());
			questionTitlePanel.repaint(); // Cập nhật lại giao diện viền

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
		// Highlight câu đang làm
		for (int i = 0; i < questionButtons.size(); i++) {
		    JButton btn = questionButtons.get(i);
		    if (i == currentQuestionIndex) {
		    	btn.setBackground(java.awt.Color.decode("#FFEB3B")); // Màu vàng nhạt đẹp hơn
		    } else {
		        btn.setBackground(null); // reset về mặc định
		    }
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
	// Lớp xử lý sự kiện cho nút "Trở về"
	private class BackButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Đóng cửa sổ QuizFrame hiện tại
			dispose();

			// Quay trở lại giao diện trước đó (ví dụ: MainMenuFrame)
			new MenuFrame().setVisible(true); // Thay MainMenuFrame bằng lớp giao diện chính của bạn
		}
	}
	
	private class NextButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (currentQuestionIndex < questions.size() - 1) {
				currentQuestionIndex++;
				displayQuestion();
			}
		}
	}

	private class PrevButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (currentQuestionIndex > 0) {
				currentQuestionIndex--;
				displayQuestion();
			}
		}
	}

	private class SubmitQuizListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int confirm = JOptionPane.showConfirmDialog(
				QuizFrame.this,
				"Bạn có chắc chắn muốn nộp bài không?",
				"Xác nhận",
				JOptionPane.YES_NO_OPTION
			);
			if (confirm == JOptionPane.YES_OPTION) {
			    if (countdownTimer != null) countdownTimer.stop();
			    JOptionPane.showMessageDialog(QuizFrame.this, "Bạn đã hoàn thành bài kiểm tra! Điểm: " + score);
			    dispose();
			    new ResultFrame(score, questions.size()).setVisible(true);
			}

		}
	}
    private String formatTime(int seconds) {
        int mins = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }

    private void startCountdownTimer() {
        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--; // Giảm 1 giây mỗi lần
                timerLabel.setText("Thời gian còn lại: " + formatTime(timeRemaining)); // Cập nhật giao diện

                if (timeRemaining <= 0) {
                    countdownTimer.stop(); // Dừng timer khi hết giờ
                    JOptionPane.showMessageDialog(QuizFrame.this, "Hết thời gian làm bài! Bài sẽ được nộp tự động.");
                    dispose(); // Đóng cửa sổ hiện tại
                    new ResultFrame(score, questions.size()).setVisible(true); // Hiển thị kết quả
                }
            }
        });
        countdownTimer.start(); // Bắt đầu timer
    }




}
