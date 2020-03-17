package com.uboxol.cloud.pandorasBox.cfg;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MailConfig {
	public static final String PROPERTIES_DEFAULT = "mail.properties";
    public static String host;
    public static Integer port;
    public static String userName;
    public static String passWord;
    public static String emailForm;
    public static String timeout;
    public static String personal;
    public static Properties properties;
    public static String fileUrl;

    static {
        init();
    }

    private static void init(){
        properties = new Properties();
        try {
            //获取配置文件内容
            InputStream inputStream = MailConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_DEFAULT);
            properties.load(inputStream);
            inputStream.close();
            //给属性赋值
            host = properties.getProperty("mailHost");
            port = Integer.parseInt(properties.getProperty("mailPort"));
            userName = properties.getProperty("mailUsername");
            passWord = properties.getProperty("mailPassword");
            emailForm = properties.getProperty("mailUsername");
            timeout = properties.getProperty("mailTimeout");
            personal = "冰箱客诉后台";//发送人
            fileUrl = properties.getProperty("file-root-dir");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
