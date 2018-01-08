package main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class CoursesRESTController {
    private CoursesService coursesService;

    @Autowired
    public CoursesRESTController(CoursesService coursesService) {
        this.coursesService = coursesService;
    }

    @RequestMapping("/courses")
    public ResponseEntity<Object> GetCourses() {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("free", coursesService.getFreeCourses());
        res.put("paid", coursesService.getPaidCourses());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
