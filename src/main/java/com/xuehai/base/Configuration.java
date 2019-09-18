package com.xuehai.base;


import com.xuehai.utils.FileUtil;
import java.util.List;
import java.util.Map;

/**
 * @ClassName Configuration
 * @Description:    初始化基础配置
 * @Author Sniper
 * @Date 2019/4/8 10:04
 */
public class Configuration {

    private static final String CLASS_NAME = Configuration.class.getName();
    private static Map<String, Object> config;

    /**
     * @description:    获取基本配置
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @throws
     * @author Sniper
     * @date 2019/4/19 10:06
     */
    public static Map<String, Object> getConfig() {
        if (config == null) {
            Log.info(CLASS_NAME, "开始搜索config.yaml配置文件");
            String targetPath = System.getProperty("user.dir");
            List<String> configPathList = FileUtil.searchFiles(targetPath, "config.yaml");
            if (configPathList.size() < 1) {
                throw new NullPointerException("未找到config.yaml配置文件");
            } else if (configPathList.size() == 1) {
                config = FileUtil.getYamlValue(configPathList.get(0));
            } else {
                String beRead = null;
                for (String configPath : configPathList) {
                    if (configPath.contains("src")) {
                        beRead = configPath;
                        break;
                    }
                }
                if (beRead == null) {
                    beRead = configPathList.get(0);
                }
                Log.info(CLASS_NAME, "找到{}个config.yaml文件,当前读取:{}", configPathList.size(), beRead);
                config = FileUtil.getYamlValue(beRead);
            }
            Log.info(CLASS_NAME, "配置文件加载成功");
        }
        return config;
    }

}
