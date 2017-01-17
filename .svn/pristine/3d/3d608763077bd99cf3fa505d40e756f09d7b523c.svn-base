package cn.itcast.zz.zhbj20.bean;

import java.util.List;

/**
 * Created by wangdh on 2016/11/30.
 * 新闻子界面 对应的 jvabean
 */
public class NewsItemBean {
    //新闻列表数据
    public List<News> newss;
    public class News{
        /**
         *    "id": 239,
         "listimage": "/images/10007/10007-9.png",
         "newstypeid": 10007,
         "pubdate": "2014-04-08 14:58",
         "title": "北京",
         "url": "/images/10007/724D6A55496A11726628.html"
         */
        public int id;  //新闻id
        public String listimage; //图片url
        public int newstypeid; //新闻类型id
        public String pubdate;//发布日期
        public String title;//标题
        public String url;//详情url -- html网页

        public News(int id, String listimage, int newstypeid, String pubdate, String title, String url) {
            this.id = id;
            this.listimage = listimage;
            this.newstypeid = newstypeid;
            this.pubdate = pubdate;
            this.title = title;
            this.url = url;
        }

        public News() {
            super();
        }

        @Override
        public String toString() {
            return "News{" +
                    "id=" + id +
                    ", listimage='" + listimage + '\'' +
                    ", newstypeid=" + newstypeid +
                    ", pubdate='" + pubdate + '\'' +
                    ", title='" + title + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    //顶部轮播图数据
    public List<TopNews> topNewss;
    public class TopNews{
        public String title;  //顶部轮播图 标题
        public String topimage; // 图片url

        @Override
        public String toString() {
            return "TopNews{" +
                    "title='" + title + '\'' +
                    ", topimage='" + topimage + '\'' +
                    '}';
        }
    }
}
