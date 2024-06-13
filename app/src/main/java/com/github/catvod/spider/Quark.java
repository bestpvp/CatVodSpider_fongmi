package com.github.catvod.spider;


import android.annotation.SuppressLint;
import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.catvod.api.AliYun;
import com.github.catvod.bean.Result;
import com.github.catvod.bean.Sub;
import com.github.catvod.bean.Vod;
import com.github.catvod.crawler.Spider;
import com.github.catvod.net.OkHttp;
import com.github.catvod.net.OkResult;
import com.github.catvod.utils.ProxyVideo;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Response;

public class Quark extends Spider {
public static Map<String, JSONObject> shareTokenCache = new HashMap();
    public static Map<String, String> saveFileIdCaches = new HashMap();
    public static Map<String, Object> quarkTranscodingCache = new HashMap();
    public static Map<String, JSONObject> quarkDownloadingCache = new HashMap();
    public static final String pr = "pr=ucpro&fr=pc";
    public static final String saveDirName = "CatVodOpen";
    public static final String cookie = "_UP_A4A_11_=wb963112a24a479cbfae7672d7093072; tfstk=faSZt7TKIlEZ6Ugq7Bt2TbuhkkY9DhV5SiOXntXDCCAMCh6c8t6zBK11nI72tsetfhVtmIXR9FOGGhG406-MkNOX1I-VH_7aNuZ5BOKvj7N7Vu-HjcKviotDkvKh5nV7NkZ5BOKvmZt9FV71KBpZScfmmJ8HhBmMncvmtJJvtIxDodDnKK9Wnmvi6sODNDJyS5h0rlwYKLTwZO8-m0SEVFRlIBoDpMJaiQXMTmmXsmJlV9W__0BBXG1yF1Znb_WceZYF0XVyMMWhjZXK_S-Nd1IyjgPZfpte_Uvhq-gJOMR1iG83F08XxgTkjwaL1EtwnhYRJ-36iZXP4h1_9mA1z9fePMGIV3bRTi8kqgriMpYatiQZiqYicpR7LJvh6jGk7RSz7q3vWwpeNRetoqc-jmgNc8uxkFBwLQwTU; _UP_D_=pc; __pus=e657cf8682ee39c12c7df1a16f9b050dAARXW5Uh3dnw6z1PpG0J6YZQELsnL5rL/4cW2Qu3xa8ijgZtHc5E9eHwTrMjfrMRIQpIhweHkOLQOTZyXIYZOQw8; __kp=edcc7360-23d5-11ef-9178-cba46a2863fe; __kps=AARZ+A5Hj/etCmPFm2p4+5r1; __ktd=DcLzDmVfE6OjWh9P9zGzfQ==; __uid=AARZ+A5Hj/etCmPFm2p4+5r1; __puus=ad2bae0cedce9ca1a15c395422797d6dAASM4aoI3hEk7bHc8+Bw+jT2OrIqukUvwONXxCKAwnSgbpu0afWju/Kgg5xYq0JRRGOHr4q7V60pePXtFPMGWqbigeefiG/VH94VUuxRclu0uQWkQAHgDdwTPCvZZJHX/13TksKBRszGzAaSC3shXhb0GGU6atryF7Ncd4bMzjXni5DkhOYBCY832EVAONb7O8fnA4fQQHfjORTL2BHezYY1";
    public static Object saveDirId = null;
    public static Quark instance = null;

    public static String apiUrl = "https://drive.quark.cn/1/clouddrive/";

    @Override
    public void init(Context context, String extend) {

    }

    @SuppressLint("NotConstructor")
    public Quark Quark() {
        if (Objects.isNull(instance)) {
            instance = new Quark();
        }
        return instance;
    }

