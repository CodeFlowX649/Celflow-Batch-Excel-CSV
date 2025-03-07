package com.celflow.FileConvert;

import com.celflow.Utils;
import com.opencsv.CSVReader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class csv_to_execl {
    private static final int MAX_ROWS_PER_SHEET = 1048576; // Excel单表最大行数

    private static String outputFolderPath = new Utils().getDeskPath() + "\\csv_to_execl";
    private static String outputFilePath = outputFolderPath + "\\csv_to_execl.xlsx";

    public void csv_to_execl_main() {
        List<String> inputFilePaths = new Utils().selectCsvFiles(false);
        System.out.println("稍等片刻 正在运行。。。。。");

        // 创建输出文件夹
        createOutputFolder();

        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(inputFilePaths.get(0)), StandardCharsets.UTF_8))) {

            String[] nextLine; // 定义 nextLine 变量
            int rowNum = 0;
            int fileIndex = 1;
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sheet1");

            // 读取CSV文件并写入Excel
            while ((nextLine = reader.readNext()) != null) {
                System.out.println("已转换"+rowNum+"行数据");
                if (rowNum >= MAX_ROWS_PER_SHEET) {
                    // 如果当前行数超过最大限制，保存当前文件并创建新的Excel文件
                    saveWorkbook(workbook, outputFilePath.replace(".xlsx", "_" + fileIndex + ".xlsx"));
                    workbook.close();
                    workbook = new XSSFWorkbook();
                    sheet = workbook.createSheet("Sheet1");
                    rowNum = 0;
                    fileIndex++;
                }

                Row row = sheet.createRow(rowNum++);
                for (int i = 0; i < nextLine.length; i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(nextLine[i]);
                }
            }

            // 保存最后一个Excel文件
            saveWorkbook(workbook, outputFilePath.replace(".xlsx", "_" + fileIndex + ".xlsx"));
            workbook.close();

            System.out.println();
            System.out.println("======================CSV 转 EXECL完成==============================");
            System.out.println("结果已保存到:" + outputFolderPath + " |(系统桌面 csv_to_execl文件夹)");
            System.out.println("共生成 " + fileIndex + " 个Excel文件。");
            System.out.println("提示：由于Excel单个工作表最多支持 " + MAX_ROWS_PER_SHEET + " 行数据，");
            System.out.println("如果CSV文件中的数据量超过此限制，程序会自动拆分并生成多个Excel文件。");
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveWorkbook(Workbook workbook, String filePath) throws Exception {
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
    }

    private static void createOutputFolder() {
        try {
            Path path = Paths.get(outputFolderPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}