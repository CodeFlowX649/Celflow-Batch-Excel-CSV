package com.celflow.csv;

import com.celflow.Utils;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 按某一列的值拆分成多个 CSV 文件（比如按“部门”拆分）
 */
public class csv_filter {

    private static String outputFilePath = new Utils().getDeskPath() + "\\csv_filter\\";

    public static final String ANSI_RESET = "\u001B[0m"; // 重置颜色
    public static final String ANSI_RED = "\u001B[31m";  // 红色


    public void csv_filter_main(String splitColumn) throws IOException {


        List<String> inputFilePaths = new Utils().selectCsvFiles(false);
        System.out.println(inputFilePaths.get(0));
//        String splitColumn = "Memo"; // 按哪一列拆分
        System.out.println("列名： "+splitColumn);

        try {
            splitCsvByColumn(inputFilePaths.get(0), outputFilePath, splitColumn);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    public static void splitCsvByColumn(String inputFilePath, String outputDirectory, String splitColumn)
            throws IOException, CsvValidationException {
        System.out.println("稍等片刻 正在运行。。。。。");

        // 创建输出目录（如果目录不存在）
        Path outputPath = Paths.get(outputDirectory);
        if (!Files.exists(outputPath)) {
            Files.createDirectories(outputPath);
            System.out.println("创建输出目录: " + outputDirectory);
        }

        // 用于存储不同部门的 CSVWriter
        Map<String, CSVWriter> writers = new HashMap<>();

        // 使用 InputStreamReader 和 OutputStreamWriter 显式指定 UTF-8 编码
        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(inputFilePath), StandardCharsets.UTF_8))) {
            String[] header = reader.readNext(); // 读取表头
            int splitColumnIndex = -1;

            // 找到要拆分的列的索引
            for (int i = 0; i < header.length; i++) {
                if (header[i].equals(splitColumn)) {
                    splitColumnIndex = i;
                    break;
                }
            }

            if (splitColumnIndex == -1) {
                throw new IllegalArgumentException("指定的列名不存在: " + splitColumn);
            }

            String[] line;
            while ((line = reader.readNext()) != null) {
                String columnValue = line[splitColumnIndex];

                // 如果该部门的 CSVWriter 还不存在，则创建一个新的
                if (!writers.containsKey(columnValue)) {
                    String outputFilePath = outputDirectory + columnValue + ".csv";
                    System.out.println("filesPath:"+outputFilePath);
                    CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath), StandardCharsets.UTF_8));
                    writers.put(columnValue, writer);
                    writer.writeNext(header); // 写入表头
                }

                // 写入当前行到对应的 CSVWriter
                writers.get(columnValue).writeNext(line);
            }

            // 关闭所有 CSVWriter
            for (CSVWriter writer : writers.values()) {
                writer.close();
            }
            System.out.println();
            System.out.println("======================CSV 文件拆分完成完成==============================");
            System.out.println("输出文件：" + outputDirectory + " |(系统桌面 csv_filter文件夹)");
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("稍等片刻 正在运行。。。。。");

        List<String> inputFilePaths = new Utils().selectCsvFiles(false);
        System.out.println(inputFilePaths.get(0));
        String splitColumn = "Memo"; // 按哪一列拆分
        System.out.println("列名： "+splitColumn);

        try {
            splitCsvByColumn(inputFilePaths.get(0), outputFilePath, splitColumn);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }
}
