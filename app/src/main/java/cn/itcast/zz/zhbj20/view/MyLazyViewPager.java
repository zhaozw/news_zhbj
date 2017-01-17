package cn.itcast.zz.zhbj20.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by wangdh on 2016/11/27.
 *  屏蔽	左右滑动

 1. 不中断事件
 2. 不消费事件
 */
public class MyLazyViewPager extends  LazyViewPager {
    public MyLazyViewPager(Context context) {
        super(context);
    }

    public MyLazyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
