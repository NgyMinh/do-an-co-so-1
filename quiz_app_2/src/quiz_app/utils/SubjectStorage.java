package quiz_app.utils; 

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectStorage {

    private static final String FILE_NAME = "subjects.txt";

    // Lưu danh sách lĩnh vực vào tệp
    public static void saveSubjects(List<String> subjects) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String subject : subjects) {
                writer.write(subject);
                writer.newLine(); // Xuống dòng
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu danh sách lĩnh vực: " + e.getMessage());
        }
    }

    // Tải danh sách lĩnh vực từ tệp
    public static List<String> loadSubjects() {
        List<String> subjects = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                subjects.add(line.trim());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Tệp danh sách lĩnh vực không tồn tại. Tạo tệp mới...");
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc danh sách lĩnh vực: " + e.getMessage());
        }
        return subjects;
    }
}