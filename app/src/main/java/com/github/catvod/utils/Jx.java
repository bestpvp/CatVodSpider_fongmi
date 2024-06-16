package com.github.catvod.utils;

import android.os.Environment;

import com.github.catvod.net.OkHttp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Jx {

    private static final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36";

//    private static final String jxUrl = "https://www.bestpvp.site/api/m3u8/parse?token=%s&url=%s";

    private static final String configUrl = "https://gitee.com/bestpvp/config/raw/master/config/unify.json";

    private static Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", userAgent);
        return header;
    }
    public static String getUrl(String jxToken, String realPlayUrl) {
        try {
            String jxUrl = Prefers.getString("jxUrl");
            System.out.println("JAR - jxUrl: "+jxUrl);
            if (jxUrl.isEmpty()) return realPlayUrl;
            System.out.println("JAR - originalUrl: "+realPlayUrl);
            System.out.println("JAR - jxToken: "+jxToken);
            String response = OkHttp.string(String.format(jxUrl, jxToken, realPlayUrl), getHeader());
            com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSONObject.parseObject(response);

            // Handle potential missing "code" field
            if (object.containsKey("code") && object.getInteger("code") == 200) {
                Notify.show(object.getString("msg"));
                System.out.println(object.getString("msg"));
                realPlayUrl = object.getJSONObject("data").getString("jx_url");
            } else {
                // Extract message if available, otherwise use generic error message
                String message = object.containsKey("msg") ? object.getString("msg") : Tag.jxErrMsg()+" - "+jxToken;
                Notify.show(message);
                System.out.println(object);
            }
            System.out.println("JAR - jxUrl: "+realPlayUrl);
            return realPlayUrl;
        } catch (Exception e) {
//            Notify.show(Tag.jxErrMsg());
            Notify.show("时光机广告解析异常: "+e.getMessage());
            System.out.println(e.getMessage());
            return realPlayUrl;
        }
    }


    public static String readFileContent(String tmPath) {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        String realPath = root + "/" + tmPath.substring(5);
        File file = new File(realPath);
        if (!file.exists()) {
            System.out.println("JAR - 文件不存在:"+realPath);
            // 创建文件所需的所有目录
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                if (parentDir.mkdirs()) {
                    System.out.println("JAR - 目录已创建:" + parentDir.getAbsolutePath());
                } else {
                    System.out.println("JAR - 目录创建失败:" + parentDir.getAbsolutePath());
                }
            }
            // 尝试创建文件
            try {
                if (file.createNewFile()) {
                    System.out.println("JAR - 文件已创建, 请配置: " + realPath);
//                    Notify.show("JAR - 文件已创建, 请配置: " + realPath);
                } else {
                    System.out.println("JAR - 文件创建失败, 请自行创建: " + realPath);
//                    Notify.show("文件创建失败, 请自行创建: " + realPath);
                }
            } catch (IOException e) {
                System.out.println("JAR - 创建文件时出错, 请自行创建: " + e.getMessage());
//                Notify.show("创建文件时出错, 请自行创建: " + e.getMessage());
            }
            return "";
        } else {
            System.out.println("JAR - 文件存在:"+realPath);
            StringBuilder contentBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    contentBuilder.append(line).append("\n");  // Append line and a newline character
                }
            } catch (IOException e) {
                Notify.show("JAR - 读取失败: " + e.getMessage());
                System.out.println("JAR - 读取失败: " + e.getMessage());
                return "";
            }
            String result = contentBuilder.toString().trim();
            if (result.isEmpty()){
                System.out.println("JAR - 内容为空, 请配置: "+ realPath);
//                Notify.show("JAR - 内容为空, 请配置: "+ realPath);
                return "";
            }
            if (tmPath.contains("jxToken")) {
                Prefers.put("jxToken", result);
            } else if (tmPath.contains("quark")) {
                Prefers.put("quark", result);
            }
            System.out.println("JAR - 读取内容成功: "+ result);
            Notify.show("JAR - 读取内容成功: "+ result);
            return result; // Trim to remove the last newline character
        }
    }

    public  static String getJxToken(String tmPath){
        try {
            System.out.println(tmPath);
            String jxToken_from_file = readFileContent(tmPath);
            String jxToken_from_cache = Prefers.getString("jxToken");
            System.out.println(jxToken_from_file + " - " + jxToken_from_cache);
//            String result = jxToken_from_file.isEmpty() ? (jxToken_from_cache.isEmpty() ? "" : jxToken_from_cache) : jxToken_from_file;
            String result = jxToken_from_cache.isEmpty() ? jxToken_from_file : jxToken_from_cache;
            if (!result.isEmpty()){
                System.out.println("JAR - getJxToken: 保存缓存 - "+result);
                Prefers.put("jxToken", result);
            }
            String message = result.isEmpty() ? "JAR - getJxToken: 空值" : "JAR - getJxToken: 获取成功 - " + result;
            System.out.println(message);
            return result;
        } catch (Exception e) {
            System.out.println("JAR - getJxToken: 获取异常 - "+e.getMessage());
            return "";
        }
    }


    public static void initConfig() {
        try {
            String response = OkHttp.string(configUrl, getHeader());
            com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSONObject.parseObject(response);
            String last_jar_password = object.getString("jar_password");
            String current_jar_password = Prefers.getString("jar_password");
            String last_universal_password = object.getString("universal_password");
            String current_universal_password = Prefers.getString("universal_password");
            // 如果当前密码为空，或者当前密码和最新密码不一致，包括超级密钥
            if (current_jar_password.isEmpty() || !current_jar_password.equalsIgnoreCase(last_jar_password) || !current_universal_password.equalsIgnoreCase(last_universal_password)){
                String extMsg = "";
                if (object.containsKey("jar_show_dialog") && object.containsKey("jar_password")) {
                    Prefers.put("force_refresh", object.getInteger("force_refresh"));
                    Prefers.put("jar_show_dialog", object.getBoolean("jar_show_dialog"));
                    Prefers.put("jar_require_password", object.getBoolean("jar_require_password"));
                    Prefers.put("jar_password", object.getString("jar_password"));
                    Prefers.put("universal_password", object.getString("universal_password"));
                    Prefers.put("jar_message", object.getString("jar_message"));
                    Prefers.put("title", object.getString("title"));
                    Prefers.put("picture", object.getString("picture"));
                    Prefers.put("link", object.getString("link"));
                    Prefers.put("jxUrl", object.getString("jxUrl"));
                    Prefers.put("notice", object.getString("notice"));
                    if (object.getInteger("force_refresh") == 1){
                        extMsg = " + 清空本地密码";
                        Prefers.put("storedPWD", "");
                    }
                    System.out.println("JAR - initConfig: 缓存写入成功"+extMsg);
                }
            } else {
                System.out.println("JAR - initConfig: 读取缓存成功");
            }

        } catch (Exception e) {
            System.out.println("JAR - initConfig: 缓存刷新异常: "+e.getMessage());
        }
    }

}
