package com.celflow.csv;

import com.celflow.Utils;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.apache.tika.Tika;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * 去重：相同数据行去重
 * 删除空行：删除全是空值的行
 */
public class csv_removal {
    private static String outputFilePath = new Utils().getDeskPath() + "\\large_output.csv";

    public void csv_removal_main(){
        List<String> inputFilePaths = new Utils().selectCsvFiles(false); // 输入 CSV 文件
        System.out.println(inputFilePaths.get(0));


        System.out.println("稍等片刻 正在运行。。。。。");

        processCSV(inputFilePaths.get(0), outputFilePath);
    }

    public static void processCSV(String inputFile, String outputFile) {
        Set<List<String>> uniqueRows = new HashSet<>(); // 存储去重数据

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

                String[] row;
                while ((row = reader.readNext()) != null) {
                    if (isEmptyRow(row)) continue; // 过滤空行
                    System.out.println("row:"+Arrays.toString(row));
                    List<String> rowList = Arrays.asList(row);
                    if (uniqueRows.add(rowList)) { // 去重
                        writer.writeNext(row);
                    }
                }
            }
            System.out.println();
            System.out.println("======================CSV 去重/清洗完成==============================");
            System.out.println("输出文件：" + outputFile + " |(系统桌面 large_output.csv文件)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 判断是否是全空行
    private static boolean isEmptyRow(String[] row) {
        return Arrays.stream(row).allMatch(cell -> cell == null || cell.trim().isEmpty());
    }

    // 去除 BOM (如果存在)
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
}


//    public static void main(String[] args) {
//        List<String> inputFilePaths = new Utils().selectCsvFiles(false); // 输入 CSV 文件
//        System.out.println(inputFilePaths.get(0));
//
//        System.out.println("提示信息：如果出现中文乱码的问题 请修改原文件的字符集编码为UTF-8");
//        System.out.println("提示信息：推荐方式 使用记事本打开 查看右下角 字符集编码 ");
//        System.out.println("稍等片刻 正在运行。。。。。");
//
//        processCSV(inputFilePaths.get(0), outputFilePath);
//    }