package com.celflow.csv;

import com.celflow.Utils;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 统计某列的唯一值个数（如不同的“城市”数量）
 */
public class csv_columncounter {
    public static final String ANSI_RESET = "\u001B[0m"; // 重置颜色
    public static final String ANSI_RED = "\u001B[31m";  // 红色
    private static String outputFilePath = new Utils().getDeskPath() + "\\csv_columncounter.csv";


    public void csv_columncounter_main(String targetColumn) {
        List<String> inputFilePaths = new Utils().selectCsvFiles(false);

//        String targetColumn = "Memo"; // 要统计的列名

        try {
            Map<String, Integer> valueCounts = countColumnValues(inputFilePaths.get(0), targetColumn);
            writeOutputFile(outputFilePath, valueCounts);
            System.out.println();
            System.out.println("======================CSV 文件统计完成==============================");
            System.out.println("结果已保存到:" + outputFilePath + " |(系统桌面 csv_filter文件夹)");
            System.out.println();

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Integer> countColumnValues(String inputFilePath, String targetColumn)
            throws IOException, CsvValidationException {
        System.out.println("稍等片刻 正在运行。。。。。");

        // 用于存储列值及其出现次数
        Map<String, Integer> valueCounts = new HashMap<>();

        // 使用 InputStreamReader 显式指定 UTF-8 编码
        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(inputFilePath), StandardCharsets.UTF_8))) {
            String[] header = reader.readNext(); // 读取表头
            int targetColumnIndex = -1;

            // 找到目标列的索引
            for (int i = 0; i < header.length; i++) {
                if (header[i].equals(targetColumn)) {
                    targetColumnIndex = i;
                    break;
                }
            }

            if (targetColumnIndex == -1) {
                throw new IllegalArgumentException("指定的列名不存在: " + targetColumn);
            }

            // 逐行读取文件
            String[] line;
            while ((line = reader.readNext()) != null) {
                String columnValue = line[targetColumnIndex];
                System.out.println("columnValue:"+columnValue);
                // 更新列值的出现次数
                valueCounts.put(columnValue, valueCounts.getOrDefault(columnValue, 0) + 1);
            }
        }

        return valueCounts;
    }

    public static void writeOutputFile(String outputFilePath, Map<String, Integer> valueCounts) throws IOException {
        // 使用 OutputStreamWriter 显式指定 UTF-8 编码
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath), StandardCharsets.UTF_8))) {
            // 写入表头
            writer.writeNext(new String[]{"值", "出现次数"});

            // 写入统计结果
            for (Map.Entry<String, Integer> entry : valueCounts.entrySet()) {
                writer.writeNext(new String[]{entry.getKey(), entry.getValue().toString()});
            }
        }
    }


    public static void main(String[] args) {
        List<String> inputFilePaths = new Utils().selectCsvFiles(false);

        String targetColumn = "Memo"; // 要统计的列名

        try {
            Map<String, Integer> valueCounts = countColumnValues(inputFilePaths.get(0), targetColumn);
            writeOutputFile(outputFilePath, valueCounts);
            System.out.println("统计完成，结果已保存到: " + outputFilePath+"\n");
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }
}
