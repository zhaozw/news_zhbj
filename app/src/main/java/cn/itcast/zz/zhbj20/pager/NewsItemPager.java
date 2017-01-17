package cn.itcast.zz.zhbj20.pager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.zz.zhbj20.activity.NewsDetailActivity;
import cn.itcast.zz.zhbj20.adapter.DefaultBaseAdapter;
import cn.itcast.zz.zhbj20.R;
import cn.itcast.zz.zhbj20.bean.NewsBean;
import cn.itcast.zz.zhbj20.bean.NewsItemBean;
import cn.itcast.zz.zhbj20.utils.NetUrl;
import cn.itcast.zz.zhbj20.utils.SpUtils;
import cn.itcast.zz.zhbj20.view.RollViewPager;
import cn.itcast.zz.zhbj20.view.XListView;

/**
 * Created by wangdh on 2016/11/30.
 * 新闻子界面
 * 传递新闻子界面所需要的数据:
 *
 *      1. 构造  (传递必须的数据)
 *      2. setXXX() 方法
 */
public class NewsItemPager extends  BasePager implements XListView.IXListViewListener {

    private static final String TAG = "NewsItemPager";
    private final ViewPager.OnPageChangeListener newsItemOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //标题
            tv_top_news_title.setText(topTitles.get(position));
            //指示点一起轮播
            for (int i=0;i<dots.size();i++){
                //当前点  红色
                if(i == position){
                    dots.get(i).setImageResource(R.drawable.dot_focus);
                }else{
                    //上一个点  白色
                    dots.get(i).setImageResource(R.drawable.dot_normal);
                }
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private final AdapterView.OnItemClickListener newsOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //position 有误差  包含 头和脚
            position = position - lv_news_item_pager.getHeaderViewsCount();
            //跳转
            Intent intent = new Intent(context,NewsDetailActivity.class);
            //传递当前新闻的url
            String newsDetailUrl = datas.get(position).url;
            intent.putExtra("newsDetailUrl",newsDetailUrl);

            context.startActivity(intent);

            //记录当前点击新闻id
            int newsId =  datas.get(position).id;
            //sp
            SpUtils.setReadNewsIds(context,newsId);
            adapter.notifyDataSetChanged();

        }
    };
    private  NewsBean newsBean;


    private int pageNum = 0;//页码

    @ViewInject(R.id.lv_news_item_pager)
    private XListView lv_news_item_pager;

    //存放viewpager的线形布局
    @ViewInject(R.id.ll_top_news_viewpager)
    private LinearLayout ll_top_news_viewpager;
    //轮播图对应的标题
    @ViewInject(R.id.tv_top_news_title)
    private TextView tv_top_news_title;

    //存放 指示点
    @ViewInject(R.id.ll_dots)
    private LinearLayout ll_dots;
    private RollViewPager rollViewPager;
    private NewsItemBean newsItemBean;

    private BitmapUtils bitmapUtils;
    private String firstPageUrl;
    private NewsItemBaseAdapter adapter;

    public NewsItemPager(Context context, NewsBean newsBean) {
        super(context);
        this.newsBean = newsBean;
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.news_item_pager,null);
        ViewUtils.inject(this,view);
        //listview的头 -- 轮播图
        View rollView = View.inflate(context,R.layout.layout_roll_view,null);
        ViewUtils.inject(this,rollView);
        //listview添加头布局
        lv_news_item_pager.addHeaderView(rollView);
        //必须设置适配器  (填充的身体)
        String[] datas = {"aaaaaaaa","bbbbbbbbbb","ccccccccc","ddddddddd","eeeeeeeee"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,
                android.R.id.text1,datas);
        lv_news_item_pager.setAdapter(adapter);
        //添加自定义viewpager
        rollViewPager = new RollViewPager(context);
        ll_top_news_viewpager.addView(rollViewPager);

        //rollViewpager添加界面改变监听
        rollViewPager.setOnPageChangeListener(newsItemOnPageChangeListener);
        //rollViewPager 添加条目点击监听 谷歌并没有给viewpager设置条目点击监听.
        rollViewPager.setOnItemClickListner(new RollViewPager.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(context,topTitles.get(position) , Toast.LENGTH_SHORT).show();
            }
        });
