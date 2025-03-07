package com.celflow;

import com.celflow.FileConvert.csv_to_execl;
import com.celflow.FileConvert.excel_to_csv;
import com.celflow.csv.*;

public class CelFlowMain {

    public static final String ANSI_RESET = "\u001B[0m"; // 重置颜色
    public static final String ANSI_RED = "\u001B[31m";  // 红色

    public static void main(String[] args) {
        System.out.println("======================系统运行==============================");
        System.out.println();
        String command = null; // 读取第一个参数

//        printUsage();

        try {
            command = args[0].toLowerCase();
//            command = "csv_merge";
        } catch (Exception e) {
            System.out.println();
//            System.out.println("没有指定有效参数！！！！！");
            System.out.println(ANSI_RED + "没有指定有效参数" + ANSI_RESET);
        }


        switch (command) {
            case "-csv_merge":
                new csv_merge().csv_merge_main();
                break;
            case "-csv_removal":
                new csv_removal().csv_removal_main();
                break;
            case "-csv_format":
                try {
                    new csv_format().csv_format_main(args[1],args[2],args[3]);
                } catch (Exception e) {
                    System.out.println();
                    System.out.println(ANSI_RED + "没有指定有效参数" + ANSI_RESET);
                }
                break;
            case "-csv_filter":
                try {
                    new csv_filter().csv_filter_main(args[1]);
                } catch (Exception e) {
                    System.out.println();
                    System.out.println(ANSI_RED + "缺少有效参数，请检查是否输入列名 或 检查列名是否正确" + ANSI_RESET);
                }
                break;
            case "-csv_columncounter":
                try {
                    new csv_columncounter().csv_columncounter_main(args[1]);
                } catch (Exception e) {
                    System.out.println();
                    System.out.println(ANSI_RED + "缺少有效参数，请检查是否输入列名 或 检查列名是否正确" + ANSI_RESET);
                }
                break;
            case "-csv_to_execl":
                try {
                    new csv_to_execl().csv_to_execl_main();
                } catch (Exception e) {
                    System.out.println();
                    System.out.println(ANSI_RED + "所选文件异常" + ANSI_RESET);
                }
                break;
            case "-excel_to_csv":
                try {
                    new excel_to_csv().excel_to_csv_main();
                } catch (Exception e) {
                    System.out.println();
                    System.out.println(ANSI_RED + "所选文件异常" + ANSI_RESET);
                }
                break;
            case "-help":
                printUsage();
                break;
            default:
                System.out.println();
                System.out.println(ANSI_RED + "未知参数："+ command + ANSI_RESET);
                break;
        }
        System.out.println();
        System.out.println("提示信息：如果出现中文乱码的问题 请修改原文件的字符集编码为UTF-8");
        System.out.println("提示信息：使用记事本打开 查看右下角 字符集编码 ");
    }


    private static void printUsage() {
        System.out.println("程序参数说明：");
        System.out.println("1. CSV 合并：csv_merge");
        System.out.println("2. CSV 去除重复：csv_removal");
        System.out.println("3. CSV 格式化日期：csv_format 输入日期格式 输出日期格式 列名");
        System.out.println("   示例：csv_format yyyy-MM-dd yyyy/MM/dd 编码");
        System.out.println("4. CSV 拆分文件：csv_filter 列名");
        System.out.println("   示例：csv_filter 部门");
        System.out.println("5. CSV 数据统计：csv_columncounter 列名");
        System.out.println("   示例：csv_filter 部门");
        System.out.println("6. CSV转Execl：csv_to_execl");
        System.out.println("7. ExecL转CSV：excel_to_csv");
        System.out.println();
    }
}
