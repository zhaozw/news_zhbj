package cn.itcast.zz.zhbj20.pager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * Created by wangdh on 2016/11/27.
 * 专题对应的pager界面
 */
public class TopicPager extends BasePager {

    private TextView textView;

    public TopicPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        textView = new TextView(context);
        return textView;
    }

    @Override
    public void initData() {
        textView.setText("我是专题界面");
    }
}
