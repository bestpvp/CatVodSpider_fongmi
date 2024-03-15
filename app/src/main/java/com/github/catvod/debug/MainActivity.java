package com.github.catvod.debug;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.github.catvod.R;
import com.github.catvod.crawler.Spider;
import com.github.catvod.spider.AList;
import com.github.catvod.spider.Ali;
import com.github.catvod.spider.Dm84;
import com.github.catvod.spider.Init;
import com.github.catvod.spider.Push;
import com.github.catvod.spider.Wogg;
import com.github.catvod.spider.Douban;
import com.github.catvod.spider.Live2Vod;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {

    private ExecutorService executor;
    private Spider spider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button homeContent = findViewById(R.id.homeContent);
        Button homeVideoContent = findViewById(R.id.homeVideoContent);
        Button categoryContent = findViewById(R.id.categoryContent);
        Button detailContent = findViewById(R.id.detailContent);
        Button playerContent = findViewById(R.id.playerContent);
        Button searchContent = findViewById(R.id.searchContent);
        homeContent.setOnClickListener(view -> executor.execute(this::homeContent));
        homeVideoContent.setOnClickListener(view -> executor.execute(this::homeVideoContent));
        categoryContent.setOnClickListener(view -> executor.execute(this::categoryContent));
        detailContent.setOnClickListener(view -> executor.execute(this::detailContent));
        playerContent.setOnClickListener(view -> executor.execute(this::playerContent));
        searchContent.setOnClickListener(view -> executor.execute(this::searchContent));
        Logger.addLogAdapter(new AndroidLogAdapter());
        executor = Executors.newCachedThreadPool();
        executor.execute(this::initSpider);
    }

    private void initSpider() {
        try {
            Init.init(getApplicationContext());
            //豆瓣
//            spider = new Douban();
//            spider.init(this, "https://raw.githubusercontent.com/zhixc/CatVodTVSpider/main/other/json/douban.json");

            //Live2Vod
//            spider = new Live2Vod();
//            spider.init(this, "南风$https://agit.ai/Yoursmile7/TVBox/raw/branch/master/live.txt#饭太硬$https://agit.ai/fantaiying/0/raw/branch/main/tvlive.txt&&&https://img1.dd.ci/file/08b8a048adf5d333c6030.png");

            //Push
//            spider = new Push();
//            spider.init(this, "https://www.aliyundrive.com/s/ZuJK794e1jm");

            spider = new Dm84();
            spider.init(this, "");

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void homeContent() {
        try {
            Logger.t("homeContent").d(spider.homeContent(true));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void homeVideoContent() {
        try {
            Logger.t("homeVideoContent").d(spider.homeVideoContent());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void categoryContent() {
        try {
            //douban
//            String tid = "hot_gaia";
            //Live2Vod
//            String tid = "{\"circuit\":\"\",\"pic\":\"https://cdn.jsdelivr.net/gh/zhixc/CatVodTVSpider@main/other/pic/live.png\",\"url\":\"https://agit.ai/fantaiying/0/raw/branch/main/tvlive.txt\",\"group\":\"1\"}";
            //Dm84
            String tid = "2";


            Logger.t("categoryContent").d(spider.categoryContent(tid, "1", true, new HashMap<>()));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void detailContent() {
        try {
            //Live2Vod
//            String s = "{\"vod_play_url\":\"CCTV1$https://cntv.sbs/live?auth=230601&id=cctv1\",\"pic\":\"https://live.fanmingming.com/tv/CCTV1.png\"}";
//            Logger.t("detailContent").d(spider.detailContent(Arrays.asList(s)));
//            Logger.t("detailContent").d(spider.detailContent(Arrays.asList("2121173431")));

            //Dm84
            String s = "4587.html";
            Logger.t("detailContent").d(spider.detailContent(Arrays.asList(s)));

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void playerContent() {
        try {
            String flag = "test";
            String id = "https://cntv.sbs/live?auth=230601&id=cctv1";
            Logger.t("playerContent").d(spider.playerContent(flag, id, new ArrayList<>()));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void searchContent() {
        try {
            Logger.t("searchContent").d(spider.searchContent("我的人间烟火", false));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}