package cn.itcast.zz.zhbj20.bean;

/**
 * Created by wangdh on 2016/11/29.
 *
 * 使用Gson 需要依据json -- 封装 -- 对应的javabean
 *
 * javabean的字段名 --- 必须与 -- json的key一致
 */
public class NewsBean {
    public int id;//新闻类型id
    public String title;//新闻类型名

    @Override
    public String toString() {
        return "NewsType [id=" + id + ", title=" + title + "]";
    }

    public NewsBean(int id, String title) {
        super();
        this.id = id;
        this.title = title;
    }

    public NewsBean() {
        super();
    }
}