//        lv_news_item_pager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });

        //xlistview设置
        lv_news_item_pager.setPullLoadEnable(true);
        lv_news_item_pager.setXListViewListener(this);

        //listview添加 条目点击监听
        lv_news_item_pager.setOnItemClickListener(newsOnItemClickListener);

        return view;
    }

    @Override
    public void initData() {
//        textView.setText(newsBean.title+":"+newsBean.id);
        //http://localhost:8080/zhbjServer19/getNewsByNewsTypeId?newsTypeId=10007&pageNum=0
        firstPageUrl = NetUrl.base_url+"/getNewsByNewsTypeId?newsTypeId="+newsBean.id+"&pageNum="+pageNum;
        //1. 获取缓存
        String cacheJson = SpUtils.getCache(context, firstPageUrl);
//        2. 如果有缓存,解析缓存--展示缓存  parseJson()
        if(!TextUtils.isEmpty(cacheJson)){
//            parseJson(cacheJson);
        }
//        3. 直接请求网络
        requestPage(firstPageUrl);
    }

    /**
     * 请求每一页的数据
     * @param url : 每一页的url
     */
    private void requestPage(String url) {
        mySend(url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String jsonResult = responseInfo.result;
                Log.i(TAG, "onSuccess: 新闻子界面请求成功:"+jsonResult);
                // 4. 将数据缓存起来
//                SpUtils.setCache(context,url,jsonResult);
//              5. 解析数据 -- 展示数据  parseJson
                parseJson(jsonResult);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                Log.i(TAG, "onFailure: 新闻子界面请求失败:"+msg);
            }
        });
    }

    //顶部标题集合
    private List<String> topTitles = new ArrayList<>();
    //顶部图片url集合
    private List<String> topImageUrls = new ArrayList<>();

    /**
     * 解析json,展示数据
     * @param jsonString
     */
    private void parseJson(String jsonString) {
        //解析json
        Gson gson = new Gson();
        //jsonString:代表0页数据 ---  newsItemBean
        //jsonString:代表1页数据 ---  newsItemBean
        newsItemBean = gson.fromJson(jsonString, NewsItemBean.class);
        Log.i(TAG, "parseJson: 新闻子界面解析成功:"+ newsItemBean.toString());

        if(!isLoadingMore){ //不是加载更多 -- 第一次进入,下拉刷新
            //轮播图逻辑: 将数据传递给 ROllViewpager,自己设置适配器
            //初始化 标题集合,图片集合
            topTitles.clear();
            topImageUrls.clear();
            for (int i = 0; i< newsItemBean.topNewss.size(); i++){
                NewsItemBean.TopNews topNews = newsItemBean.topNewss.get(i);
                topTitles.add(topNews.title);
                topImageUrls.add(topNews.topimage);
            }
            //传递 图片集合
            rollViewPager.setTopImageUrls(topImageUrls);
            //开始轮播
            rollViewPager.startRoll();
            //标题指示点的初始化
            tv_top_news_title.setText(topTitles.get(0));//蜗居生活
            addPoints();
        }

        if(!isLoadingMore){
            //新闻列表设置适配器
            datas = newsItemBean.newss;
            adapter = new NewsItemBaseAdapter(datas);
            lv_news_item_pager.setAdapter(adapter);
            //停止刷新
            lv_news_item_pager.stopRefresh();
        }else{
            //加载更多
            /**
             * 数据源 拼接 第二页数据.
             适配器 刷新.
             */
            datas.addAll(newsItemBean.newss);
            adapter.notifyDataSetChanged();
            //停止加载更多
            lv_news_item_pager.stopLoadMore();

        }


    }

    private List<NewsItemBean.News> datas = new ArrayList<>();
    private List<ImageView> dots = new ArrayList<>();

    private void addPoints() {
        ll_dots.removeAllViews();
        dots.clear();
        for (int i=0;i<topImageUrls.size();i++){
            ImageView imageView = new ImageView(context);
            if(i == 0){
                imageView.setImageResource(R.drawable.dot_focus);
            }else{
                imageView.setImageResource(R.drawable.dot_normal);
            }
            //添加
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //间距
            params.leftMargin = 10;//px
            ll_dots.addView(imageView,params);
            dots.add(imageView);
        }
    }



    class NewsItemBaseAdapter extends DefaultBaseAdapter<NewsItemBean.News>{

        public NewsItemBaseAdapter(List<NewsItemBean.News> datas) {
            super(datas);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NewsItemHolder holder = null;
            if(convertView==null){
                convertView = View.inflate(context,R.layout.listview_item_news,null);
                holder = new NewsItemHolder();
                holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tv_pub_date = (TextView) convertView.findViewById(R.id.tv_pub_date);
                convertView.setTag(holder);
            }else{
                holder  = (NewsItemHolder) convertView.getTag();
            }
            NewsItemBean.News news =  datas.get(position);
            holder.tv_title.setText(news.title);
            holder.tv_pub_date.setText(news.pubdate);
            //图片
            String imageUrl = NetUrl.base_url+news.listimage;
            bitmapUtils.display(holder.iv_img,imageUrl);

            //处理已读新闻 id1#id2
            String readNewsIds = SpUtils.getReadNewsIds(context);
            String currentNewsId = String.valueOf(news.id);
            if(readNewsIds.contains(currentNewsId)){
                holder.tv_title.setTextColor(Color.RED);
            }else{
                holder.tv_title.setTextColor(Color.BLACK);
            }
            return convertView;
        }
    }
    class NewsItemHolder {
        ImageView iv_img;
        TextView tv_title,tv_pub_date;
    }

    private boolean  isLoadingMore = false;//是否正在加载更多 , 默认不是
    /**
     * 下拉刷新回调
     * 联网请求数据: 最新 第一页的数据
     */
    @Override
    public void onRefresh() {
        isLoadingMore = false;

        requestPage(firstPageUrl);

    }

    /**
     * 上啦加载更多
     */
    @Override
    public void onLoadMore() {
        isLoadingMore = true;

        pageNum++;
        String nextPageUrl = NetUrl.base_url+"/getNewsByNewsTypeId?newsTypeId="+newsBean.id+"&pageNum="+pageNum;

        requestPage(nextPageUrl);

    }
}