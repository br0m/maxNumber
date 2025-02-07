package com.example.max_number;

import com.example.max_number.service.MaxNumberService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MaxNumberApplicationTests {
    private static final String FILE_PATH = "test.xlsx";
    private static final int NUMBERS_COUNT = 10_000;
    private static final long MAX_EXCEL_VALUE = (long) Math.pow(2, 53);

    @Autowired
    private MaxNumberService maxNumberService;

    @RepeatedTest(10)
    void getMaxNumberFromXLSXFileByIndexTest() throws IOException {
        var random = new Random();
        var testNumbers = random.longs(NUMBERS_COUNT, -MAX_EXCEL_VALUE, MAX_EXCEL_VALUE)
            .boxed()
            .toList();
        var sortedTestNumbers = new ArrayList<>(testNumbers);
        sortedTestNumbers.sort(Collections.reverseOrder());

        try (var outputStream = new FileOutputStream(FILE_PATH); var workbook = new XSSFWorkbook()) {
            var sheet = workbook.createSheet();
            for (int i = 0; i < NUMBERS_COUNT; i++) {
                var row = sheet.createRow(i);
                var cell = row.createCell(0);
                cell.setCellValue(testNumbers.get(i));
            }
            workbook.write(outputStream);
        }

        var index = random.nextInt(1, NUMBERS_COUNT + 1);
        var expected = sortedTestNumbers.get(index - 1);
        var actual = maxNumberService.getMaxNumberFromXLSXFileByIndex(FILE_PATH, index);
        Files.delete(Paths.get(FILE_PATH));

        assertThat(actual).isEqualTo(expected);
    }
}
