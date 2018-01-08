package main;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileUploadController {
    private final CoursesService coursesService;
    private Workbook wb;

    @Autowired
    public FileUploadController(CoursesService coursesService) {
        this.coursesService = coursesService;
    }

    @GetMapping("/")
    public String getUploadFileForm() {
        return "uploadForm";
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,  RedirectAttributes redirectAttributes) {
        try {
            if(file.getOriginalFilename().endsWith(".xls")) {
                wb = new HSSFWorkbook(file.getInputStream());
            } else if (file.getOriginalFilename().endsWith(".xlsx")) {
                wb = new XSSFWorkbook(file.getInputStream());
            } else {
                redirectAttributes.addFlashAttribute("message", "Файл должен иметь расширение XLS или XLSX");
                return "redirect:/";
            }
            return "redirect:sheet-selection";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Произошла ошибка при загрузке файла");
            return "redirect:/";
        }
    }

    @GetMapping("/sheet-selection")
    public String sheetSelectionPage(Model model) {
        if(wb == null) {
            return "redirect:/";
        }
        String[] sheetNames = new String[wb.getNumberOfSheets()];
        for(int i = 0; i < sheetNames.length; i++) {
            sheetNames[i] = wb.getSheetName(i);
        }
        model.addAttribute("sheets", sheetNames);
        return "selectSheet";
    }
    @PostMapping("/sheet-selection")
    public String handleSheetSelection(@RequestParam("free-courses-selection") int freeSheetIndex, @RequestParam("paid-courses-selection") int paidSheetIndex, RedirectAttributes redirectAttributes)
    {
        if(freeSheetIndex == paidSheetIndex) {
            redirectAttributes.addFlashAttribute("message", "Листы для платных и бесплатных кружков должны различаться");
            return "redirect:/sheet-selection";
        }
        Sheet freeCoursesSheet = wb.getSheetAt(freeSheetIndex);
        Sheet paidCoursesSheet = wb.getSheetAt(paidSheetIndex);
        coursesService.UpdateCourses(freeCoursesSheet, paidCoursesSheet);
        redirectAttributes.addFlashAttribute("message",
                "Вы успешно обновили список кружков");
        return "redirect:/";
    }
}
