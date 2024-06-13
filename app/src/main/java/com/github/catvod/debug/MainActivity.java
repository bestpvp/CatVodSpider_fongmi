package com.github.catvod.debug;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.github.catvod.R;
import com.github.catvod.crawler.Spider;
import com.github.catvod.spider.Duanjuso;
import com.github.catvod.spider.Init;
import com.github.catvod.spider.QuarkShare;
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

//        // Show the dialog before setting up the layout
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Welcome to CatVod!");
//        builder.setMessage("This is a sample application for CatVod.");
//        builder.setCancelable(false); // Make the dialog non-cancelable
//        builder.setPositiveButton("OK", (dialog, which) -> {
//            // Proceed with activity setup after the dialog is dismissed
//            setContentView(R.layout.activity_main);
//            // ... (rest of the code remains the same)
//        });
//        builder.show();

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

//            spider = new Douban();
//            spider.init(this, "");

//            spider = new Kanqiu();
//            spider.init(this, "");

//            Live2Vod
//            spider = new Live2Vod();
//            spider.init(this, "./sites/码上/remote_live.json");
//            spider.init(this, "南风$https://agit.ai/Yoursmile7/TVBox/raw/branch/master/live.txt#饭太硬$https://agit.ai/fantaiying/0/raw/branch/main/tvlive.txt&&&https://img1.dd.ci/file/08b8a048adf5d333c6030.png");

            //Push
//            spider = new Push();
//            spider.init(this, "https://www.aliyundrive.com/s/ZuJK794e1jm");

//            spider = new Dm84();
//            spider.init(this, "");

//            spider = new Jianpian();
//            spider.init(this, "");

//            spider = new Ying();
//            spider.init(this, "");

//            spider = new Ysj();
//            spider.init(this, "");

//            spider = new FreeOK();
//            spider.init(this, "");

//            spider = new SixV();
//            spider.init(this, "https://www.66ss.org/");

//            String extend = "{\"siteUrl\":\"https://www.rarbt.fun\",\"jxToken\":\"tm://tm/jxToken.txt\",\"enableJX\":false}";
//            spider = new Rarbt();
//            spider.init(this,extend);

//            spider = new DyGang();
//            spider.init(this,"");

//            spider = new Xunlei8();
//            spider.init(this,"");

//            String extend = "{\"siteUrl\":\"https://www.voflix.vip\",\"jxToken\":\"tm://tm/jxToken.txt\",\"enableJX\":false}";
//            spider = new Voflix();
//            spider.init(this, extend);

//            spider = new QuarkShare();
//            spider.init(this,"");

            spider = new Duanjuso();
            spider.init(this,"");

//            spider = new Star();
//            spider.init(this, "");

//            spider = new Kanqiu();
//            spider.init(this, "");
//
//            spider = new JustLive();
//            spider.init(this, "");


//            spider = new NiNi();
//            spider.init(this, "");

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
//            String tid = "2";

//            String tid = "1";

//            String tid = "/vodshow/1--------";

//            String tid = "/label/new.html";

//            String tid = "dongzuopian";

//            String tid = "movie";

//            String tid = "my_dianying";
            String tid = "1";
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

//            Dm84
//            String s = "4587.html";
//            Logger.t("detailContent").d(spider.detailContent(Arrays.asList(s)));

            //JIANPIAN
//            String s = "563183";

            //freeok
//            String s = "https://www.freeok.vip/vod-detail/66986.html";

//            String s = "/dongzuopian/23266.html";

//            String s = "/movie/Ua7j.html";
//            Logger.t("detailContent").d(spider.detailContent(Arrays.asList(s)));

//            String s = "/ys/20240404/54346.htm";
//            Logger.t("detailContent").d(spider.detailContent(Arrays.asList(s)));

            // Voflix
//            String s = "/detail/173629.html";
//            Logger.t("detailContent").d(spider.detailContent(Arrays.asList(s)));

            //ying
            String s = "23448.html";
            Logger.t("detailContent").d(spider.detailContent(Arrays.asList(s)));

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void playerContent() {
        try {
            // live
//            String flag = "test";
//            String id = "https://cntv.sbs/live?auth=230601&id=cctv1";

            // jianpian
//            String flag = "周处除三害";
//            String id = "tvbox-xg:ftp://a.gbl.114s.com:20320/8945/周处除三害-2024_HD国语中字.mp4";
//            Logger.t("playerContent").d(spider.playerContent(flag, id, new ArrayList<>()));

            // freeok
//            String flag = "泪之女王";
//            String id = "https://www.freeok.vip/vod-detail/66986.html";

//            String flag = "阿迪普鲁什";
//            String id = "https://www.rarbt.fun/DR/Ua7j-1-1.html";

            // Voflix
            String flag = "第1期";
            String id = "/play/94490-4-1.html";

            //ying
//            String flag = "1";
//            String id = "/vp/23448-1-3.html";

            Logger.t("playerContent").d(spider.playerContent(flag, id, new ArrayList<>()));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void searchContent() {
        try {
            Logger.t("searchContent").d(spider.searchContent("慕尼黑", false));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}