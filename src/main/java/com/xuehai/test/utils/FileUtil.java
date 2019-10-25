package com.xuehai.test.utils;

import com.xuehai.test.base.Log;
import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FileUtil
 * @Description:    文件处理工具类
 * @Author Sniper
 * @Date 2019/3/14 10:23
 */
public class FileUtil {

    private static final String CLASS_NAME = FileUtil.class.getName();

    /**
     * @description:        读文件行,并存入list
     * @param filePath      文件路径
     * @param charset       字符编码
     * @return java.util.List<java.lang.String>
     * @author Sniper
     * @date 2019/4/23 12:58
     */
    public static List<String> readLine(String filePath, String charset) {
        List<String> list = new ArrayList<>();
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            try {
                Log.debug(CLASS_NAME, "开始读取文本内容,文件路径: {},字符编码: {}", filePath, charset);
                fileInputStream = new FileInputStream(filePath);
                inputStreamReader = new InputStreamReader(fileInputStream, charset);
                bufferedReader = new BufferedReader(inputStreamReader);
                String nextLine;
                while ((nextLine = bufferedReader.readLine()) != null && !nextLine.isEmpty()) {
                    list.add(nextLine);
                }
                Log.debug(CLASS_NAME, "文本内容读取成功");
            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            }
        } catch (IOException e) {
            Log.error(CLASS_NAME, "文件读取失败", e);
        }
        return list;
    }

    /**
     * @description:    读取文本文件内容,以单行展示
     * @param filePath  文件路径
     * @param charset   字符编码
     * @return          文本字符串内容
     * @author Sniper
     * @date 2019/3/14 10:25
     */
    public static String read(String filePath, String charset) {
        StringBuilder temp = new StringBuilder();
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            try {
                Log.debug(CLASS_NAME, "开始读取文本内容,文件路径: {},字符编码: {}", filePath, charset);
                fileInputStream = new FileInputStream(filePath);
                inputStreamReader = new InputStreamReader(fileInputStream, charset);
                bufferedReader = new BufferedReader(inputStreamReader);
                String nextLine;
                while ((nextLine = bufferedReader.readLine()) != null) {
                    temp.append(nextLine);
                }
                Log.debug(CLASS_NAME, "文本内容读取成功");
            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            }
        } catch (IOException e) {
            Log.error(CLASS_NAME, "文件读取失败", e);
        }
        return temp.toString();
    }

    /**
     * @description:    yaml文件解析
     * @param yamlPath  yaml文件路径
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author Sniper
     * @date 2019/4/19 10:22
     */
    public static Map<String, Object> getYamlValue(String yamlPath) {
        Map map = new HashMap<>();
        FileInputStream fileInputStream = null;
        try {
            try {
                Log.debug(CLASS_NAME, "开始加载YAML文件,文件路径: {}", yamlPath);
                fileInputStream = new FileInputStream(new File(yamlPath));
                Yaml yaml = new Yaml();
                map = yaml.loadAs(fileInputStream, Map.class);
                Log.debug(CLASS_NAME, "YAML文件加载成功");
            } finally {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            }
        } catch (IOException e) {
            Log.error(CLASS_NAME, "yaml读取失败", e);
        }
        return map;
    }

    public static void write(InputStream is, String savePath) {
        FileOutputStream out = null;
        try{
            try {
                out = new FileOutputStream(new File(savePath));
                int temp;
                while ((temp = is.read()) != -1) {
                    out.write(temp);
                }

            } finally {
                if (out != null)
                    out.close();
            }
        } catch (IOException e) {
            Log.error(CLASS_NAME, "文件写入失败", e);
        }
    }

    /**
     * @description:        文件递归查找
     * @param targetPath    起始检索路径
     * @param name          目标文件名
     * @return List<String>
     * @author Sniper
     * @date 2019/4/19 10:22
     */
    public static List<String> searchFiles(String targetPath, String name) {
        List<String> list = new ArrayList<>();
        Log.debug(CLASS_NAME, "开始搜索文件,当前路径:{},目标文件名:{}", targetPath, name);
        File file = new File(targetPath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File temp : files) {
                    if (temp.isDirectory()) {
                        if (!temp.getAbsolutePath().endsWith("target")) {
                            List<String> innerList = searchFiles(temp.getAbsolutePath(), name);
                            list.addAll(innerList);
                        }
                    } else {
                        if (name.equals(temp.getName())) {
                            String path = temp.getAbsolutePath();
                            list.add(path);
                            Log.debug(CLASS_NAME, "文件匹配成功,当前路径为:{}", path);
                        }
                    }
                }
            }
        } else {
            if (name.equals(file.getName())) {
                String path = file.getAbsolutePath();
                list.add(path);
                Log.debug(CLASS_NAME, "文件匹配成功,当前路径为:{}", path);
            }
        }
        return list;
    }

    /**
     * @description:    删除文件夹
     * @param directory     待删文件夹
     * @return void
     * @throw IOException
     * @author Sniper
     * @date 2019/5/22 9:51
     */
    public static void deleteDirectory(File directory) throws IOException {
        if (directory == null) {
            throw new IllegalArgumentException("错误的参数:null");
        } else if (!directory.exists()) {
            throw new IllegalArgumentException("文件不存在,当前路径:" + directory.getAbsolutePath());
        } else {
            delete(directory);
        }

        if (!directory.delete()) {
            throw new IOException("文件夹删除失败,当前路径:" + directory.getAbsolutePath());
        }
    }

    /**
     * @description: 文件删除
     * @param file      待删文件
     * @return void
     * @throw   IOException
     * @author Sniper
     * @date 2019/5/22 9:35
     */
    public static void delete(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("错误的参数:null");
        } else if (!file.exists()) {
            throw new IllegalArgumentException("文件不存在,当前路径:" + file.getAbsolutePath());
        } else {
            if (file.isDirectory()) {
                File [] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        delete(f);
                    }
                }
            } else {
                if (!file.delete()) {
                    throw new IOException("文件删除失败,当前路径:" + file.getAbsolutePath());
                }
            }
        }
    }

}