    public static Quark get() {
        if (Objects.isNull(instance)) {
            instance = new Quark();
        }
        return instance;
    }
    public Map<String, String> getHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) quark-cloud-drive/2.5.20 Chrome/100.0.4896.160 Electron/18.3.5.4-b478491100 Safari/537.36 Channel/pckk_other_ch");
        headers.put("Referer", "http://pan.quark.cn");
        headers.put("Cookie", cookie);
        return headers;
    }

    @Override
    public String detailContent(List<String> ids) throws Exception {
        JSONObject shareDataByUrl = this.getShareDataByUrl(ids.get(0));
        Vod vod = new Vod();
        vod.setVodId(ids.get(0));
        vod.setVodPlayFrom(shareDataByUrl.getString("froms"));
        vod.setVodPlayUrl(shareDataByUrl.getString("urls"));
        return Result.string(vod);
    }

    @Override
    public String playerContent(String flag, String id, List<String> vipFlags)  throws Exception {
        String[] ids = id.split("\\*");
        if (flag.startsWith("Quark-")) {
            JSONArray liveTranscoding = this.getLiveTranscoding(ids[0], ids[1], ids[2], ids[3]);
            JSONArray transcoding = new JSONArray();
            for (int i = 0; i < liveTranscoding.size(); i++) {
                JSONObject jsonObject = liveTranscoding.getJSONObject(i);
                if (jsonObject.getBoolean("accessable")) {
                    transcoding.add(jsonObject);
                }
            }
            quarkTranscodingCache.put(ids[2], transcoding);
            List urls = new ArrayList();
            String[] p = new String[]{"2160P","1440P","1080P","720P","480P","360P"};
            String[] arr = new String[]{"4k","2k","super","high","low","normal"};
//            urls.add("Proxy");
//            urls.add(String.format(Proxy.getUrl() + "?do=quark&what=%s&flag=%s&shareId=%s&fileId=%s&end=%s", "src", "down", ids[0], URLEncoder.encode(ids[1]) + "*" + ids[2] + "*" + ids[3], ".bin"));
            urls.add("SRC");
//            urls.add(String.format(Proxy.getUrl() + "?do=quark&what=%s&flag=%s&shareId=%s&fileId=%s&end=%s", "src", "redirect", ids[0], URLEncoder.encode(ids[1]) + "*" + ids[2] + "*" + ids[3], ".bin"));
            urls.add(getDownloadUrl("src", "redirect",ids[0], URLEncoder.encode(ids[1]) + "*" + ids[2] + "*" + ids[3]));
            String subt = "";
            if (StringUtils.isNotEmpty(ids[3])) {
               subt = String.format(Proxy.getUrl() + "?do=quark&what=%s&flag=%s&shareId=%s&fileId=%s&end=%s", "src", "subt", ids[0], URLEncoder.encode(ids[1]) + "*" + (ids.length > 4 ? ids[4] : "") + "*" + (ids.length > 5 ? ids[5] : ""), ".bin");
            }
            for (int i = 0; i < transcoding.size(); i++) {
                JSONObject jsonObject = transcoding.getJSONObject(i);
                int idx = findElementIndex(arr, jsonObject.getString("resolution"));
                urls.add(p[idx]);
//                urls.add(String.format(Proxy.getUrl() + "?do=quark&what=%s&flag=%s&shareId=%s&fileId=%s&end=%s", "trans", jsonObject.getString("resolution").toLowerCase(), ids[0], URLEncoder.encode(ids[1]) + "*" + ids[2] + "*" + ids[3], ".mp4"));
                urls.add(getDownloadUrl("trans", jsonObject.getString("resolution").toLowerCase(),ids[0], URLEncoder.encode(ids[1]) + "*" + ids[2] + "*" + ids[3]));

            }
            return Result.get().url(urls).m3u8().subs(Arrays.asList(new Sub().url(subt))).header(getHeader()).string();
        }
        return "";
    }

    public String getDownloadUrl(String what, String flag, String shareId, String fileId) throws Exception {
        String downUrl = "";
        String[] ids = fileId.split("\\*");
        if (StringUtils.equals("trans", what)) {
                JSONArray liveTranscoding = this.getLiveTranscoding(shareId, URLDecoder.decode(ids[0]), ids[1], ids[2]);
                JSONArray transcoding = new JSONArray();
                for (int i = 0; i < liveTranscoding.size(); i++) {
                    JSONObject jsonObject = liveTranscoding.getJSONObject(i);
                    if (jsonObject.getBoolean("accessable")) {
                        transcoding.add(jsonObject);
                        if (StringUtils.equals(flag, jsonObject.getString("resolution").toLowerCase())) {
                            downUrl = jsonObject.getJSONObject("video_info").getString("url");
                        }
                    }
                }
            // 重定向到downUrl
            return downUrl;
        } else {
            if (Objects.isNull(quarkDownloadingCache.get(ids[1]))) {
                JSONObject down = this.getDownload(shareId, URLDecoder.decode(ids[0]), ids[1], ids[2], StringUtils.equals("down", flag));
                if (Objects.nonNull(down)) {
                    quarkDownloadingCache.put(ids[1], down);
                }
            }
            downUrl = quarkDownloadingCache.get(ids[1]).getString("download_url");
            if (StringUtils.equals("redirect", flag)) {
                // 重定向到downUrl
                return downUrl;
            }
        }
        return downUrl;
    }

    public int findElementIndex(String[] arr, String resolution) {
        for(int i = 0; i < arr.length; i++) {
            if (StringUtils.equals(resolution, arr[i])) {
                return i;
            }
        }
        return -1;
    }

    public JSONObject getShareDataByUrl(String shareUrl) throws Exception {
        String shareData = this.getShareData(shareUrl);
        List videos = this.getFilesByShareUrl(shareData);
        JSONArray froms = new JSONArray();
        JSONArray urls = new JSONArray();
        if (videos.size() > 0) {
            froms.add("Quark-" + shareData);
            StringBuilder url = new StringBuilder();
            for(int i = 0; i < videos.size(); i++) {
                Object o = videos.get(i);
                JSONObject v = (JSONObject) o;
                StringBuilder sb = new StringBuilder();
                sb.append(shareData).append("*").append(v.getString("stoken")).append("*").append(v.getString("fid")).append("*")
                        .append(v.getString("share_fid_token")).append("*").append(v.containsKey("subtitle")?v.getJSONObject("subtitle").getString("fid"): "").append("*")
                        .append(v.containsKey("subtitle")?v.getJSONObject("subtitle").getString("share_fid_token"): "");
                String fileName = formatPlayUrl("", v.getString("file_name")) + "$" + sb.toString();
                url.append(fileName).append(i == videos.size() - 1 ? "" : "#");
            }
            urls.add(url.toString());
        }
        JSONObject ret = new JSONObject();
        ret.put("shareUrl", shareUrl);
        ret.put("froms", froms.getString(0));
        ret.put("urls", urls.getString(0));
        return ret;
    }

    public static String formatPlayUrl(String src, String name) {
        if (Objects.equals(src.trim(), name.trim())) {
            return name;
        }
        return name
                .trim()
                .replace(src, "")
                .replace("<", "")
                .replace(">", "")
                .replace("《", "")
                .replace("》", "")
                .replace("$", " ")
                .replace("#", " ")
                .trim();
    }

    public String getShareData(String url) {
        Pattern pattern = Pattern.compile("https:\\/\\/pan\\.quark\\.cn\\/s\\/([^\\\\|#/]+)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }
        // folderId = "0"
    }

    public List getFilesByShareUrl(String shareId) throws Exception {
        getShareToken(shareId);
        if (StringUtils.isEmpty(String.valueOf(shareTokenCache.get(shareId)))) {
            return new ArrayList();
        }
        List videos = new ArrayList();
        List subtitles = new ArrayList();
        listFile(shareId, "0", "1", videos, subtitles);
        if (subtitles.size() > 0 ) {
            for (int i = 0; i < videos.size(); i++) {
                JSONObject json = (JSONObject)videos.get(i);
                Results matchSubtitle = findBestLCS(json, subtitles);
                if (Objects.nonNull(matchSubtitle.bestMatch)) {
                    json.put("subtitle", matchSubtitle.bestMatch.getString("target"));
                }
            }

        }
        return videos;
    }

    public static Ret lcs(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return new Ret(0, "", 0);
        }

        int str1Length = str1.length();
        int str2Length = str2.length();
        int[][] num = new int[str1Length][str2Length];
        int maxlen = 0;
        int lastSubsBegin = 0;
        StringBuilder sequence = new StringBuilder(); // 添加 StringBuilder 实例

        for (int i = 0; i < str1Length; i++) {
            Arrays.fill(num[i], 0);
        }

        for (int i = 0; i < str1Length; i++) {
            for (int j = 0; j < str2Length; j++) {
                if (str1.charAt(i) != str2.charAt(j)) {
                    num[i][j] = 0;
                } else {
                    if (i == 0 || j == 0) {
                        num[i][j] = 1;
                    } else {
                        num[i][j] = 1 + num[i - 1][j - 1];
                    }

                    if (num[i][j] > maxlen) {
                        maxlen = num[i][j];
                        int thisSubsBegin = i - num[i][j] + 1;
                        if (lastSubsBegin == thisSubsBegin) {
                            sequence.append(str1.charAt(i));
                        } else {
                            lastSubsBegin = thisSubsBegin;
                            sequence.setLength(0);
                            sequence.append(str1.substring(lastSubsBegin, i + 1 - lastSubsBegin));
                        }
                    }
                }
            }
        }

        return new Ret(maxlen, sequence.toString(), lastSubsBegin);
    }
    public static Results findBestLCS(JSONObject mainItem, List<JSONObject> targetItems) {
        List<JSONObject> results = new ArrayList<>();
        JSONObject result = new JSONObject();
        int bestMatchIndex = 0;
        int bestLcsLength = 0;

        for (int i = 0; i < targetItems.size(); i++) {
            Ret currentLCS = lcs(mainItem.getString("name"), targetItems.get(i).getString("name"));
            result.put("target", targetItems.get(i));
            result.put("lcs", currentLCS);
            results.add(result);
            if (currentLCS.length > ((Ret)results.get(bestLcsLength).get("lcs")).length) {
                bestMatchIndex = i;
            }
        }
        JSONObject bestMatch = results.get(bestMatchIndex);

        Results output = new Results();
        output.allLCS = results;
        output.bestMatch = bestMatch;
        output.bestMatchIndex = bestMatchIndex;

        return output;
    }
    public static class Results {
        List<JSONObject> allLCS;
        JSONObject bestMatch;
        int bestMatchIndex;
    }
    private static class Ret {
        int length;
        String sequence;
        int offset;

        Ret(int length, String sequence, int offset) {
            this.length = length;
            this.sequence = sequence;
            this.offset = offset;
        }
    }
    public List listFile(String shareId, String folderId, String page, List videos, List subtitles) throws IOException {
        if (StringUtils.isBlank(folderId)) {
            folderId = "0";
        }
        if (StringUtils.isBlank(page)) {
            page = "1";
        }
        String stoken = shareTokenCache.get(shareId).getString("stoken");
        String encode = URLEncoder.encode(stoken);
        String url = "share/sharepage/detail?" + pr +"&pwd_id="+shareId+"&stoken="+encode+"&pdir_fid="+folderId+"&force=0&_page="+page+"&_size=200&_sort=file_type:asc,file_name:asc";
        String get = this.api(url, null, "GET");
        if (StringUtils.isEmpty(get)) {
            return new ArrayList<>();
        }
        JSONObject jsonObject = JSONObject.parseObject(get);
        if (!jsonObject.containsKey("data")) {
            return new ArrayList();
        }
        if (jsonObject.getJSONObject("data").containsKey("list")) {
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray items = data.getJSONArray("list");
            if (Objects.isNull(items) || items.size() == 0) {
                return new ArrayList<>();
            }
            List subDir = new ArrayList();
            for (int i = 0; i < items.size(); i++) {
                JSONObject json = items.getJSONObject(i);
                Boolean dir = json.getBoolean("dir");
                Boolean file = json.getBoolean("file");
                String objCategory = json.getString("obj_category");
                String type = json.getString("type");
                Long size = json.getLong("size");
                if (dir) {
                    subDir.add(json);
                } else if (file && "video".equals(objCategory)) {
                    if (size < 1024 * 1025 * 5)  {
                        continue;
                    }
                    json.put("stoken", shareTokenCache.get(shareId).getString("stoken"));
                    videos.add(json);
                } else if ("file".equals(type)) {
                    String[] exts = {".srt", ".ass", ".scc", ".stl", ".ttml"};
                    String fileName = json.getString("file_name");
                    for (String ext : exts) {
                        if (fileName.endsWith(ext)) {
                            subtitles.add(json);
                        }
                    }
                }
            }
            // 翻页
            int total = jsonObject.getJSONObject("metadata").getInteger("_total");
            if (Integer.parseInt(page) < Math.ceil((double) total / 200)) {
                List nextItems = listFile(shareId, folderId, String.valueOf(Integer.parseInt(page) + 1), videos, subtitles);
                items.addAll(nextItems);
                JSONObject json = (JSONObject) subDir.get(0);
            }
            for(int i = 0; i < subDir.size(); i++) {
                JSONObject json = (JSONObject) subDir.get(i);
                String fid = json.getString("fid");
                items.addAll(listFile(shareId, fid, page, videos, subtitles));
            }
            return items;
        }
        return new ArrayList();
    }

    public void getShareToken(String shareId) throws Exception {
        Object o = shareTokenCache.get(shareId);
        if (Objects.isNull(o)) {
            JSONObject params = new JSONObject();
            params.put("pwd_id", shareId);
            params.put("passcode", "");
            String ret = this.api("share/sharepage/token?" + pr, params, "POST");
            if (StringUtils.isNotBlank(ret)) {
                JSONObject jsonObject = JSONObject.parseObject(ret);
                JSONObject data = jsonObject.getJSONObject("data");
                if (data.containsKey("stoken")) {
                    shareTokenCache.put(shareId, data);
                }
            }
        }
    }

    public String api(String url, JSONObject params, String method) throws IOException {
        Map<String, String> headers = getHeader();
        if (StringUtils.isEmpty(method)) {
            method = "POST";
        }
        if (StringUtils.equalsIgnoreCase(method, "POST")) {
            OkResult result = OkHttp.post(apiUrl + url, params.toString(),headers);
            Map<String, List<String>> resp = result.getResp();
            List<String> strings = resp.get("set-cookie");
            if (Objects.nonNull(strings) && strings.size() > 0 ) {

            }
            if (result.getCode() == 429) {
                return api(url, params, method);
            }
            return result.getBody();
        } else {
            Response response = OkHttp.newCall(apiUrl + url, headers);
            if (response.code() == 429) {
                return api(url, params, method);
            }
            return response.body().string();
        }
    }

    public JSONArray getLiveTranscoding(String shareId, String stoken, String fileId, String fileToken) throws Exception {
        if (Objects.isNull(saveFileIdCaches.get(fileId))) {
            String saveField = this.save(shareId, stoken, fileId, fileToken, true);
            if (StringUtils.isEmpty(saveField)) {
                return null;
            }
            saveFileIdCaches.put(fileId, saveField);
        }
        JSONObject params = new JSONObject();
        params.put("fid", saveFileIdCaches.get(fileId));
        params.put("resolutions", "normal,low,high,super,2k,4k");
        params.put("supports", "fmp4");
        String transcoding = this.api("file/v2/play?" + pr, params, "POST");
        if (StringUtils.isNotBlank(transcoding)) {
            JSONObject jsonObject = JSONObject.parseObject(transcoding);
            JSONObject data = jsonObject.getJSONObject("data");
            if (data.containsKey("video_list")) {
                return data.getJSONArray("video_list");
            }
        }
        return null;
    }
    public String save(String shareId, String stoken, String fileId, String fileToken, Boolean clean) throws Exception {
        this.createSaveDir(clean);
        if (clean) {
            saveFileIdCaches = new HashMap<>();
        }
        if (Objects.isNull(saveDirId)) {
            return null;
        }
        if (StringUtils.isEmpty(stoken)) {
            this.getShareToken(shareId);
            if (Objects.isNull(shareTokenCache.get(shareId))) {
                return null;
            }
        }
        JSONObject params = new JSONObject();
        params.put("fid_list", Arrays.asList(fileId));
        params.put("fid_token_list", Arrays.asList(fileToken));
        params.put("to_pdir_fid", saveDirId);
        params.put("pwd_id", shareId);
        params.put("stoken", Objects.nonNull(stoken)? stoken: shareTokenCache.get(shareId).getString("stoken"));
        params.put("pdir_fid", "0");
        params.put("scene", "link");
        String saveResult = this.api("share/sharepage/save?" + pr, params, "POST");
        JSONObject jsonObject = JSONObject.parseObject(saveResult);
        JSONObject data = jsonObject.getJSONObject("data");
        if (Objects.nonNull(data) && data.containsKey("task_id")) {
            int retry = 0;
            while (true) {
                String taskResult = this.api("task?" + pr + "&task_id=" + data.getString("task_id") + "&retry_index=" + retry, null, "GET");
                JSONObject dataTaskResult = JSONObject.parseObject(taskResult);
                if (Objects.nonNull(dataTaskResult)) {
                    JSONArray jsonArray = dataTaskResult.getJSONObject("data").getJSONObject("save_as").getJSONArray("save_as_top_fids");
                    if (Objects.nonNull(jsonArray) && jsonArray.size() > 0 ) {
                        return jsonArray.getString(0);
                    }
                }

                retry ++;
                if (retry > 5) {
                    break;
                }
                Thread.sleep(1000);
            }
        }
        return "";
    }

    public void createSaveDir(Boolean clean) throws IOException {
        if (!Objects.isNull(saveDirId)) {
            if (clean) {
                clearSaveDir();
            }
            return;
        }
        String listData = this.api("file/sort?" + pr + "&pdir_fid=0&_page=1&_size=200&_sort=file_type:asc,updated_at:desc", null, "GET");
        JSONObject jsonObject = JSONObject.parseObject(listData);
        if (!jsonObject.containsKey("data")) {
            return ;
        }
        if (jsonObject.getJSONObject("data").containsKey("list")) {
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray items = data.getJSONArray("list");
            for (int i = 0; i < items.size(); i++) {
                JSONObject item = items.getJSONObject(i);
                if (Objects.equals(item.getString("file_name"), saveDirName)) {
                    saveDirId = item.getString("fid");
                    this.createSaveDir();
                    break;
                }
            }
        }
        if (Objects.isNull(saveDirId)) {
            JSONObject params = new JSONObject();
            params.put("pdir_fid", "0");
            params.put("file_name", saveDirName);
            params.put("dir_path", "");
            params.put("dir_init_lock", false);
            String create = this.api("file?" + pr, params, "POST");
            System.out.println(create);
            if (Objects.nonNull(create)) {
                JSONObject datas = JSONObject.parseObject(create);
                if (!datas.containsKey("data")) {
                    return ;
                }
                if (datas.getJSONObject("data").containsKey("fid")) {
                    JSONObject data = datas.getJSONObject("data");
                    String fid = data.getString("fid");
                    saveDirId = fid;
                }
            }
        }

    }

    public void createSaveDir() throws IOException {
        String listData = this.api("file/sort?"+pr+"&pdir_fid="+saveDirId+"&_page=1&_size=200&_sort=file_type:asc,updated_at:desc", null, "GET");
        JSONObject jsonObject = JSONObject.parseObject(listData);
        if (!jsonObject.containsKey("data")) {
            return ;
        }
        if (jsonObject.getJSONObject("data").containsKey("list")) {
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray items = data.getJSONArray("list");
            if (items.size() > 0) {
                JSONArray filelist = new JSONArray();
                for(int i = 0; i < items.size(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    filelist.add(item.getString("fid"));
                }
                JSONObject params = new JSONObject();
                params.put("action_type", 2);
                params.put("filelist", filelist);
                params.put("exclude_fids", new JSONArray());
                String del = this.api("file/delete?" + pr, params, "POST");
                System.out.println(del);
            }
        }
    }

    public void clearSaveDir() {
    }

    public Object[] proxy(Map<String, String> params) throws Exception {
        String site = params.get("do"); // quark
        String what = params.get("what");
        String flag = params.get("flag");
        String shareId = params.get("shareId");
        String fileId = params.get("fileId");
        String end = params.get("end");
        if (StringUtils.equals("quark", site)) {
            String downUrl = "";
            String[] ids = fileId.split("\\*");
            if (StringUtils.equals("trans", what)) {
                if (Objects.isNull(quarkTranscodingCache.get(ids[1]))) {
                    //                     quarkTranscodingCache[ids[1]] = (await Quark.getLiveTranscoding(shareId, decodeURIComponent(ids[0]), ids[1], ids[2])).filter((t) => t.accessable);
                    JSONArray liveTranscoding = new Quark().getLiveTranscoding(shareId, URLDecoder.decode(ids[0]), ids[1], ids[2]);
                    JSONArray transcoding = new JSONArray();
                    for (int i = 0; i < liveTranscoding.size(); i++) {
                        JSONObject jsonObject = liveTranscoding.getJSONObject(i);
                        if (jsonObject.getBoolean("accessable")) {
                            transcoding.add(jsonObject);
                            if (StringUtils.isNotEmpty(downUrl) && StringUtils.equals(flag, jsonObject.getString("resolution").toLowerCase())) {
                                downUrl = jsonObject.getJSONObject("video_info").getString("url");
                            }
                        }
                    }
                    quarkTranscodingCache.put(ids[1], transcoding);
                }
                    // 重定向到downUrl
                return new Object[]{"302", "Location", downUrl};
            } else {
                if (Objects.isNull(quarkDownloadingCache.get(ids[1]))) {
                    JSONObject down = this.getDownload(shareId, URLDecoder.decode(ids[0]), ids[1], ids[2], StringUtils.equals("down", flag));
                    if (Objects.nonNull(down)) {
                        quarkDownloadingCache.put(ids[1], down);
                    }
                }
                downUrl = quarkDownloadingCache.get(ids[1]).getString("download_url");
                if (StringUtils.equals("redirect", flag)) {
                    // 重定向到downUrl
                    Map<String, String> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
                    for (String key : params.keySet()) {
                        if (key.equals("referer") || key.equals("icy-metadata") || key.equals("range") || key.equals("connection") || key.equals("accept-encoding") || key.equals("user-agent")) {
                            headers.put(key, params.get(key));
                        }
                    }
                    return new Object[]{"302", "Location", downUrl};
                }
            }
//            this.chunkStream();
            return new Object[]{"302", "Location", downUrl};
        }
        return null;
    }

    public JSONObject getDownload(String shareId, String stoken, String fileId, String fileToken, Boolean clean) throws Exception {
        if (Objects.isNull(saveFileIdCaches.get(fileId))) {
            String saveField = this.save(shareId, stoken, fileId, fileToken, clean);
            if (StringUtils.isEmpty(saveField)) {
                return null;
            }
            saveFileIdCaches.put(fileId, saveField);
        }
        JSONObject params = new JSONObject();
        params.put("fids", Arrays.asList(saveFileIdCaches.get(fileId)));
        String post = this.api("file/download?" + pr, params, "POST");
        JSONObject down = JSONObject.parseObject(post);
        if (down.containsKey("data")) {
            return down.getJSONArray("data").getJSONObject(0);
        }
        return null;
    }
}
