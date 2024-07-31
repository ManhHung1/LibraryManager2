package org.example.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.interfaces.ExcelService;
import org.example.model.User;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

    private static final Logger logger = LoggerFactory.getLogger(ExcelServiceImpl.class);

    /**
     * Exports a list of user data to an Excel file.
     * This method creates an Excel workbook with a sheet named "users". It populates the sheet with user data
     * from the provided list of User. The exported data includes the user's ID, username,
     * telephone, and email, but does not include passwords.
     * @throws IOException if an I/O error occurs during file reading
     */
    @Override
    public void exportUsers(List<User> listUsers, String filePath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("users");

            // Create header row
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);

            createCell(headerRow, 0, "ID", headerStyle);
            createCell(headerRow, 1, "Username", headerStyle);
            createCell(headerRow, 2, "Telephone", headerStyle);
            createCell(headerRow, 3, "Email", headerStyle);

            // Create data rows
            CellStyle cellStyle = workbook.createCellStyle();
            Font cellFont = workbook.createFont();
            cellFont.setFontHeightInPoints((short) 12);
            cellStyle.setFont(cellFont);
            int rowCount = 1;

            System.out.println("Exporting Users:");
            for (User user : listUsers) {
                System.out.println(user);
            }
            for (User user : listUsers) {
                Row row = sheet.createRow(rowCount++);
                int columnCount = 0;
                createCell(row, columnCount++, user.getId(), cellStyle);
                createCell(row, columnCount++, user.getUsername(), cellStyle);
                createCell(row, columnCount++, user.getTelephone(), cellStyle);
                createCell(row, columnCount++, user.getEmail(), cellStyle);
            }

            // Write to file
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
            }
        }
    }

    /**
     * Reads user data from the Excel file and converts it into a list of User objects.
     * Passwords are not imported.
     *
     * @param filePath the path to the Excel file
     * @return a list of User objects without passwords
     * @throws IOException if an I/O error occurs during file reading
     */
    @Override
    public List<User> importUsers(String filePath) throws IOException {
        List<User> listUsers = new ArrayList<>();
        try (InputStream inputStream = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            int rowCount = 1;
            for (Row row : sheet) {
                if (row.getRowNum() < rowCount) continue;

                try {
                    int id = (int) row.getCell(0).getNumericCellValue();
                    String username = row.getCell(1).getStringCellValue();
                    String telephone = row.getCell(2).getStringCellValue();
                    String email = row.getCell(3).getStringCellValue();

                    User user = new User(id, username, telephone, email);
                    listUsers.add(user);
                } catch (Exception e) {
                    logger.error("Error processing row {}: {}", row.getRowNum(), e.getMessage(), e);
                }
            }

            workbook.close();
        }
        return listUsers;
    }

    private void createCell(Row row, int col, Object value, CellStyle style) {
        Cell cell = row.createCell(col);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
}
