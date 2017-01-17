package cn.itcast.zz.zhbj20.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import cn.itcast.zz.zhbj20.R;
import cn.itcast.zz.zhbj20.utils.NetUrl;

/**
 * Created by wangdh on 2016/12/3.
 * 新闻详情界面
 */
public class NewsDetailActivity  extends Activity{
    //标题栏控件
    @ViewInject(R.id.imgbtn_left)
    private ImageButton imgbtn_left;

    @ViewInject(R.id.imgbtn_left2)
    private ImageButton imgbtn_left2;

    @ViewInject(R.id.txt_title)
    private TextView txt_title;

    @ViewInject(R.id.imgbtn_right)
    private ImageButton imgbtn_right;
    @ViewInject(R.id.imgbtn_right2)
    private ImageButton imgbtn_right2;

    @ViewInject(R.id.wv_news_detail)
    private WebView wv_news_detail;

    @ViewInject(R.id.loading_view)
    private View loading_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ViewUtils.inject(this);
        initTitleBar();
        initData();
    }

    private void initTitleBar() {
        imgbtn_left.setVisibility(View.GONE);
        //返回按钮
        imgbtn_left2.setVisibility(View.VISIBLE);
        imgbtn_left2.setImageResource(R.drawable.back);
        imgbtn_left2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //中间标题
        txt_title.setVisibility(View.GONE);
        //右侧按钮
        imgbtn_right.setVisibility(View.VISIBLE);
        imgbtn_right.setImageResource(R.drawable.icon_textsize);
        imgbtn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewsDetailActivity.this, "调整字体大小", Toast.LENGTH_SHORT).show();
                WebSettings settings = wv_news_detail.getSettings();
                settings.setTextSize(WebSettings.TextSize.LARGEST);
            }
        });
        imgbtn_right2.setVisibility(View.VISIBLE);
        imgbtn_right2.setImageResource(R.drawable.icon_share);
        imgbtn_right2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewsDetailActivity.this, "分享", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initData() {
        //获取传递的 url
        String newsDetailUrl = getIntent().getStringExtra("newsDetailUrl");
        //展示 html
        wv_news_detail.loadUrl(NetUrl.base_url+newsDetailUrl);

        wv_news_detail.setWebViewClient(new WebViewClient(){
            //开始加载的时候 显示 进度条
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                loading_view.setVisibility(View.VISIBLE);
            }
            //加载完毕的时候 隐藏进度条
            @Override
            public void onPageFinished(WebView view, String url) {
                loading_view.setVisibility(View.GONE);
            }
        });

    }
}
