package com.github.catvod.spider;

import android.text.TextUtils;

import com.github.catvod.bean.Class;
import com.github.catvod.bean.Filter;
import com.github.catvod.bean.Result;
import com.github.catvod.bean.Vod;
import com.github.catvod.crawler.Spider;
import com.github.catvod.net.OkHttp;
import com.github.catvod.utils.Util;
import com.github.catvod.utils.Notify;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author FongMi
 */
public class Dm84 extends Spider {

    private static final String siteUrl = "https://dm84.tv";

    private HashMap<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("User-Agent", Util.CHROME);
        headers.put("Accept", Util.ACCEPT);
        return headers;
    }

    private Filter getFilter(String name, String key, List<String> texts) {
        List<Filter.Value> values = new ArrayList<>();
        for (String text : texts) {
            if (text.isEmpty()) continue;
            String n = text.replace("按", "");
            String v = key.equals("by") ? replaceBy(text) : text;
            values.add(new Filter.Value(n, v));
        }
        return new Filter(key, name, values);
    }

    private String replaceBy(String text) {
        return text.replace("按时间", "time").replace("按人气", "hits").replace("按评分", "score");
    }

    @Override
    public String homeContent(boolean filter) {
        List<Vod> list = new ArrayList<>();
        List<Class> classes = new ArrayList<>();
        LinkedHashMap<String, List<Filter>> filters = new LinkedHashMap<>();
        Document doc = Jsoup.parse(OkHttp.string(siteUrl, getHeaders()));
        int count = 0;
        for (Element element : doc.select("ul.nav_row > li > a")) {
            if (element.attr("href").startsWith("/list")) {
                String id = element.attr("href").split("-")[1].substring(0, 1);
                String name = "";
                if (count == 1){
                    name = "插兜的干货仓库";
                }
                else{
                    name = element.text().substring(0, 2);
                }
                classes.add(new Class(id, name));
            }
            count++;
        }
        for (Class item : classes) {
            doc = Jsoup.parse(OkHttp.string(siteUrl + "/list-" + item.getTypeId() + ".html", getHeaders()));
            Elements elements = doc.select("ul.list_filter > li > div");
            List<Filter> array = new ArrayList<>();
            array.add(getFilter("类型", "type", elements.get(0).select("a").eachText()));
            array.add(getFilter("时间", "year", elements.get(1).select("a").eachText()));
            array.add(getFilter("排序", "by", elements.get(2).select("a").eachText()));
            filters.put(item.getTypeId(), array);
        }
        for (Element element : doc.select("div.item")) {
            String img = element.select("a.cover").attr("data-bg");
            String url = element.select("a.title").attr("href");
            String name = element.select("a.title").text();
            String remark = element.select("span.desc").text();
            String id = url.split("/")[2];
            list.add(new Vod(id, name, img, remark));
        }
        //{"class":[{"type_id":"1","type_name":"插兜的干货仓库"},{"type_id":"2","type_name":"日本"},{"type_id":"3","type_name":"欧美"},{"type_id":"4","type_name":"电影"}],"filters":{"1":[{"key":"type","name":"类型","value":[{"n":"全部","v":"全部"},{"n":"奇幻","v":"奇幻"},{"n":"战斗","v":"战斗"},{"n":"玄幻","v":"玄幻"},{"n":"穿越","v":"穿越"},{"n":"科幻","v":"科幻"},{"n":"武侠","v":"武侠"},{"n":"热血","v":"热血"},{"n":"耽美","v":"耽美"},{"n":"搞笑","v":"搞笑"},{"n":"动态漫画","v":"动态漫画"}]},{"key":"year","name":"时间","value":[{"n":"全部","v":"全部"},{"n":"2024","v":"2024"},{"n":"2023","v":"2023"},{"n":"2022","v":"2022"},{"n":"2021","v":"2021"},{"n":"2020","v":"2020"},{"n":"2019","v":"2019"},{"n":"2018","v":"2018"},{"n":"2017","v":"2017"},{"n":"2016","v":"2016"},{"n":"2015","v":"2015"}]},{"key":"by","name":"排序","value":[{"n":"时间","v":"time"},{"n":"人气","v":"hits"},{"n":"评分","v":"score"}]}],"2":[{"key":"type","name":"类型","value":[{"n":"全部","v":"全部"},{"n":"冒险","v":"冒险"},{"n":"奇幻","v":"奇幻"},{"n":"战斗","v":"战斗"},{"n":"后宫","v":"后宫"},{"n":"热血","v":"热血"},{"n":"励志","v":"励志"},{"n":"搞笑","v":"搞笑"},{"n":"校园","v":"校园"},{"n":"机战","v":"机战"},{"n":"悬疑","v":"悬疑"},{"n":"治愈","v":"治愈"},{"n":"百合","v":"百合"},{"n":"恐怖","v":"恐怖"},{"n":"泡面番","v":"泡面番"},{"n":"恋爱","v":"恋爱"},{"n":"推理","v":"推理"}]},{"key":"year","name":"时间","value":[{"n":"全部","v":"全部"},{"n":"2024","v":"2024"},{"n":"2023","v":"2023"},{"n":"2022","v":"2022"},{"n":"2021","v":"2021"},{"n":"2020","v":"2020"},{"n":"2019","v":"2019"},{"n":"2018","v":"2018"},{"n":"2017","v":"2017"},{"n":"2016","v":"2016"},{"n":"2015","v":"2015"}]},{"key":"by","name":"排序","value":[{"n":"时间","v":"time"},{"n":"人气","v":"hits"},{"n":"评分","v":"score"}]}],"3":[{"key":"type","name":"类型","value":[{"n":"全部","v":"全部"},{"n":"科幻","v":"科幻"},{"n":"冒险","v":"冒险"},{"n":"战斗","v":"战斗"},{"n":"百合","v":"百合"},{"n":"奇幻","v":"奇幻"},{"n":"热血","v":"热血"},{"n":"搞笑","v":"搞笑"}]},{"key":"year","name":"时间","value":[{"n":"全部","v":"全部"},{"n":"2024","v":"2024"},{"n":"2023","v":"2023"},{"n":"2022","v":"2022"},{"n":"2021","v":"2021"},{"n":"2020","v":"2020"},{"n":"2019","v":"2019"},{"n":"2018","v":"2018"},{"n":"2017","v":"2017"},{"n":"2016","v":"2016"},{"n":"2015","v":"2015"}]},{"key":"by","name":"排序","value":[{"n":"时间","v":"time"},{"n":"人气","v":"hits"},{"n":"评分","v":"score"}]}],"4":[{"key":"type","name":"类型","value":[{"n":"全部","v":"全部"},{"n":"搞笑","v":"搞笑"},{"n":"奇幻","v":"奇幻"},{"n":"治愈","v":"治愈"},{"n":"科幻","v":"科幻"},{"n":"喜剧","v":"喜剧"},{"n":"冒险","v":"冒险"},{"n":"动作","v":"动作"},{"n":"爱情","v":"爱情"}]},{"key":"year","name":"时间","value":[{"n":"全部","v":"全部"},{"n":"2024","v":"2024"},{"n":"2023","v":"2023"},{"n":"2022","v":"2022"},{"n":"2021","v":"2021"},{"n":"2020","v":"2020"},{"n":"2019","v":"2019"},{"n":"2018","v":"2018"},{"n":"2017","v":"2017"},{"n":"2016","v":"2016"},{"n":"2015","v":"2015"}]},{"key":"by","name":"排序","value":[{"n":"时间","v":"time"},{"n":"人气","v":"hits"},{"n":"评分","v":"score"}]}]},"jx":0,"list":[{"vod_id":"4662.html","vod_name":"大雨","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXiaISONIYuibNp0swRnss1BzLd0mDDlWAUIA/600","vod_remarks":"高清"},{"vod_id":"4660.html","vod_name":"蜡笔小新：新次元！超能力大决战","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXiaISONIYuibNpQ8uAPugXvkuicX53IiaQw4Mg/600","vod_remarks":"高清"},{"vod_id":"4657.html","vod_name":"灌篮高手","vod_pic":"https://gimg3.baidu.com/gimg/app=2028&src=lz.sinaimg.cn/large/008w3CKjgy1h9ghrbuyl4j307i0ant9q.jpg","vod_remarks":"高清"},{"vod_id":"4648.html","vod_name":"金田一少年之事件\ufffd 2024-03-15 13:50:09.212  7924-8845  PRETTY_LOG...omeContent com.github.catvod.demo               D  │ \ufffd\ufffd 剧场版1：歌剧院新杀人事件","vod_pic":"https://gimg3.baidu.com/gimg/app=2028&src=lz.sinaimg.cn/mw600/691c7126jw1f76qr7o7gvj206y09qwey.jpg","vod_remarks":"高清"},{"vod_id":"4647.html","vod_name":"金田一少年事件簿2 杀戮的深蓝","vod_pic":"https://gimg3.baidu.com/gimg/app=2028&src=lz.sinaimg.cn/mw600/6ea6a5a6gy1gritu481oaj20f00ldq4v.jpg","vod_remarks":"高清"},{"vod_id":"4644.html","vod_name":"金田一少年事件簿 黑魔术杀人事件","vod_pic":"https://p5.toutiaoimg.com/img/tos-cn-i-siecs4i2o7/0a2e2a64b4494be1a503c9bdcc5741c5~noop.image","vod_remarks":"高清"},{"vod_id":"4646.html","vod_name":"金田一少年事件簿：歌剧院最后的杀人","vod_pic":"https://puui.qpic.cn/vcover_vt_pic/0/34tw3bb7dvqv6qkt1450402283.jpg","vod_remarks":"高清"},{"vod_id":"4645.html","vod_name":"金田一少年事件簿：吸血鬼传说杀人事件","vod_pic":"https://gimg3.baidu.com/gimg/app=2028&src=lz.sinaimg.cn/mw600/60f3a03fgy1gnbkqnjxaaj20cg0iojs9.jpg","vod_remarks":"高清"},{"vod_id":"4638.html","vod_name":"后空翻少年！！ 剧场版","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXvduia6EHUicKzDMl9me0lVabV9LGicz8c8Xg/600","vod_remarks":"高清"},{"vod_id":"4636.html","vod_name":"一人之下剧场版：锈铁重现","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXhcl6WMIkibqI9Eqa8rqzVfn6urmbTvuKOw/600","vod_remarks":"高清"},{"vod_id":"4618.html","vod_name":"飞鸭向前冲","vod_pic":"https://gimg3.baidu.com/gimg/app=2028&src=lz.sinaimg.cn/large/8a65eec0gy1hfh91kzth1j207i0b9q2v.jpg","vod_remarks":"高清"},{"vod_id":"4617.html","vod_name":"火神之天启之子","vod_pic":"https://vcover-vt-pic.puui.qpic.cn/vcover_vt_pic/0/mzc0020016fukdz1705894196740/260","vod_remarks":"高清"},{"vod_id":"4613.html","vod_name":"星愿","vod_pic":"https://gimg3.baidu.com/gimg/app=2028&src=tvax4.sinaimg.cn/large/8a65eec0gy1hiqh0wlzfvj207i0b4div.jpg","vod_remarks":"高清"},{"vod_id":"4558.html","vod_name":"怪物女孩","vod_pic":"https://gimg3.baidu.com/gimg/app=2028&src=tvax4.sinaimg.cn/large/8a65eec0gy1hjg03mlaesj207i0amtch.jpg","vod_remarks":"高清"},{"vod_id":"4543.html","vod_name":"贝肯熊：火星任务","vod_pic":"https://vcover-vt-pic.puui.qpic.cn/vcover_vt_pic/0/mzc00200pgcb9os1703485955210/260","vod_remarks":"高清"},{"vod_id":"4539.html","vod_name":"火影忍者剧场版1-11合集","vod_pic":"https://gimg3.baidu.com/gimg/app=2028&src=tvax4.sinaimg.cn/mw690/005BjCpAgw1f24rwdkf6ij30br0goq68.jpg","vod_remarks":""},{"vod_id":"4538.html","vod_name":"名侦探柯南 灰原哀物语～黑铁的神秘列车～","vod_pic":"https://gimg3.baidu.com/gimg/app=2028&src=tvax4.sinaimg.cn/mw690/5a688945ly1hldsjrptbqj21xo2qd1kx.jpg","vod_remarks":"高清"},{"vod_id":"4524.html","vod_name":"画江湖之天罡","vod_pic":"https://vcover-vt-pic.puui.qpic.cn/vcover_vt_pic/0/mzc0020030lu5ww1702003471681/260","vod_remarks":"高清"},{"vod_id":"4523.html","vod_name":"橘色奇迹 -未来-","vod_pic":"https://p26.toutiaoimg.com/img/tos-cn-i-siecs4i2o7/ed07f228451d4f71949c5b58c713174d~noop.image","vod_remarks":"高清"},{"vod_id":"4495.html","vod_name":"警笛","vod_pic":"https://gimg3.baidu.com/gimg/app=2028&src=tvax4.sinaimg.cn/large/7f137f9aly1hkmpukafrcj207i0a6mxk.jpg","vod_remarks":"HD高清"},{"vod_id":"4486.html","vod_name":"中国惊奇先生·极道仙师","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXqn74ddQnpBjcVctcbCjoZHRW6ymHhA4Gg/600","vod_remarks":"HD高清"},{"vod_id":"4485.html","vod_name":"青春期猪头少年不会梦到娇怜外出妹","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXqn74ddQnpBjOHb5JjbRUK76N5aFn4V8pQ/600","vod_remarks":"HD高清"},{"vod_id":"4484.html","vod_name":"鬼灭之刃 柱众会议·蝶屋敷篇","vod_pic":"http://mapp.alicdn.com/16097873446926qNqDDLrdu9Xed4.jpeg","vod_remarks":"HD高清"},{"vod_id":"4483.html","vod_name":"鬼灭之刃 兄妹的羁绊","vod_pic":"https://y.gtimg.cn/music/photo_new/T023R300x300 2024-03-15 13:50:09.212  7924-8845  PRETTY_LOG...omeContent com.github.catvod.demo               D  │ M0000025G6Zg2bD0AI.jpg","vod_remarks":"HD高清"},{"vod_id":"4482.html","vod_name":"鬼灭之刃 那田蜘蛛山篇","vod_pic":"https://y.gtimg.cn/music/photo_new/T023R300x300M000004TElQV3Im6FH.jpg","vod_remarks":"HD高清"},{"vod_id":"4481.html","vod_name":"坏蛋联盟：坏坏假期","vod_pic":"https://gimg3.baidu.com/gimg/app=2028&src=tvax4.sinaimg.cn/large/8a65eec0gy1hkdjoooevwj207i0b9djg.jpg","vod_remarks":"HD高清"},{"vod_id":"4477.html","vod_name":"名侦探柯南：黑铁的鱼影","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXqn74ddQnpBjNRdFzNT2e0AaWx4g6FYIfw/600","vod_remarks":"高清"},{"vod_id":"4476.html","vod_name":"蜥蜴伯伯里奥","vod_pic":"https://gimg3.baidu.com/gimg/app=2028&src=tvax4.sinaimg.cn/large/8a65eec0gy1hk33tnu8kjj207i0b9tbu.jpg","vod_remarks":"高清"},{"vod_id":"4475.html","vod_name":"魔发精灵3","vod_pic":"https://gimg3.baidu.com/gimg/app=2028&src=tvax4.sinaimg.cn/large/8a65eec0gy1hjq6idbjgxj207i0b90x0.jpg","vod_remarks":"高清"},{"vod_id":"4456.html","vod_name":"哆啦A梦：大雄与天空的理想乡","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXtx9oblH1fKJicibI7CH0GQZHL4yHibOIvbBA/600","vod_remarks":"HD高清"},{"vod_id":"4434.html","vod_name":"蓝色巨人","vod_pic":"https://gimg3.baidu.com/gimg/app=2028&src=tvax4.sinaimg.cn/large/8a65eec0gy1hj2x2eas6xj207i0alq6y.jpg","vod_remarks":"HD高清"},{"vod_id":"4394.html","vod_name":"落第魔女","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXpfmYVtR3bAjiaQfr1BGpwHc7nPiaM8Ns4cw/600","vod_remarks":"HD高清"},{"vod_id":"4359.html","vod_name":"长安三万里","vod_pic":"https://puui.qpic.cn/vcover_vt_pic/0/mzc00200887tz3a1688286304531/260","vod_remarks":"HD高清"},{"vod_id":"4332.html","vod_name":"茶啊二中","vod_pic":"https://puui.qpic.cn/vcover_vt_pic/0/mzc00200fac1rlf1694405362384/260","vod_remarks":"HD高清"},{"vod_id":"4331.html","vod_name":"忍者神龟：变种大乱斗","vod_pic":"https://gimg3.baidu.com/gimg/app=2028&src=tvax4.sinaimg.cn/large/7f137f9aly1hhzxgdi8pqj207i0b43z4.jpg","vod_remarks":"HD高清"},{"vod_id":"4312.html","vod_name":"男子游泳部剧场版：通往世界的路之梦","vod_pic":"http://mapp.alicdn.com/16054413780938WCAYQUbLcoucc6.jpg","vod_remarks":"高清"}],"parse":0}
        Notify.show("感谢使用时光机数据源");
        return Result.string(classes, list, filters);
    }

    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
        List<Vod> list = new ArrayList<>();
        if (extend.get("type") == null) extend.put("type", "");
        if (extend.get("year") == null) extend.put("year", "");
        if (extend.get("by") == null) extend.put("by", "time");
        String by = extend.get("by");
        String type = URLEncoder.encode(extend.get("type"));
        String year = extend.get("year");
        String target = siteUrl + String.format("/show-%s--%s-%s--%s-%s.html", tid, by, type, year, pg);
        Document doc = Jsoup.parse(OkHttp.string(target, getHeaders()));
        for (Element element : doc.select("div.item")) {
            String img = element.select("a.cover").attr("data-bg");
            String url = element.select("a.title").attr("href");
            String name = element.select("a.title").text();
            String remark = element.select("span.desc").text();
            String id = url.split("/")[2];
            list.add(new Vod(id, name, img, remark));
        }
        //{"jx":0,"list":[{"vod_id":"4519.html","vod_name":"挣扎吧，亚当君","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXk85LJvyvFjxPbbH7w3R1SLasr6wFIjwHA/600","vod_remarks":"完结"},{"vod_id":"4587.html","vod_name":"勇气爆发","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXl4FJfbO3HJyBwGxjpfC1aOgFvoicLKq5Hw/600","vod_remarks":"第10话"},{"vod_id":"4585.html","vod_name":"月刊妄想科学","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXl4FJfbO3HJyPJcKgwCEzn26STK8fwTHibQ/600","vod_remarks":"第10话"},{"vod_id":"4583.html","vod_name":"异世界温泉开拓记","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXl4FJfbO3HJyt2icvzoXanQeMtw9cOsiaqOQ/600","vod_remarks":"第10话"},{"vod_id":"4584.html","vod_name":"新 福星小子 第二季","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXl4FJfbO3HJyb2kWszlEXyobdnLWJdRtibA/600","vod_remarks":"第10话"},{"vod_id":"4547.html","vod_name":"迷宫饭","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXl4FJfbO3HJyjdkCBE4LiaHoFe2zHUCC7rw/600","vod_remarks":"第11话"},{"vod_id":"4551.html","vod_name":"魔都精兵的奴隶","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXl4FJfbO3HJyJU8eRibg8BJKfiaBY9Ez7Lyw/600","vod_remarks":"第11话"},{"vod_id":"4548.html","vod_name":"秒杀外挂太强了，异世界的家伙们根本就不是对手。","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXl4FJfbO3HJySaRA2WL2iaSygeFCek5j9Fg/600","vod_remarks":"第11话"},{"vod_id":"4586.html","vod_name":"魔女与野兽","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXl4FJfbO3HJynDiaVqNH7p3xib8qv4bXl31Q/600","vod_remarks":"第9话"},{"vod_id":"4593.html","vod_name":"牙狼：钢之继承者","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXkx0v75lpiarhFDiaHLcfVyDg7EANoeKtdmw/600","vod_remarks":"第9话"},{"vod_id":"4580.html","vod_name":"通灵王 FLOWRS","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXl4FJfbO3HJyv7B7icPr7ibPgeGdcsIvQhlw/600","vod_remarks":"第10话"},{"vod_id":"4544.html","vod_name":"弱势角色友崎君 第二季","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXl4FJfbO3HJyLu6ictzIwCrbAEwSH8U4dLg/600","vod_remarks":"第11话"},{"vod_id":"4581.html","vod_name":"外科医生爱丽丝","vod_pic":"https://gimg3.baidu.com/gimg/app=2028&src=tvax4.sinaimg.cn/mw1024/006yt1Omgy1hfhdbwcly4j30tm15oar2.jpg","vod_remarks":"第10话"},{"vod_id":"4577.html","vod_name":"战国妖狐 救世姐弟篇","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXl4FJfbO3HJyw5X62vGMA6g9QTzApPeLvg/600","vod_remarks":"第10话"},{"vod_id":"2127.html","vod_name":"游戏王 GO RUSH！！","vod_pic":"https://y.gtimg.cn/music/photo_new/T023R300x300M000002KESCy0esvZK.jpg","vod_remarks":"第99话"},{"vod_id":"4550.html","vod_name":"梦想成为魔法少女","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXl4FJfbO3HJyAgqE4vqfzdvReG0vjvzeUw/600","vod_remarks":"第11话"},{"vod_id":"4549.html","vod_name":"异修罗","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXl4FJfbO3HJy6WcAEJSHDdUsPNaaliaxeIg/600","vod_remarks":"第11话"},{"vod_id":"4579.html","vod_name":"到了30岁还是处男，似乎会变成魔法师","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXl4FJfbO3HJyI9KVDtC2e9Y5rwIsfVn9zw/600","vod_remarks":"第10话"},{"vod_id":"4578.html","vod_name":"金属口红","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXl4FJfbO3HJyVhAl2FXD6LaI2SOibL5ecQQ/600","vod_remarks":"第10话"},{"vod_id":"4545.html","vod_name":"欢迎来到实力至上主义的教室 第三季","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXl4FJfbO3HJyYiaLYKSglIBqQtSskbhPUqg/600","vod_remarks":"第11话"},{"vod_id":"4576.html","vod_name":"恶役千金LV99～我是隐藏BOSS但不是魔王～","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXl4FJfbO3HJypfGjFKAS4mia 2024-03-15 13:52:20.293  7924-8855  PRETTY_LOG...oryContent com.github.catvod.demo               D  │ CJibGyVyxzXw/600","vod_remarks":"第10话"},{"vod_id":"4575.html","vod_name":"北海道辣妹贼拉可爱","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXl4FJfbO3HJykoTB2XJwrlvpibzYT8Nnjpg/600","vod_remarks":"第10话"},{"vod_id":"4574.html","vod_name":"奇异贤伴 黑色天使 第二季","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXl4FJfbO3HJyq9TFdOtqWL5FfMP7mQEyPQ/600","vod_remarks":"第10话"},{"vod_id":"4571.html","vod_name":"公主大人，接下来是“拷问”时间","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXl4FJfbO3HJydOK6kmGKljRXu0nHygNsEw/600","vod_remarks":"第10话"},{"vod_id":"4572.html","vod_name":"月光下的异世界之旅 第二季","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXl4FJfbO3HJySwQZ9TKZH3gXfOmxSZhjrA/600","vod_remarks":"第10话"},{"vod_id":"4573.html","vod_name":"愚蠢天使与恶魔共舞","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXk85LJvyvFjxxmAibhAiaSUdwbLFIoeFDd4A/600","vod_remarks":"第10话"},{"vod_id":"4540.html","vod_name":"为了在异世界也能抚摸毛茸茸而努力。","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXk85LJvyvFjxuuYZlI1iaY9ssDMhyHhicxcw/600","vod_remarks":"第11话"},{"vod_id":"4656.html","vod_name":"忍者神威","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXiaISONIYuibNpY5IyMp9NbCmTrSfMIzzt2w/600","vod_remarks":"第5话"},{"vod_id":"4432.html","vod_name":"爱犬指令","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXqUNAFawO2RA3t2Mq9GwZgicia2eNSFb60Ew/600","vod_remarks":"第19话"},{"vod_id":"4569.html","vod_name":"至高之牌 第二季","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXl4FJfbO3HJyRAwxl9rcY4wJ21KqN0NbLg/600","vod_remarks":"第10话"},{"vod_id":"4364.html","vod_name":"香格里拉·开拓异境～粪作猎手挑战神作～","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXpfmYVtR3bAj7LqzAlbrhA1q3ZpNnwibaMQ/600","vod_remarks":"第22话"},{"vod_id":"4591.html","vod_name":"王者天下 第五季","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXl4FJfbO3HJyhMONR5SO1AgNsPaSXdhyBg/600","vod_remarks":"第9话"},{"vod_id":"4365.html","vod_name":"队长小翼2 世少篇","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXpfmYVtR3bAjnwLB8pASNSNkCFX76aR26g/600","vod_remarks":"第23话"},{"vod_id":"4423.html","vod_name":"七大罪：默示录的四骑士","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXpfmYVtR3bAjtL2XZgxfgs6TJUQibC9vUZQ/600","vod_remarks":"第21话"},{"vod_id":"4568.html","vod_name":"轮回七次的恶役千金，在前敌国享受随心所欲的新婚生活","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXl4FJfbO3HJyYb0dwaoiceNuAtOWqDkkPJg/600","vod_remarks":"第10话"},{"vod_id":"4594.html","vod_name":"狩火之王 第二季","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXkx0v75lpiarhMa1zo4SJX51GHU3gQoOYXQ/600","vod_remarks":"第9话"}],"parse":0}
        return Result.string(list);
    }

    @Override
    public String detailContent(List<String> ids) {
        Document doc = Jsoup.parse(OkHttp.string(siteUrl.concat("/v/").concat(ids.get(0)), getHeaders()));
        String name = doc.select("h1.v_title").text();
        String remarks = doc.select("p.v_desc > span.desc").text();
        String img = doc.select("meta[property=og:image]").attr("content");
        String area = doc.select("meta[name=og:video:area]").attr("content");
        String type = doc.select("meta[name=og:video:class]").attr("content");
        String actor = doc.select("meta[name=og:video:actor]").attr("content");
        String content = "关注「插兜的干货仓库」: "+doc.select("meta[property=og:description]").attr("content");
        String year = doc.select("meta[name=og:video:release_date]").attr("content");
        String director = doc.select("meta[name=og:video:director]").attr("content");

        Vod vod = new Vod();
        vod.setVodId(ids.get(0));
        vod.setVodPic(img);
        vod.setVodYear(year);
        vod.setVodName(name);
        vod.setVodArea(area);
        vod.setVodActor(actor);
        vod.setVodRemarks(remarks);
        vod.setVodContent(content);
        vod.setVodDirector(director);
        vod.setTypeName(type);

        Map<String, String> sites = new LinkedHashMap<>();
        Elements sources = doc.select("ul.tab_control > li");
        Elements sourceList = doc.select("ul.play_list");
        for (int i = 0; i < sources.size(); i++) {
            Element source = sources.get(i);
            String sourceName = source.text();
            Elements playList = sourceList.get(i).select("a");
            List<String> vodItems = new ArrayList<>();
            for (int j = 0; j < playList.size(); j++) {
                Element e = playList.get(j);
                vodItems.add(e.text() + "$" + e.attr("href"));
            }
            if (vodItems.size() > 0) {
                sites.put(sourceName, TextUtils.join("#", vodItems));
            }
        }
        if (sites.size() > 0) {
            vod.setVodPlayFrom(TextUtils.join("$$$", sites.keySet()));
            vod.setVodPlayUrl(TextUtils.join("$$$", sites.values()));
        }
//        {"jx":0,"list":[{"type_name":"科幻,机战","vod_actor":"铃木崚汰,阿座上洋平,会泽纱弥,宫本侑芽,加隈亚衣,前田佳织里,藤井雪代,森奈奈子,三宅健太,志村知幸","vod_area":"日本","vod_content":"关注「插兜的干货仓库」: ——连系他们的是「勇气」。　在人型装甲兵器『Titano Stride，通称TS』盛行的时代，各国军队集结在「夏威夷欧胡","vod_director":"大张正己","vod_id":"4587.html","vod_name":"勇气爆发","vod_pic":"http://p.qpic.cn/music_cover/PiajxSqBRaEKia1eoHwIziaXl4FJfbO3HJyBwGxjpfC1aOgFvoicLKq5Hw/600","vod_play_from":"线路1$$$线路2","vod_play_url":"1$/p/4587-1-1.html#2$/p/4587-1-2.html#3$/p/4587-1-3.html#4$/p/4587-1-4.html#5$/p/4587-1-5.html#6$/p/4587-1-6.html#7$/p/4587-1-7.html#8$/p/4587-1-8.html#9$/p/4587-1-9.html#10$/p/4587-1-10.html$$$1$/p/4587-2-1.html#2$/p/4587-2-2.html#3$/p/4587-2-3.html#4$/p/4587-2-4.html#5$/p/4587-2-5.html#6$/p/4587-2-6.html#7$/p/4587-2-7.html","vod_remarks":"第10话","vod_year":"2024"}],"parse":0}
        return Result.string(vod);
    }

    @Override
    public String searchContent(String key, boolean quick) {
        List<Vod> list = new ArrayList<>();
        String target = siteUrl.concat("/s----------.html?wd=").concat(key);
        Document doc = Jsoup.parse(OkHttp.string(target, getHeaders()));
        for (Element element : doc.select("div.item")) {
            String img = element.select("a.cover").attr("data-bg");
            String url = element.select("a.title").attr("href");
            String name = element.select("a.title").text();
            String remark = element.select("span.desc").text();
            String id = url.split("/")[2];
            list.add(new Vod(id, name, img, remark));
        }
        return Result.string(list);
    }

    @Override
    public String playerContent(String flag, String id, List<String> vipFlags) {
        Document doc = Jsoup.parse(OkHttp.string(siteUrl.concat(id), getHeaders()));
        String url = doc.select("iframe").attr("src");
        return Result.get().url(url).parse().header(getHeaders()).string();
    }
}
