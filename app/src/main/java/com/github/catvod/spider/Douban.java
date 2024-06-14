package com.github.catvod.spider;

import android.content.Context;

import com.github.catvod.bean.Class;
import com.github.catvod.bean.Result;
import com.github.catvod.bean.Vod;
import com.github.catvod.crawler.Spider;
import com.github.catvod.net.OkHttp;
import com.github.catvod.utils.Dialog;
import com.github.catvod.utils.Json;
import com.github.catvod.utils.Jx;
import com.github.catvod.utils.Util;
import com.github.catvod.utils.Tag;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Douban extends Spider {

    private static final String siteUrl = "https://frodo.douban.com/api/v2";
    private static final String apikey = "?apikey=0ac44ae016490db2204ce0a042db2916";
    private static final String filterString = "{\"hot_gaia\":[{\"key\":\"sort\",\"name\":\"排序\",\"value\":[{\"n\":\"热度\",\"v\":\"recommend\"},{\"n\":\"最新\",\"v\":\"time\"},{\"n\":\"评分\",\"v\":\"rank\"}]},{\"key\":\"area\",\"name\":\"地区\",\"value\":[{\"n\":\"全部\",\"v\":\"全部\"},{\"n\":\"华语\",\"v\":\"华语\"},{\"n\":\"欧美\",\"v\":\"欧美\"},{\"n\":\"韩国\",\"v\":\"韩国\"},{\"n\":\"日本\",\"v\":\"日本\"}]}],\"tv_hot\":[{\"key\":\"type\",\"name\":\"分类\",\"value\":[{\"n\":\"综合\",\"v\":\"tv_hot\"},{\"n\":\"国产剧\",\"v\":\"tv_domestic\"},{\"n\":\"欧美剧\",\"v\":\"tv_american\"},{\"n\":\"日剧\",\"v\":\"tv_japanese\"},{\"n\":\"韩剧\",\"v\":\"tv_korean\"},{\"n\":\"动画\",\"v\":\"tv_animation\"}]}],\"show_hot\":[{\"key\":\"type\",\"name\":\"分类\",\"value\":[{\"n\":\"综合\",\"v\":\"show_hot\"},{\"n\":\"国内\",\"v\":\"show_domestic\"},{\"n\":\"国外\",\"v\":\"show_foreign\"}]}],\"movie\":[{\"key\":\"类型\",\"name\":\"类型\",\"value\":[{\"n\":\"全部类型\",\"v\":\"\"},{\"n\":\"喜剧\",\"v\":\"喜剧\"},{\"n\":\"爱情\",\"v\":\"爱情\"},{\"n\":\"动作\",\"v\":\"动作\"},{\"n\":\"科幻\",\"v\":\"科幻\"},{\"n\":\"动画\",\"v\":\"动画\"},{\"n\":\"悬疑\",\"v\":\"悬疑\"},{\"n\":\"犯罪\",\"v\":\"犯罪\"},{\"n\":\"惊悚\",\"v\":\"惊悚\"},{\"n\":\"冒险\",\"v\":\"冒险\"},{\"n\":\"音乐\",\"v\":\"音乐\"},{\"n\":\"历史\",\"v\":\"历史\"},{\"n\":\"奇幻\",\"v\":\"奇幻\"},{\"n\":\"恐怖\",\"v\":\"恐怖\"},{\"n\":\"战争\",\"v\":\"战争\"},{\"n\":\"传记\",\"v\":\"传记\"},{\"n\":\"歌舞\",\"v\":\"歌舞\"},{\"n\":\"武侠\",\"v\":\"武侠\"},{\"n\":\"情色\",\"v\":\"情色\"},{\"n\":\"灾难\",\"v\":\"灾难\"},{\"n\":\"西部\",\"v\":\"西部\"},{\"n\":\"纪录片\",\"v\":\"纪录片\"},{\"n\":\"短片\",\"v\":\"短片\"}]},{\"key\":\"地区\",\"name\":\"地区\",\"value\":[{\"n\":\"全部地区\",\"v\":\"\"},{\"n\":\"华语\",\"v\":\"华语\"},{\"n\":\"欧美\",\"v\":\"欧美\"},{\"n\":\"韩国\",\"v\":\"韩国\"},{\"n\":\"日本\",\"v\":\"日本\"},{\"n\":\"中国大陆\",\"v\":\"中国大陆\"},{\"n\":\"美国\",\"v\":\"美国\"},{\"n\":\"中国香港\",\"v\":\"中国香港\"},{\"n\":\"中国台湾\",\"v\":\"中国台湾\"},{\"n\":\"英国\",\"v\":\"英国\"},{\"n\":\"法国\",\"v\":\"法国\"},{\"n\":\"德国\",\"v\":\"德国\"},{\"n\":\"意大利\",\"v\":\"意大利\"},{\"n\":\"西班牙\",\"v\":\"西班牙\"},{\"n\":\"印度\",\"v\":\"印度\"},{\"n\":\"泰国\",\"v\":\"泰国\"},{\"n\":\"俄罗斯\",\"v\":\"俄罗斯\"},{\"n\":\"加拿大\",\"v\":\"加拿大\"},{\"n\":\"澳大利亚\",\"v\":\"澳大利亚\"},{\"n\":\"爱尔兰\",\"v\":\"爱尔兰\"},{\"n\":\"瑞典\",\"v\":\"瑞典\"},{\"n\":\"巴西\",\"v\":\"巴西\"},{\"n\":\"丹麦\",\"v\":\"丹麦\"}]},{\"key\":\"sort\",\"name\":\"排序\",\"value\":[{\"n\":\"近期热度\",\"v\":\"T\"},{\"n\":\"首映时间\",\"v\":\"R\"},{\"n\":\"高分优先\",\"v\":\"S\"}]},{\"key\":\"年代\",\"name\":\"年代\",\"value\":[{\"n\":\"全部年代\",\"v\":\"\"},{\"n\":\"2024\",\"v\":\"2024\"},{\"n\":\"2023\",\"v\":\"2023\"},{\"n\":\"2022\",\"v\":\"2022\"},{\"n\":\"2021\",\"v\":\"2021\"},{\"n\":\"2020\",\"v\":\"2020\"},{\"n\":\"2019\",\"v\":\"2019\"},{\"n\":\"2010年代\",\"v\":\"2010年代\"},{\"n\":\"2000年代\",\"v\":\"2000年代\"},{\"n\":\"90年代\",\"v\":\"90年代\"},{\"n\":\"80年代\",\"v\":\"80年代\"},{\"n\":\"70年代\",\"v\":\"70年代\"},{\"n\":\"60年代\",\"v\":\"60年代\"},{\"n\":\"更早\",\"v\":\"更早\"}]}],\"tv\":[{\"key\":\"类型\",\"name\":\"类型\",\"value\":[{\"n\":\"不限\",\"v\":\"\"},{\"n\":\"电视剧\",\"v\":\"电视剧\"},{\"n\":\"综艺\",\"v\":\"综艺\"}]},{\"key\":\"电视剧形式\",\"name\":\"电视剧形式\",\"value\":[{\"n\":\"不限\",\"v\":\"\"},{\"n\":\"喜剧\",\"v\":\"喜剧\"},{\"n\":\"爱情\",\"v\":\"爱情\"},{\"n\":\"悬疑\",\"v\":\"悬疑\"},{\"n\":\"动画\",\"v\":\"动画\"},{\"n\":\"武侠\",\"v\":\"武侠\"},{\"n\":\"古装\",\"v\":\"古装\"},{\"n\":\"家庭\",\"v\":\"家庭\"},{\"n\":\"犯罪\",\"v\":\"犯罪\"},{\"n\":\"科幻\",\"v\":\"科幻\"},{\"n\":\"恐怖\",\"v\":\"恐怖\"},{\"n\":\"历史\",\"v\":\"历史\"},{\"n\":\"战争\",\"v\":\"战争\"},{\"n\":\"动作\",\"v\":\"动作\"},{\"n\":\"冒险\",\"v\":\"冒险\"},{\"n\":\"传记\",\"v\":\"传记\"},{\"n\":\"剧情\",\"v\":\"剧情\"},{\"n\":\"奇幻\",\"v\":\"奇幻\"},{\"n\":\"惊悚\",\"v\":\"惊悚\"},{\"n\":\"灾难\",\"v\":\"灾难\"},{\"n\":\"歌舞\",\"v\":\"歌舞\"},{\"n\":\"音乐\",\"v\":\"音乐\"}]},{\"key\":\"综艺形式\",\"name\":\"综艺形式\",\"value\":[{\"n\":\"不限\",\"v\":\"\"},{\"n\":\"真人秀\",\"v\":\"真人秀\"},{\"n\":\"脱口秀\",\"v\":\"脱口秀\"},{\"n\":\"音乐\",\"v\":\"音乐\"},{\"n\":\"歌舞\",\"v\":\"歌舞\"}]},{\"key\":\"地区\",\"name\":\"地区\",\"value\":[{\"n\":\"全部地区\",\"v\":\"\"},{\"n\":\"华语\",\"v\":\"华语\"},{\"n\":\"欧美\",\"v\":\"欧美\"},{\"n\":\"国外\",\"v\":\"国外\"},{\"n\":\"韩国\",\"v\":\"韩国\"},{\"n\":\"日本\",\"v\":\"日本\"},{\"n\":\"中国大陆\",\"v\":\"中国大陆\"},{\"n\":\"中国香港\",\"v\":\"中国香港\"},{\"n\":\"美国\",\"v\":\"美国\"},{\"n\":\"英国\",\"v\":\"英国\"},{\"n\":\"泰国\",\"v\":\"泰国\"},{\"n\":\"中国台湾\",\"v\":\"中国台湾\"},{\"n\":\"意大利\",\"v\":\"意大利\"},{\"n\":\"法国\",\"v\":\"法国\"},{\"n\":\"德国\",\"v\":\"德国\"},{\"n\":\"西班牙\",\"v\":\"西班牙\"},{\"n\":\"俄罗斯\",\"v\":\"俄罗斯\"},{\"n\":\"瑞典\",\"v\":\"瑞典\"},{\"n\":\"巴西\",\"v\":\"巴西\"},{\"n\":\"丹麦\",\"v\":\"丹麦\"},{\"n\":\"印度\",\"v\":\"印度\"},{\"n\":\"加拿大\",\"v\":\"加拿大\"},{\"n\":\"爱尔兰\",\"v\":\"爱尔兰\"},{\"n\":\"澳大利亚\",\"v\":\"澳大利亚\"}]},{\"key\":\"sort\",\"name\":\"排序\",\"value\":[{\"n\":\"近期热度\",\"v\":\"T\"},{\"n\":\"首播时间\",\"v\":\"R\"},{\"n\":\"高分优先\",\"v\":\"S\"}]},{\"key\":\"年代\",\"name\":\"年代\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"2024\",\"v\":\"2024\"},{\"n\":\"2023\",\"v\":\"2023\"},{\"n\":\"2022\",\"v\":\"2022\"},{\"n\":\"2021\",\"v\":\"2021\"},{\"n\":\"2020\",\"v\":\"2020\"},{\"n\":\"2019\",\"v\":\"2019\"},{\"n\":\"2010年代\",\"v\":\"2010年代\"},{\"n\":\"2000年代\",\"v\":\"2000年代\"},{\"n\":\"90年代\",\"v\":\"90年代\"},{\"n\":\"80年代\",\"v\":\"80年代\"},{\"n\":\"70年代\",\"v\":\"70年代\"},{\"n\":\"60年代\",\"v\":\"60年代\"},{\"n\":\"更早\",\"v\":\"更早\"}]},{\"key\":\"平台\",\"name\":\"平台\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"腾讯视频\",\"v\":\"腾讯视频\"},{\"n\":\"爱奇艺\",\"v\":\"爱奇艺\"},{\"n\":\"优酷\",\"v\":\"优酷\"},{\"n\":\"湖南卫视\",\"v\":\"湖南卫视\"},{\"n\":\"Netflix\",\"v\":\"Netflix\"},{\"n\":\"HBO\",\"v\":\"HBO\"},{\"n\":\"BBC\",\"v\":\"BBC\"},{\"n\":\"NHK\",\"v\":\"NHK\"},{\"n\":\"CBS\",\"v\":\"CBS\"},{\"n\":\"NBC\",\"v\":\"NBC\"},{\"n\":\"tvN\",\"v\":\"tvN\"}]}],\"rank_list_movie\":[{\"key\":\"榜单\",\"name\":\"榜单\",\"value\":[{\"n\":\"实时热门电影\",\"v\":\"movie_real_time_hotest\"},{\"n\":\"一周口碑电影榜\",\"v\":\"movie_weekly_best\"},{\"n\":\"豆瓣电影Top250\",\"v\":\"movie_top250\"}]}],\"rank_list_tv\":[{\"key\":\"榜单\",\"name\":\"榜单\",\"value\":[{\"n\":\"实时热门电视\",\"v\":\"tv_real_time_hotest\"},{\"n\":\"华语口碑剧集榜\",\"v\":\"tv_chinese_best_weekly\"},{\"n\":\"全球口碑剧集榜\",\"v\":\"tv_global_best_weekly\"},{\"n\":\"国内口碑综艺榜\",\"v\":\"show_chinese_best_weekly\"},{\"n\":\"国外口碑综艺榜\",\"v\":\"show_global_best_weekly\"}]}]}";

    private Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("Host", "frodo.douban.com");
        header.put("Connection", "Keep-Alive");
        header.put("Referer", "https://servicewechat.com/wx2f9b06c1de1ccfca/84/page-frame.html");
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36 MicroMessenger/7.0.9.501 NetType/WIFI MiniProgramEnv/Windows WindowsWechat");
        return header;
    }

    @Override
    public void init(Context context, String extend) throws Exception {
        super.init(context, extend);
        Init.checkPermission();
        Jx.initConfig();
        Dialog.showDialog(Init.getActivity());
    }

    @Override
    public String homeContent(boolean filter) throws Exception {
//        Notify.show(Tag.notifyMsg());
        Notice.show("https://gitee.com/bestpvp/config/raw/master/config/notice.json");
        List<Class> classes = new ArrayList<>();
        List<String> typeIds = Arrays.asList("hot_gaia", "tv_hot", "show_hot", "movie", "tv", "rank_list_movie", "rank_list_tv");
        List<String> typeNames = Arrays.asList(Tag.categoryMsg(), "热播剧集", "热播综艺", "电影筛选", "电视筛选", "电影榜单", "电视剧榜单");
        for (int i = 0; i < typeIds.size(); i++) classes.add(new Class(typeIds.get(i), typeNames.get(i)));
        String recommendUrl = "http://api.douban.com/api/v2/subject_collection/subject_real_time_hotest/items" + apikey;
        JSONObject jsonObject = new JSONObject(OkHttp.string(recommendUrl, getHeader()));
        JSONArray items = jsonObject.optJSONArray("subject_collection_items");
        return Result.string(classes, parseVodListFromJSONArray(items), filter ? Json.parse(filterString) : null);
    }

    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) throws Exception {
        String sort = extend.get("sort") == null ? "T" : extend.get("sort");
        String tags = URLEncoder.encode(getTags(extend));
        int start = (Integer.parseInt(pg) - 1) * 20;
        String cateUrl;
        String itemKey = "items";
        switch (tid) {
            case "hot_gaia":
                sort = extend.get("sort") == null ? "recommend" : extend.get("sort");
                String area = extend.get("area") == null ? "全部" : extend.get("area");
                sort = sort + "&area=" + URLEncoder.encode(area);
                cateUrl = siteUrl + "/movie/hot_gaia" + apikey + "&sort=" + sort + "&start=" + start + "&count=20";
                break;
            case "tv_hot":
                String type = extend.get("type") == null ? "tv_hot" : extend.get("type");
                cateUrl = siteUrl + "/subject_collection/" + type + "/items" + apikey + "&start=" + start + "&count=20";
                itemKey = "subject_collection_items";
                break;
            case "show_hot":
                String showType = extend.get("type") == null ? "show_hot" : extend.get("type");
                cateUrl = siteUrl + "/subject_collection/" + showType + "/items" + apikey + "&start=" + start + "&count=20";
                itemKey = "subject_collection_items";
                break;
            case "tv":
                cateUrl = siteUrl + "/tv/recommend" + apikey + "&sort=" + sort + "&tags=" + tags + "&start=" + start + "&count=20";
                break;
            case "rank_list_movie":
                String rankMovieType = extend.get("榜单") == null ? "movie_real_time_hotest" : extend.get("榜单");
                cateUrl = siteUrl + "/subject_collection/" + rankMovieType + "/items" + apikey + "&start=" + start + "&count=20";
                itemKey = "subject_collection_items";
                break;
            case "rank_list_tv":
                String rankTVType = extend.get("榜单") == null ? "tv_real_time_hotest" : extend.get("榜单");
                cateUrl = siteUrl + "/subject_collection/" + rankTVType + "/items" + apikey + "&start=" + start + "&count=20";
                itemKey = "subject_collection_items";
                break;
            default:
                cateUrl = siteUrl + "/movie/recommend" + apikey + "&sort=" + sort + "&tags=" + tags + "&start=" + start + "&count=20";
                break;
        }
        JSONObject object = new JSONObject(OkHttp.string(cateUrl, getHeader()));
        JSONArray array = object.getJSONArray(itemKey);
        List<Vod> list = parseVodListFromJSONArray(array);
        int page = Integer.parseInt(pg), count = Integer.MAX_VALUE, limit = 20, total = Integer.MAX_VALUE;
        return Result.get().vod(list).page(page, count, limit, total).string();
    }

    private List<Vod> parseVodListFromJSONArray(JSONArray items) throws Exception {
        List<Vod> list = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String vodId = "msearch:" + item.optString("id");
            String name = item.optString("title");
            String pic = getPic(item);
            String remark = getRating(item);
            list.add(new Vod(vodId, name, pic, remark));
        }
        return list;
    }

    private String getRating(JSONObject item) {
        try {
            if (item.getJSONObject("rating").optString("value").equals("0")){
                return "新片上架";
            }
            else {
                return "豆瓣评分:" + item.getJSONObject("rating").optString("value");
            }
        } catch (Exception e) {
            return "随便看看";
        }
    }

    private String getPic(JSONObject item) {
        String picUrl;
        try {
            picUrl = item.getJSONObject("pic").optString("normal") + "@Referer=https://api.douban.com/@User-Agent=" + Util.CHROME;
        } catch (Exception e) {
            picUrl = "https://lintech.work/static/img/tm.png";
        }
        return picUrl;
    }

    private String getTags(HashMap<String, String> extend) {
        try {
            StringBuilder tags = new StringBuilder();
            for (String key : extend.keySet()) if (!key.equals("sort")) tags.append(extend.get(key)).append(",");
            return Util.substring(tags.toString());
        } catch (Exception e) {
            return "";
        }
    }
}
