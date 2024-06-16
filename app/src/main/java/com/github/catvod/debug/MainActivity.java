package com.github.catvod.debug;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.github.catvod.R;
import com.github.catvod.crawler.Spider;
import com.github.catvod.spider.Douban;
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

            spider = new Douban();
            spider.init(this, "");

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
//            spider.init(this,"ctoken=t9IeztzRI875lwt5HAu1yfdM; b-user-id=717bb9a6-7114-08ae-48f0-9f355b94d722; grey-id=abce4637-60f4-d634-89b5-db3ec1cb5648; grey-id.sig=pJFeIoDh0Lp3zvZBDgyfFr1TX3KJpDd0wqFOvi6U6tk; _UP_A4A_11_=wb9641d22e9a44569b38e0692046eb6d; tfstk=fiEEGp4ex0V69qao_fir_IdNrNoK2mCb-uGSE82odXcndDwu78wVpYNQEbzrs7BpADfpqb2Y62G3NDOP45onyyGSObozeSW1GisbpJn-xs1fcb3Hqvm-ZgioydngR0ffGGZAIGYjqzs-2A2aIfHrEHmuZdXZUYikx4xHSCctE0cuZ2YgIYMyZBYnZhYWfJ2kbxgh9_XfV6vSC2l0LZKk4l-j8j2n_3bubvooiJcwq3cK-UTgE7fyVm4Y_uu0Gg-tA-zijvzNEFcnuyVIB7S2xXqaKkcQx1tItkegluNGE3czSr04oJf67Azgdlu_ms-rJPVLk4r51inQl-Z-okjeMfgxUSo371xUig-MwAbuXuUeZU0nBAlfQOXlM5Mnj2S37UL-SmHZG9MBrUAcpAlfQdTJy23EQj6n-; _UP_D_=pc; __wpkreporterwid_=acc8716f-a2e6-4349-a753-d362941687b5; _UP_F7E_8D_=IDN4vwi8L0GobJxPLMOGBEPwKLOVbxJPcg0RzQPI6Knpe%2FVlExDLvwWk3%2BqxkwVyhdZ%2Bc09AyraclvYdENl26pP6NZpJjHSFAga2josF9WI5KqmBMZjstSyxXmZd2p0oVFkfbqd%2FhVjlt0AbNkJqehFHs%2BQ%2FpshPrgvAKVxvsq4i6PDzvwNnqu7rSXFm5RjbW1%2FmvBLvMwKTOk2wEvbokFCfwAd1vIZUMwqsjagN0q2sE%2BlICX7X3bnSe%2BfKM3Zv2UZosU2CPO%2B1Iq86JO9nn9dRDhAqYRxbfyeN7nt0BlGuC8KDRz%2FVAlL6rVAFx8Bor8W58Q%2B8dF%2F5NYnDCuCRKgZGr%2FUOk3sLe1kCgrk%2By8w80vdxOCQMN6P6GbeZVhMFSjr6E2PYGvGzVjEvMmznaYgK6I8jY9WVzigJOC%2BMHN%2BzVjEvMmznaZXRA%2FkTlwOLBTMqB7aZ91np%2B2IQpk63KWzaL7EpoajxeN5%2FTNSk2xc%3D; __pus=b22c4e80a5700c96e66b59dddb401e26AATh1VEYVPltikyGh43LdZJ6Fr3qoZ9Lq39C3IemV3rISeciOEctM3HPfEnrOYOrv0VxBSPzVUM8BAbhZKl008uG; __kp=43076ec0-29fd-11ef-952c-e52bd6867b51; __kps=AASQV09sSYKYhxIlHATBnv+9; __ktd=MRtco4x6rqed8XmaXH94Kw==; __uid=AASQV09sSYKYhxIlHATBnv+9; __itrace_wid=492acbf2-4a65-4264-b701-2a1329df2264; __puus=22714315fb58fe3952df6e66e5238708AAQus+Fo+Ass1hVoxNIocMANbDl51iSytvtetz8hNZ3NpIO4GNWNAdDqqjkOdUTaKDki86OHz1CDr2hzgVfzqHzA7BKRwpz9W/u8mxZXo3xoIq+x8n5tvzjxMN2NaJZXYft8/ReVDalk9dGsTQR5MLF+ycKaQfkOwhezUFILwkcUk4IFRBMPfQ0/S69RZq3bnUsJFx0wLUWB8HwfEUvjQlHU");

//            spider = new Duanjuso();
//            spider.init(this,"");

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