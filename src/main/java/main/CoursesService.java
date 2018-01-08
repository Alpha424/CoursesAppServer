package main;

import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CoursesService {
    private List<Map<String, Object>> freeCourses;
    private List<Map<String, Object>> paidCourses;
    public void UpdateCourses(Sheet freeCoursesSheet, Sheet paidCoursesSheet) {
        this.freeCourses = ExcelFileParser.ParseSheet(freeCoursesSheet);
        this.paidCourses = ExcelFileParser.ParseSheet(paidCoursesSheet);
    }

    public List<Map<String, Object>> getFreeCourses() {
        return freeCourses;
    }

    public List<Map<String, Object>> getPaidCourses() {
        return paidCourses;
    }
}
