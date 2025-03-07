package com.celflow.csv;

import com.celflow.Utils;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.apache.tika.Tika;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 统一日期格式（YYYY-MM-DD）
 * 统一数字格式（去掉千分符、转换为小数）
 */
public class csv_format {

    private static String outputFilePath = new Utils().getDeskPath() + "\\format_output.csv";
    public static final String ANSI_RESET = "\u001B[0m"; // 重置颜色
    public static final String ANSI_RED = "\u001B[31m";  // 红色


    public void csv_format_main(String inputFormat,String outputFormat,String column) {
        List<String> inputFilePaths = new Utils().selectCsvFiles(false);
        System.out.println(inputFilePaths.get(0));

        System.out.println("稍等片刻 正在运行。。。。。");
//        inputFormat = "yyyy-MM-dd";
//        outputFormat = "yyyy|MM";
//        column = "Workmonth";
        System.out.println("输入格式： "+inputFormat);
        System.out.println("输出格式： "+outputFormat);
        System.out.println("列名： "+column);
        String Uppercolumn = column.toUpperCase();
        processCSV(inputFilePaths.get(0), outputFilePath,inputFormat,outputFormat,Uppercolumn);

    }


    public static void processCSV(String inputFile, String outputFile,String inputFormat,String outputFormat,String column) {

        try {
            // 使用 Apache Tika 检测文件编码
            Tika tika = new Tika();
            String detectedCharset = tika.detect(new File(inputFile));

            // 检查返回的编码是否有效，如果返回的是文件类型，可以用常见编码替代
            if ("text/csv".equals(detectedCharset)) {
                detectedCharset = "UTF-8";  // 默认设置为 UTF-8 编码
            }

            // 读取文件并去除 BOM（如果存在）
            try (BufferedReader br = new BufferedReader(new InputStreamReader(removeBOM(Files.newInputStream(Paths.get(inputFile))), detectedCharset));
                 BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(outputFile)), detectedCharset));
                 CSVReader reader = new CSVReader(br);
                 CSVWriter writer = new CSVWriter(bw)) {
                String[] frisRow = reader.readNext();
                System.out.println("frisRow:"+Arrays.toString(frisRow));
                int index = -1 ;
                for (int i = 0; i < frisRow.length; i++) {
                    if (frisRow[i].toUpperCase().equals(column)){
                        System.out.println(frisRow[i]);
                        index = i;
                        break;
                    }
                }
                System.out.println(index);
                if(index == -1){
                    System.out.println();
                    System.out.println(ANSI_RED + "未找到指定列名" + ANSI_RESET);

                    return;
                }

                String[] row;
                String FormatStr = "";
                while ((row = reader.readNext()) != null) {
                    System.out.println("row:"+ Arrays.toString(row));
                    if(row[index].equals(column) ){
                        writer.writeNext(row);
                        continue;
                    }
                    try {
                        FormatStr  = convertDateFormat(row[2],inputFormat,outputFormat);
                    } catch (Exception e) {
                        System.out.println();
                        System.out.println(ANSI_RED + "指定的日期格式错误 或 指定日期格式与实际数据格式不对应" + ANSI_RESET);
//                        e.printStackTrace();
                    }
                    row[index] = FormatStr;
                    writer.writeNext(row);
                }
            }
            System.out.println();
            System.out.println("======================CSV 数据格式化完成==============================");
            System.out.println("输出文件：" + outputFile + " |(系统桌面 format_output.csv文件)");
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static InputStream removeBOM(InputStream inputStream) throws IOException {
        byte[] bom = new byte[3];
        inputStream.read(bom, 0, 3);

        // 检查 BOM 是否存在（UTF-8 BOM是0xEF,0xBB,0xBF）
        if (bom[0] == (byte) 0xEF && bom[1] == (byte) 0xBB && bom[2] == (byte) 0xBF) {
            return inputStream; // 跳过 BOM
        }

        // 如果没有 BOM，则重新包装文件流并返回
        return new SequenceInputStream(new ByteArrayInputStream(bom), inputStream);
    }

    /**
     * 统一日期格式
     * @Param dateStr 原数据
     * @Param inputFormat 输入日期格式
     * @Param outputFormat  目标日期格式
     */
    public static String convertDateFormat(String dateStr, String inputFormat, String outputFormat) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(inputFormat);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputFormat);

        LocalDateTime dateTime;
        try {
            // 尝试解析成 LocalDateTime
            dateTime = LocalDateTime.parse(dateStr, inputFormatter);
        } catch (Exception e) {
            try {
                // 尝试解析成 LocalDate
                LocalDate date = LocalDate.parse(dateStr, inputFormatter);
                dateTime = date.atTime(LocalTime.MIN);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Invalid date format: " + dateStr);
            }
        }
        return dateTime.format(outputFormatter);
    }

    public static void main(String[] args) {
        List<String> inputFilePaths = new Utils().selectCsvFiles(false);
        System.out.println(inputFilePaths.get(0));

        System.out.println("稍等片刻 正在运行。。。。。");
        String inputFormat = "yyyy-MM-dd";
        String outputFormat = "yyyy|MM";
        String column = "workmonth";
        System.out.println("输入格式： "+inputFormat);
        System.out.println("输出格式： "+outputFormat);
        System.out.println("列名： "+column);
        String Uppercolumn = column.toUpperCase();
        processCSV(inputFilePaths.get(0), outputFilePath,inputFormat,outputFormat,Uppercolumn);
    }
}
