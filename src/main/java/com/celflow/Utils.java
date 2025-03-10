package com.celflow;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Utils {

    public static final String ANSI_RESET = "\u001B[0m"; // 重置颜色
    public static final String ANSI_RED = "\u001B[31m";  // 红色

    /**
     * 选择 CSV 文件（GUI 文件选择器）
     */
    public List<String> selectCsvFiles(boolean setMultiSelect) {
        List<String> filePaths = new ArrayList<>();
        JFileChooser fileChooser = new JFileChooser();

        // 设置对话框标题
        if (setMultiSelect){
            fileChooser.setDialogTitle("请选择多个 CSV 文件");
        }else {
            fileChooser.setDialogTitle("请选择单个 CSV 文件");
        }


        // 启用多选
        fileChooser.setMultiSelectionEnabled(setMultiSelect);

        // 只允许选择文件
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // 设置文件过滤器，只显示 CSV 文件
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV 文件 (*.csv)", "csv");
        fileChooser.setFileFilter(filter);

        // 设置默认打开目录为桌面
        File desktopDir = new File(System.getProperty("user.home"), "Desktop");
        if (desktopDir.exists()) {
            fileChooser.setCurrentDirectory(desktopDir);
        }

        // 获取屏幕尺寸
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // 设置 JFileChooser 的大小为屏幕的50%
        int chooserWidth = (int) (screenWidth * 0.5);
        int chooserHeight = (int) (screenHeight * 0.5);
        fileChooser.setPreferredSize(new Dimension(chooserWidth, chooserHeight));

        // 递归设置 JFileChooser 所有组件的字体大小为 24
        setFontForAllComponents(fileChooser, new Font("Dialog", Font.PLAIN, 24));

        // 显示文件选择对话框
        int returnValue = fileChooser.showOpenDialog(null);
        if (setMultiSelect){
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                for (File file : fileChooser.getSelectedFiles()) {
                    filePaths.add(file.getAbsolutePath());
                }
            }
        }else {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = null;
            try {
                filePath = selectedFile.getAbsolutePath();
            } catch (Exception e) {
                throw new IllegalArgumentException(ANSI_RED + "未选择有效文件 程序退出！！！" + ANSI_RESET);

            }
            filePaths.add(filePath);
        }
        return filePaths;
    }

    /**
     * 选择 CSV 文件（GUI 文件选择器）
     */
    public List<String> selectExcelFiles(boolean setMultiSelect) {
        List<String> filePaths = new ArrayList<>();
        JFileChooser fileChooser = new JFileChooser();

        // 设置对话框标题
        if (setMultiSelect){
            fileChooser.setDialogTitle("请选择多个 EXECL 文件");
        }else {
            fileChooser.setDialogTitle("请选择单个 EXECL 文件");
        }


        // 启用多选
        fileChooser.setMultiSelectionEnabled(setMultiSelect);

        // 只允许选择文件
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // 设置文件过滤器，只显示 CSV 文件
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Excel 文件 (*.xlsx, *.xls)",
                "xlsx", "xls"
        );
        fileChooser.setFileFilter(filter);

        // 设置默认打开目录为桌面
        File desktopDir = new File(System.getProperty("user.home"), "Desktop");
        if (desktopDir.exists()) {
            fileChooser.setCurrentDirectory(desktopDir);
        }

        // 获取屏幕尺寸
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // 设置 JFileChooser 的大小为屏幕的50%
        int chooserWidth = (int) (screenWidth * 0.5);
        int chooserHeight = (int) (screenHeight * 0.5);
        fileChooser.setPreferredSize(new Dimension(chooserWidth, chooserHeight));

        // 递归设置 JFileChooser 所有组件的字体大小为 24
        setFontForAllComponents(fileChooser, new Font("Dialog", Font.PLAIN, 24));

        // 显示文件选择对话框
        int returnValue = fileChooser.showOpenDialog(null);
        if (setMultiSelect){
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                for (File file : fileChooser.getSelectedFiles()) {
                    filePaths.add(file.getAbsolutePath());
                }
            }
        }else {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = null;
            try {
                filePath = selectedFile.getAbsolutePath();
            } catch (Exception e) {
//                System.out.println("未选则有效文件 程序退出！！！");
//                System.out.println(ANSI_RED + "未选择有效文件 程序退出！！！" + ANSI_RESET);
                throw new IllegalArgumentException(ANSI_RED + "未选择有效文件 程序退出！！！" + ANSI_RESET);

            }
            filePaths.add(filePath);
        }
        return filePaths;
    }



    /**
     * 递归设置组件及其子组件的字体
     */
    public  void setFontForAllComponents(Component component, Font font) {
        component.setFont(font); // 设置当前组件的字体
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                setFontForAllComponents(child, font); // 递归设置子组件的字体
            }
        }
    }


    /**
     * 获取桌面路径
     */
    public  String getDeskPath() {
        return System.getProperty("user.home") + "\\Desktop";
    }


    /**
     * 配置编码
     */
    public static Charset detectCharset(String filePath) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath))) {
            byte[] buffer = new byte[3];
            bis.read(buffer, 0, 3);

            if (buffer[0] == (byte) 0xEF && buffer[1] == (byte) 0xBB && buffer[2] == (byte) 0xBF) {
                return StandardCharsets.UTF_8;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return StandardCharsets.UTF_8; // 强制使用 UTF-8 编码
    }



}
