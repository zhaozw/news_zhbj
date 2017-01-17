package cn.itcast.zz.zhbj20.pager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * Created by wangdh on 2016/11/27.
 * 互动对应的pager界面
 */
public class InterActionPager extends BasePager {

    private TextView textView;

    public InterActionPager(Context context){
        super(context);
    }
    @Override
    public View initView(){
        textView = new TextView(context);
        return textView;
    }
    @Override
    public void initData(){
        textView.setText("我是互动界面");
    }
}
