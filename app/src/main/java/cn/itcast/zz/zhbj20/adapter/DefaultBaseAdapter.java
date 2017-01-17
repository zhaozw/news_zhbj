package cn.itcast.zz.zhbj20.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.itcast.zz.zhbj20.bean.NewsItemBean;
import cn.itcast.zz.zhbj20.utils.NetUrl;

/**
 * Created by wangdh on 2016/12/1.
 * 抽取基类(框架)步骤:
 *   发现共性 -- 向上抽取
 * 1. 保留共性方法
 * 2. 抽象非共性方法  (限制子类必须重写)
 * 3. 发现未知数据源,应该传递 (构造/setXXX())
 * 4. 发现未知数据类型,应该泛型  T
 */
public abstract class DefaultBaseAdapter<T> extends BaseAdapter {

    public  List<T> datas;

    public DefaultBaseAdapter(List<T> datas){
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent) ;
}