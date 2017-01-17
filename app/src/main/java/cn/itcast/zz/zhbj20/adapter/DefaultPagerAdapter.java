package cn.itcast.zz.zhbj20.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.itcast.zz.zhbj20.pager.BasePager;

/**
 * Created by wangdh on 2016/12/1.
 * Viewpager的Pageradapter的基类
 */
public abstract class DefaultPagerAdapter<T> extends PagerAdapter {

    private  List<T> datas;

    public DefaultPagerAdapter(List<T> datas){
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public abstract Object instantiateItem(ViewGroup container, int position) ;


}