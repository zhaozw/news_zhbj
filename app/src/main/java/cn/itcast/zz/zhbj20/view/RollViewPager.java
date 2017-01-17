package cn.itcast.zz.zhbj20.view;

import android.content.Context;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.itcast.zz.zhbj20.R;
import cn.itcast.zz.zhbj20.utils.NetUrl;

/**
 * Created by wangdh on 2016/11/30.
 * 1. 自动轮播,
 * 2. 子Viewpager左右滑动.
 */
public class RollViewPager extends ViewPager {
    private static final String TAG = "RollViewPager";
    private List<String> topImageUrls;
    private static final int msg_auto_play_next = 0;

    private BitmapUtils bitmapUtils;
    private RollPagerAdapter adapter;
    private OnItemClickListener onItemClickListener;

    public RollViewPager(Context context) {
        super(context);
        bitmapUtils = new BitmapUtils(context);
    }

    public RollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        bitmapUtils = new BitmapUtils(context);
    }

    /**
     * 接收数据 图片url
     * @param topImageUrls
     */
    public void setTopImageUrls(List<String> topImageUrls) {
        this.topImageUrls = topImageUrls;
        //设置适配器
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case msg_auto_play_next:
                    //切换下一张
                    int nextItem = (getCurrentItem()+1)%topImageUrls.size();
                    setCurrentItem(nextItem);
                    //循环
                    handler.sendEmptyMessageDelayed(msg_auto_play_next,2000);
                    break;
            }
        }
    };

    /**
     * 开始轮播
     */
    public void startRoll() {
        //设置适配器
        if(adapter==null){
            adapter = new RollPagerAdapter();
            setAdapter(adapter);
        }
        //开始轮播  每隔一段时间 切换一张
        handler.sendEmptyMessageDelayed(msg_auto_play_next,2000);
    }

    /**
     * 当前控件挂载到窗体上的时候回调
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /**
     * 当前控件从窗体移除的时候回调
     * 移除消息
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacksAndMessages(null);
    }

    class RollPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return topImageUrls.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageview = (ImageView) View.inflate(getContext(), R.layout.viewpager_item,null);
            //展示网络图片
            /**
             * 三级缓存
             * 1. 内存缓存 , 图片对象(bitmap)存储到内存中.  HashMap<key,value>: key : 唯一标示 imageUrl / value : bitmap
             *               LruCache<key,value> : LruCache与HashMap类似.  算法: Lru  less recent use 最少最近使用.(保存使用频率高,近期使用图片)
             * 2. 本地缓存,  图片文件(.jpg/.png)存储到 sd/内部存储 ,  mnt/sdcard/项目名/cache/image/xxx.png
             *                  xxx : 图片文件名称  , 如何命名?   imageUrl+md5
             *                          md5?  imageUrl : 特殊字符 , window/linux不允许 文件/文件夹包含特殊字符
             *                                 规范imageUrl , 数字+字母
             * 3. 网络缓存, 图片文件存储到网络(服务器)
             *
             *              缓存?  断开网络之后,依然可以获取到的数据.
             *
             * 加载逻辑:  原则: 优先加载速度最快的缓存
             *
             * 1. 先从 内存缓存获取, 如果获取到,展示. 如果获取不到,再从本地缓存获取.
             * 2. 再从 本地缓存, 如果获取到了, 展示 , 并且存储到内存缓存中.  如果获取不到, 最后从网络获取.
             * 3. 从网络缓存获取, 如果获取到了, 存储内存,存储本地中,展示.
             */
            bitmapUtils.display(imageview, NetUrl.base_url+topImageUrls.get(position));
            container.addView(imageview);
            return imageview;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private int downX = 0;
    private int downY = 0;
    private int downTime = 0;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //父Viewpager中途中断事件,判断达到自己滑动距离之后中断事件
        //子Viewpager可以检测到down,一些move,cancle.
        Log.i(TAG, "onTouchEvent: "+ev.getAction());
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                downTime = (int) System.currentTimeMillis();
                getParent().requestDisallowInterceptTouchEvent(true);
                //大喊一声 爹,别抢事件  (请求爹不允许中断事件)
                //轮播图停止
                handler.removeMessages(msg_auto_play_next);
                break;
            case MotionEvent.ACTION_MOVE:
                /**
                 * 2. 优化逻辑 -- move

                 1. 如果处于第一张图片,并且向右滑动 moveX>downX,交由父亲
                 2. 如果处于最后一张图片,并且向左滑动 moveX<downX,交由父亲
                 3. 其他情况,自己处理
                 */
                int moveX = (int) ev.getX();
                if(getCurrentItem() == 0 && moveX>downX){
                    getParent().requestDisallowInterceptTouchEvent(false);
                }else if(getCurrentItem() == topImageUrls.size()-1 && moveX<downX){
                    getParent().requestDisallowInterceptTouchEvent(false);
                }else{
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                /**
                 * 3. 回调监听的方法  onItemClick()  -- action_up

                 单击 : 一次 down / up
                 一次 down / up  : 长按 / 单击  : down/up的间隔时间  : 小于500ms -- 单击
                 一次 down / up : 滑动  / 单击 : down/up的间隔距离 (水平/垂直) : 小于 5px(防抖动)  -- 单击
                 */
                //down/up的间隔时间
                int upTime = (int) System.currentTimeMillis();
                int upX = (int) ev.getX();
                int upY = (int) ev.getY();
                int disX = Math.abs(upX-downX);
                int disY = Math.abs(upY-downY);

                if(upTime-downTime<=500&&disX<5&&disY<5){
                    //回调监听的方法  onItemClick()
                    if(onItemClickListener!=null){
                        onItemClickListener.onItemClick(getCurrentItem());//position -- 当前显示的界面索引
                    }
                }
            case MotionEvent.ACTION_CANCEL:
                //抬起继续轮播
                startRoll();
                break;
        }
        return super.onTouchEvent(ev);
    }
    /**
     * 	自定义监听步骤:

         1. 定义监听接口 OnItemClickListener.

         条目单击的回调方法 onItemClick();

         2. 对外暴露设置监听的方法: setOnItemClickListner(OnItemClickListener onItemClickListener)


         3. 回调监听的方法  onItemClick()  -- action_up

                单击 : 一次 down / up
                一次 down / up  : 长按 / 单击  : down/up的间隔时间  : 小于500ms -- 单击
                一次 down / up : 滑动  / 单击 : down/up的间隔距离 (水平/垂直) : 小于 5px(防抖动)  -- 单击
     */
    public interface  OnItemClickListener{
        /**
         *
         * @param position  : 点击的位置
         */
        public void onItemClick(int position);
    }
    public void setOnItemClickListner(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
