package cn.itcast.zz.zhbj20.pager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by wangdh on 2016/11/27.
 * pager界面基类
 */
public abstract class BasePager {
    public Context context;

    public BasePager(Context context) {
        this.context = context;
    }

    /**
     * 1.确定当前自定义pager的布局    View.inflate 将布局转换为view
     * 2. 查找控件
     * @return
     */
    public abstract View initView();

    public abstract void initData();

    public void mySend(String url, RequestCallBack<String> reqestCallback) {
        //请求数据
        HttpUtils httpUtils = new HttpUtils();
        //1.请求方式  get/post  post安全,url长度没限制
        //            get  常用于 获取数据
        //            post 常用于 提交数据
        httpUtils.send(HttpRequest.HttpMethod.GET, url, reqestCallback);
    }
}
