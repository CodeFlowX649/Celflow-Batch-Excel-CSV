package com.celflow.FileConvert;

import com.celflow.Utils;
import com.opencsv.CSVWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import java.util.List;

public class excel_to_csv {
    private static String outputFilePath = new Utils().getDeskPath() + "\\excel_to_csv.csv";

    public void excel_to_csv_main() {
        List<String> inputFilePaths = new Utils().selectExcelFiles(true);
        System.out.println("稍等片刻 正在运行。。。。。");

        // 提示用户程序的功能
        System.out.println("提示：程序可以读取选中的多个Excel文件，并将它们合并为一个CSV文件。");
        System.out.println();

        // 创建输出文件夹
//        createOutputFolder();

        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath), StandardCharsets.UTF_8))) {
            boolean isHeaderWritten = false; // 标记是否已写入表头

            for (String inputFilePath : inputFilePaths) {
                System.out.println("正在处理文件: " + inputFilePath);

                try (FileInputStream fileInputStream = new FileInputStream(inputFilePath);
                     Workbook workbook = new XSSFWorkbook(fileInputStream)) {

                    for (Sheet sheet : workbook) {
                        for (Row row : sheet) {
                            String[] rowData = new String[row.getLastCellNum()];

                            // 如果是第一行且表头未写入，则写入表头
                            if (row.getRowNum() == 0 && !isHeaderWritten) {
                                for (Cell cell : row) {
                                    rowData[cell.getColumnIndex()] = cell.toString();
                                }
                                writer.writeNext(rowData);
                                isHeaderWritten = true;
                                continue;
                            }

                            // 写入数据行
                            for (Cell cell : row) {
                                rowData[cell.getColumnIndex()] = cell.toString();
                            }
                            writer.writeNext(rowData);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("处理文件时出错: " + inputFilePath);
                    e.printStackTrace();
                }
            }

            System.out.println();
            System.out.println("======================Excel 转 CSV完成==============================");
            System.out.println("结果已保存到: " + outputFilePath + " |(系统桌面 excel_to_csv文件夹)");
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}