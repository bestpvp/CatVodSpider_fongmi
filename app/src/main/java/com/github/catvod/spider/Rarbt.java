package com.github.catvod.spider;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.catvod.bean.Class;
import com.github.catvod.bean.Result;
import com.github.catvod.bean.Vod;
import com.github.catvod.crawler.Spider;
import com.github.catvod.net.OkHttp;
import com.github.catvod.utils.Jx;
import com.github.catvod.utils.Notify;
import com.github.catvod.utils.Prefers;
import com.github.catvod.utils.Tag;
import com.github.catvod.utils.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author SoEasy
 */
public class Rarbt extends Spider {

    private static String siteUrl = "";

    private final Pattern regexCategory = Pattern.compile("/type/(\\w+).html");
    private final Pattern regexPageTotal = Pattern.compile("\\$\\(\"\\.mac_total\"\\)\\.text\\('(\\d+)'\\);");

    private String jxToken;

    private Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", Util.CHROME);
        return header;
    }

    @Override
    public void init(Context context, String extend) throws Exception {
//        if (!extend.isEmpty()) siteUrl = extend;
        super.init(context, extend);
        System.out.println(extend);
        org.json.JSONObject extendJson = new org.json.JSONObject(extend);
        if (siteUrl.isEmpty()) siteUrl = extendJson.getString("siteUrl");
        String jxTokenPath = extendJson.getString("jxToken");
        if (jxTokenPath.isEmpty()) {
            jxToken = Prefers.getString("jxToken");
        } else if (jxTokenPath.startsWith("tm://")) {
            jxToken = Jx.readFileContent(jxTokenPath).isEmpty() ? Prefers.getString("jxToken") : Jx.readFileContent(jxTokenPath);
        } else {
            jxToken = Prefers.getString("jxToken");
        }
        System.out.println("siteUrl: "+siteUrl +" - jxToken: "+jxToken);
    }

    @Override
    public String homeContent(boolean filter) throws Exception {
        Notify.show(Tag.jxEnableMsg());
        List<Class> classes = new ArrayList<>();
        boolean foundFirst = true;
        Document doc = Jsoup.parse(OkHttp.string(siteUrl, getHeader()));
        Elements elements = doc.select("a.links");
        for (Element e : elements) {
            Matcher mather = regexCategory.matcher(e.attr("href"));
            if (mather.find()) {
                if (foundFirst) {
                    classes.add(new Class(mather.group(1), Tag.categoryMsg()));
                    foundFirst = false;
                }
                else {
                    classes.add(new Class(mather.group(1), e.text().trim()));
                }
            }
        }
        return Result.string(classes, parseVodListFromDoc(doc));
    }

    private List<Vod> parseVodListFromDoc(Document doc) {
        List<Vod> list = new ArrayList<>();
        Elements elements = doc.select("a.module-item");
        for (Element e : elements) {
            if (!e.select(".module-poster-item-title .badges").isEmpty()) {
                String vodId = e.attr("href");
                String vodName = e.attr("title");
                String vodPic = e.select(".module-item-pic > img").attr("data-original");
                String vodRemarks = e.select(".module-item-douban").text();
                list.add(new Vod(vodId, vodName, vodPic, vodRemarks));
            } else {
                System.out.println(e.attr("title"));
            }
        }
        return list;
    }
    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) throws Exception {
        String[] urlParams = new String[]{tid, "", "", "", "", "", "", "", pg, "", "", ""};
        if (extend != null && extend.size() > 0) {
            for (String key : extend.keySet()) {
                urlParams[Integer.parseInt(key)] = extend.get(key);
            }
        }
        Document doc = Jsoup.parse(OkHttp.string(String.format("%s/index.php/vod/show/id/%s/page/%s.html", siteUrl, tid, pg), getHeader()));
        return Result.get().vod(parseVodListFromDoc(doc)).string();
    }

    @Override
    public String detailContent(List<String> ids) throws Exception {
        Document doc = Jsoup.parse(OkHttp.string(siteUrl + ids.get(0), getHeader()));
        Elements circuits = doc.select("#y-playList > .module-tab-item");
        Elements sources = doc.select("#panel1");
        StringBuilder vod_play_url = new StringBuilder();
        StringBuilder vod_play_from = new StringBuilder();
        for (int i = 0; i < sources.size(); i++) {
            String spanText = circuits.get(i).select("span").text();
            String smallText = circuits.get(i).select("small").text();
            String playFromText = spanText + "(共" + smallText + "集)";
            vod_play_from.append(playFromText).append("$$$");
            Elements aElementArray = sources.get(i).select("a");
            for (int j = 0; j < aElementArray.size(); j++) {
                Element a = aElementArray.get(j);
                String href = siteUrl + a.attr("href");
                String text = a.text();
                vod_play_url.append(text).append("$").append(href);
                boolean notLastEpisode = j < aElementArray.size() - 1;
                vod_play_url.append(notLastEpisode ? "#" : "$$$");
            }
        }
        String title = doc.select("h1").text();
        String year = doc.select(".module-info-tag-link a").eq(0).text();
        String area = doc.select(".module-info-tag-link a").eq(1).text();
        String classifyName = doc.select(".module-info-tag-link a").eq(2).text();
        String director = doc.select(".module-info-items .module-info-item-content").eq(0).text();
        String actor = doc.select(".module-info-items .module-info-item-content").eq(1).text();
        String remark = doc.select(".module-info-items .module-info-item-content").eq(2).text();
//        String director = "SoEasy同学";
//        String actor = "SoEasy同学";
        String brief = doc.select(".show-desc p").text().replaceAll("^\\s+|\\s+$", "");
        Vod vod = new Vod();
        vod.setVodId(ids.get(0));
        vod.setVodYear(year);
        vod.setVodName(title);
        vod.setVodArea(area);
        vod.setVodActor(actor);
        vod.setVodRemarks(remark);
        vod.setVodContent(brief);
        vod.setVodDirector(director);
        vod.setTypeName(classifyName);
        vod.setVodPlayFrom(vod_play_from.toString());
        vod.setVodPlayUrl(vod_play_url.toString());
        return Result.string(vod);
    }

    @Override
    public String searchContent(String key, boolean quick) throws Exception {
        // /index.php/vod/search.html?wd=谢谢你
        String searchURL = siteUrl + String.format("/vod/search.html?wd=%s", URLEncoder.encode(key));
        String html = OkHttp.string(searchURL, getHeader());
        Elements items = Jsoup.parse(html).select(".module-card-items > .module-card-item");
        List<Vod> list = new ArrayList<>();
        for (Element item : items) {
            String vodId = item.select(".module-card-item-title > a").attr("href");
            String name = item.select(".module-card-item-title > a").text();
            String pic = item.select(".module-item-pic > img").attr("data-original");
            String remark = item.select(".module-item-note").text();
            list.add(new Vod(vodId, name, pic, remark));
        }
        return Result.string(list);
    }

    private static String replaceHexadecimalUnicode(String text) {
        // 使用正则表达式匹配十六进制Unicode字符
        Pattern pattern = Pattern.compile("\\%u[0-9A-Fa-f]{4}");
        Matcher matcher = pattern.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            // 将十六进制Unicode字符转换为相应的字符
            String hexCode = matcher.group();
            char ch = (char) Integer.parseInt(hexCode.substring(2), 16);
            // 将转换后的字符添加到StringBuffer中
            matcher.appendReplacement(sb, String.valueOf(ch));
        }
        // 完成替换
        matcher.appendTail(sb);
        return sb.toString();
    }
    @Override
    public String playerContent(String flag, String id, List<String> vipFlags) throws Exception {
        String content = OkHttp.string(id, getHeader());
        Matcher matcher = Pattern.compile("var player_play=(.*?)</script>").matcher(content);
        String json = matcher.find() ? matcher.group(1) : "";
        JSONObject parse = JSON.parseObject(json);
        String encUrl = parse.getString("url");
        encUrl = encUrl.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
        encUrl = encUrl.replaceAll("\\+", "%2B");
        encUrl = URLDecoder.decode(encUrl);
        String midUrl = replaceHexadecimalUnicode(encUrl);
        if (midUrl.contains(".m3u8") || midUrl.contains(".mp4")) {
            String realPlayUrl;
            realPlayUrl = midUrl.startsWith("/")?siteUrl + midUrl: midUrl;
//            realPlayUrl = "https://cdn15.yzzy-kb-cdn.com/20230719/19748_c2c82adf/2000k/hls/index.m3u8";
            jxToken = Prefers.getString("jxToken");
            if (!jxToken.isEmpty()){
                realPlayUrl = Jx.getUrl(jxToken, realPlayUrl);
            } else {
                Notify.show(Tag.jxBlankMsg());
            }
            return Result.get().url(realPlayUrl).header(getHeader()).string();
        }
        return Result.get().url("").header(getHeader()).string();
    }
}
