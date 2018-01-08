package main;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;

public class ExcelFileParser {
    public static List<Map<String, Object>> ParseSheet(Sheet sheet) {
        String[] variables = GetVariables(sheet);
        return ExtractData(sheet, variables);
    }
    private static List<Map<String, Object>> ExtractData(Sheet sheet, String[] variables) {
        List<Map<String, Object>> res = new LinkedList<>();
        int rows = sheet.getPhysicalNumberOfRows();
        for(int i = 0; i < rows; i++) {
            Row row = sheet.getRow(i);
            if(row == null) {
                continue;
            }
            int nonEmptyCells = CountNonEmptyCells(row);
            if(nonEmptyCells == variables.length) {
                Map<String, Object> entry = new LinkedHashMap<>();
                for(int k = 0; k < nonEmptyCells; k++) {
                    switch (row.getCell(k).getCellType()) {
                        case Cell.CELL_TYPE_NUMERIC: {
                            entry.put(variables[k], row.getCell(k).getNumericCellValue());
                            break;
                        }
                        case Cell.CELL_TYPE_BOOLEAN: {
                            entry.put(variables[k], row.getCell(k).getBooleanCellValue());
                            break;
                        }
                        case Cell.CELL_TYPE_STRING: {
                            entry.put(variables[k], row.getCell(k).getStringCellValue());
                            break;
                        }
                        default: {
                            entry.put(variables[k], null);
                        }
                    }

                }
                res.add(entry);
            }
        }
        //remove title row
        res.remove(0);
        return res;
    }
    private static String[] GetVariables(Sheet sheet) {
        String[] variables = null;
        int rows = sheet.getPhysicalNumberOfRows();
        for(int i = 0; i < rows; i++) {
            Row row = sheet.getRow(i);
            if(row == null) {
                continue;
            }
            int nonEmptyCells = CountNonEmptyCells(row);
            if(nonEmptyCells > 2) {
                variables = new String[nonEmptyCells];
                for(int k = 0; k < nonEmptyCells; k++) {
                    variables[k] = row.getCell(k).getStringCellValue();
                }
                break;
            }
        }
        return variables;
    }
    private static int CountNonEmptyCells(Row row) {
        if(row == null) {
            return -1;
        }
        int res = 0;
        int cellcount = row.getPhysicalNumberOfCells();
        for (int i = 0; i < cellcount; i++) {
            if(row.getCell(i) != null && (
                    row.getCell(i).getCellType() == Cell.CELL_TYPE_STRING
                    || row.getCell(i).getCellType() == Cell.CELL_TYPE_NUMERIC
                    || row.getCell(i).getCellType() == Cell.CELL_TYPE_BOOLEAN
            )) {
                res++;
            } else {
                break;
            }
        }
        return res;
    }
}
