package com.example.max_number.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class WorkbookUtils {

    public static int getFirstColumnIndexWithData(Sheet sheet) {
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() != CellType.BLANK) {
                    return cell.getColumnIndex();
                }
            }
        }
        return -1;
    }
}
