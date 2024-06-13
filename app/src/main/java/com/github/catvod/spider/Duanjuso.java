package com.github.catvod.spider;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.catvod.bean.Class;
import com.github.catvod.bean.Result;
import com.github.catvod.bean.Vod;
import com.github.catvod.crawler.Spider;
import com.github.catvod.net.OkHttp;
import com.github.catvod.utils.Util;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author duanjuso
 */
public class Duanjuso extends Quark {

    private static String siteUrl = "https://duanjuso.com";
    private static String hoteUrl = "https://duanjuso.com/v1/disk/hot?size=10";
    private static String newUrl = "https://duanjuso.com/v1/disk/%s?size=10";


    public void init(Context context, String extend) {
        if (!extend.isEmpty()) siteUrl = extend;
    }

    @Override
    public String homeContent(boolean filter) throws Exception {
        List<Class> classes = new ArrayList<>();
        List<String> typeIds = Arrays.asList("latest");
        List<String> typeNames = Arrays.asList("最新");
        for (int i = 0; i < typeIds.size(); i++) classes.add(new Class(typeIds.get(i), typeNames.get(i)));
        JSONObject doc = JSONObject.parseObject(OkHttp.string(hoteUrl, getHeader()));
        List<Vod> list = new ArrayList<>();
        for (int i = 0; i < doc.getJSONArray("data").size(); i++) {
            JSONObject data = doc.getJSONArray("data").getJSONObject(i);
            String vid = getShareUrl(data.getString("doc_id"));
            String name = data.getString("disk_name");
            String remark = data.getString("update_time");
            if (StringUtils.isNotEmpty(vid)) {
                list.add(new Vod(vid, name, "", remark));
            }
        }
        return Result.string(classes, list);
    }

    public String getShareUrl(String id) {
        JSONObject doc = JSONObject.parseObject(OkHttp.string(String.format("https://duanjuso.com/v1/disk/doc/%s?from=web&with_meta=true", id), getHeader()));
        if (doc.getInteger("code") == 200) {
            return doc.getJSONObject("data").getString("link");
        } else {
            return "";
        }
    }

    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) throws Exception {
        if (StringUtils.isNotEmpty(pg) && Integer.parseInt(pg) > 1) {
            return Result.string(new ArrayList<>());
        }
        JSONObject doc = JSONObject.parseObject(OkHttp.string(String.format(newUrl, tid), getHeader()));
        List<Vod> list = new ArrayList<>();
        for (int i = 0; i < doc.getJSONArray("data").size(); i++) {
            JSONObject data = doc.getJSONArray("data").getJSONObject(i);
            String vid = getShareUrl(data.getString("doc_id"));
            String name = data.getString("disk_name");
            String remark = data.getString("update_time");
            if (StringUtils.isNotEmpty(vid)) {
                list.add(new Vod(vid, name, "", remark));
            }
        }
        return Result.string(list);
    }

    @Override
    public String searchContent(String key, boolean quick) throws Exception {
        return this.searchContent(key, quick, "1");
    }

    @Override
    public String searchContent(String key, boolean quick, String pg) throws Exception {
        int page = StringUtils.isEmpty(pg)?1:Integer.parseInt(pg);
        String json = "{\n" +
                "    \"exact\": false,\n" +
                "    \"format\": [],\n" +
                "    \"page\": " + page +",\n" +
                "    \"q\": \"" + key + "\",\n" +
                "    \"share_time\": \"\",\n" +
                "    \"size\": 15,\n" +
                "    \"type\": \"\",\n" +
                "    \"user\": \"\"\n" +
                "}";
        String res = OkHttp.post( "https://duanjuso.com/v1/search/disk", json, getHeader()).getBody();
        JSONObject doc = JSONObject.parseObject(res);
        List<Vod> list = new ArrayList<>();
        for (int i = 0; i < doc.getJSONObject("data").getJSONArray("list").size(); i++) {
            JSONObject data = doc.getJSONObject("data").getJSONArray("list").getJSONObject(i);
            String vid = data.getString("link");
            String name = data.getString("disk_name").replaceAll("<em>", "").replaceAll("</em>", "");
            String remark = data.getString("shared_time");
                list.add(new Vod(vid, name, "", remark));
        }
        return Result.string(list);
    }
}