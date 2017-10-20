package bwie.com.shopcardetails;

import com.google.gson.Gson;

import java.util.List;

/**
 * 文 件 名: MyApplication
 * 创 建 人: 谢兴张
 * 创建日期: 2017/10/20
 * 邮   箱:
 * 博   客:
 * 修改时间：
 * 修改备注：
 */

public class Beans {

    public List<美女Bean> 美女;

    public static Beans objectFromData(String str) {

        return new Gson().fromJson(str, Beans.class);
    }

    public static class 美女Bean {
        /**
         * adtype : 0
         * boardid : dy_wemedia_bbs
         * clkNum : 0
         * danmu : 1
         * digest : 摄影：街边休息的小女人
         * docid : CNAAETBN0516EHL4
         * downTimes : 65
         * hasAD : 1
         * img : http://dingyue.nosdn.127.net/3o5qhGEnEac8KiwYp6xYSgo8YctD1t7NaVZgEeJ=Xt8EE1497859441273compressflag.jpg
         * imgType : 0
         * imgsrc : http://dingyue.nosdn.127.net/3o5qhGEnEac8KiwYp6xYSgo8YctD1t7NaVZgEeJ=Xt8EE1497859441273compressflag.jpg
         * imgsum : 0
         * picCount : 0
         * pixel : 660*891
         * program : HY
         * prompt : 成功为您推荐8条新内容
         * rank : 0
         * recNews : 0
         * recType : 0
         * refreshPrompt : 0
         * replyCount : 22
         * replyid : CNAAETBN0516EHL4
         * source : 人物艺术摄影
         * title : 摄影：街边休息的小女人
         * upTimes : 554
         */

        public int adtype;
        public String boardid;
        public int clkNum;
        public int danmu;
        public String digest;
        public String docid;
        public int downTimes;
        public int hasAD;
        public String img;
        public int imgType;
        public String imgsrc;
        public int imgsum;
        public int picCount;
        public String pixel;
        public String program;
        public String prompt;
        public int rank;
        public int recNews;
        public int recType;
        public int refreshPrompt;
        public int replyCount;
        public String replyid;
        public String source;
        public String title;
        public int upTimes;

        public static 美女Bean objectFromData(String str) {

            return new Gson().fromJson(str, 美女Bean.class);
        }
    }
}
