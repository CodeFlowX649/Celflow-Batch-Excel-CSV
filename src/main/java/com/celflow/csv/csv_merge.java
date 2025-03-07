package com.celflow.csv;

import com.celflow.Utils;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

/**
 * 需求：多个 CSV 文件合并成一个
 * 逻辑：选择多个 CSV 文件，按列名对齐合并
 * 文件格式支持：UTF-8 / GBK（避免乱码）
 */
public class csv_merge {
    public static final String ANSI_RESET = "\u001B[0m"; // 重置颜色
    public static final String ANSI_RED = "\u001B[31m";  // 红色

    public void csv_merge_main() {
        List<String> filePaths = new ArrayList<>();
        System.out.println("======================运行时间受电脑配置、数据量大小影响==============================");

        filePaths =  new Utils().selectCsvFiles(true);
        System.out.println(filePaths);

        if (filePaths.isEmpty()) {
            System.out.println(ANSI_RED + "未选择文件" + ANSI_RESET);
            return;
        }

        String outputFilePath = new Utils().getDeskPath() + "\\csv_merge_Output.csv";

        // 强制使用 UTF-8 编码
        Charset charset = StandardCharsets.UTF_8;
        System.out.println("稍等片刻 正在运行。。。。。");
        mergeCsvFiles(filePaths, outputFilePath, charset);
        System.out.println();
        System.out.println("======================CSV 合并完成==============================");
        System.out.println("输出文件：" + outputFilePath+" |(系统桌面 csv_merge_Output.csv文件)");

    }


    /**
     * 合并多个 CSV 文件，按列名对齐
     */
    public static void mergeCsvFiles(List<String> filePaths, String outputFilePath, Charset charset) {
        Set<String> headerSet = new LinkedHashSet<>(); // 存储所有唯一列名（按顺序）
        List<Map<String, String>> rows = new ArrayList<>(); // 存储数据行

        for (String filePath : filePaths) {
            try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(filePath), charset))) {
                String[] headers = reader.readNext();
                if (headers == null) continue;

                headerSet.addAll(Arrays.asList(headers));

                String[] line;
                while ((line = reader.readNext()) != null) {
                    Map<String, String> row = new HashMap<>();
                    for (int i = 0; i < headers.length; i++) {
                        row.put(headers[i], i < line.length ? line[i] : "");
                    }
                    rows.add(row);
                }
            } catch (IOException | CsvValidationException e) {
                e.printStackTrace();
            }
        }

        // 写入合并后的 CSV 文件
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath), charset))) {
            List<String> headerList = new ArrayList<>(headerSet);
            writer.writeNext(headerList.toArray(new String[0])); // 写入表头

            for (Map<String, String> row : rows) {
                List<String> values = new ArrayList<>();
                for (String header : headerList) {
                    values.add(row.getOrDefault(header, ""));
                }
                writer.writeNext(values.toArray(new String[0]));
                System.out.println("row:"+row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}