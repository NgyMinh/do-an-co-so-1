//  1. Giao diện chính (Menu chọn chủ đề)
// Tạo JFrame chính với các nút chọn chủ đề trắc nghiệm (ví dụ: Toán, Lịch sử, Công nghệ)
// Khi bấm nút, mở JFrame hiển thị câu hỏi tương ứng

// 2. Giao diện hiển thị câu hỏi
// Tạo JPanel hiển thị câu hỏi trắc nghiệm gồm:
// - Nội dung câu hỏi
// - 4 đáp án A, B, C, D (RadioButton)
// - Nút "Nộp bài" để kiểm tra đáp án

//  3. Màn hình kết quả
// Tạo JFrame hiển thị điểm số sau khi làm bài
// Hiển thị số câu đúng / tổng số câu
// Có thể thêm nút "Quay lại menu chính"

// 4. Class quản lý câu hỏi

// Tạo class Question gồm:
// - String content (nội dung câu hỏi)
// - String[] options (4 đáp án)
// - int correctAnswerIndex (vị trí đáp án đúng)

//  5. Đọc dữ liệu câu hỏi từ file CSV
// Viết hàm đọc danh sách câu hỏi từ file CSV có định dạng:
// ID,Question,OptionA,OptionB,OptionC,OptionD,CorrectIndex
// Trả về danh sách đối tượng Question

//  6. Kết nối cơ sở dữ liệu (nếu dùng SQL Server)
// Kết nối đến SQL Server và lấy danh sách câu hỏi từ bảng 'Questions'
// Cột gồm: id, question, optionA, optionB, optionC, optionD, correctAnswer

//  7. Tính điểm và lưu kết quả
// Viết hàm chấm điểm bài làm: duyệt qua danh sách câu hỏi và câu trả lời người dùng
// Đếm số câu đúng và tính điểm tổng
// Lưu kết quả vào file hoặc database (bảng 'Results')

//  8. Bộ đếm thời gian (nếu có)
// Thêm bộ đếm thời gian đếm ngược 10 phút cho mỗi bài trắc nghiệm
// Nếu hết giờ, tự động nộp bài và tính điểm

// // Gợi ý cấu trúc thư mục dự án 
// quiz-app
// ├── Main.java
// ├── MenuFrame.java
// ├── QuizFrame.java
// ├── ResultFrame.java
// ├── Question.java
// ├── QuestionLoader.java
// └── DatabaseHelper.java (nếu dùng DB)


