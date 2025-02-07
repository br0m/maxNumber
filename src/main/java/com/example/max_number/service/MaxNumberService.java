package com.example.max_number.service;

import com.example.max_number.utils.WorkbookUtils;
import jakarta.validation.constraints.Min;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.PriorityQueue;

@Service
public class MaxNumberService {

    /**
     * Возвращает N-максимальное число из файла.
     * Например, если файл содержит значения 9, 1, 5, 7 и требуется найти второе максимальное значение, то это будет 7.
     *
     * @param filePath путь к .xlsx файлу с данными
     * @param index порядковый номер максимального числа
     * @return искомое число
     */
    public Long getMaxNumberFromXLSXFileByIndex(String filePath, @Min(1) int index) {
        var queue = new PriorityQueue<Long>(index);

        try (var buffer = new BufferedInputStream(new FileInputStream(filePath))) {
            var workbook = new XSSFWorkbook(buffer);
            var sheet = workbook.getSheetAt(0);
            var columnIndex = WorkbookUtils.getFirstColumnIndexWithData(sheet);
            if (columnIndex == -1) {
                throw new IllegalArgumentException("В файле %s нет данных (пустой)".formatted(filePath));
            }
            for (Row row : sheet) {
                var cell = row.getCell(columnIndex);
                var value = (long) cell.getNumericCellValue();
                if (queue.size() == index) {
                    if (queue.peek() < value) {
                        queue.poll();
                        queue.add(value);
                    }
                } else {
                    queue.add(value);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (queue.size() < index) {
            throw new IllegalArgumentException("В файле %s менее %d чисел".formatted(filePath, index));
        }

        return queue.peek();
    }
}
