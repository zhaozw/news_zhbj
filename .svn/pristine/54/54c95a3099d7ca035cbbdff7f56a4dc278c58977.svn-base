package cn.itcast.zz.zhbj20.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.itcast.zz.zhbj20.activity.MainActivity;

/**
 * Created by wangdh on 2016/12/3.
 * 自定义的 网络图片框架
 *      三级缓存
 *
 * * 三级缓存
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
 *
 */
public class MyBitmapUtils {

    private static final String TAG = "MyBitmapUtils";
    private  Context context;

    private LruCache<String,Bitmap> mLruCache = null;
    private final File rootFile;
    private final ExecutorService executorService;

    public MyBitmapUtils(Context context) {
        this.context = context;
        //1. 内存缓存
        //(1) int maxSize : 最大内存  : 8/1 app运行时内存
        //(2) 重新 sizeOf
        int maxSize = (int) (Runtime.getRuntime().maxMemory()/8);
        mLruCache = new LruCache<String,Bitmap>(maxSize){
            /**
             *  LruCache 存储 对象的 内存大小  如何计算.
             * @param key
             * @param value
             * @return
             */
            @Override
            protected int sizeOf(String key, Bitmap value) {
//                return value.getByteCount();//字节个数 -- 内存大小 // getRowBytes() * getHeight();
                return value.getRowBytes() * value.getHeight();//字节个数 -- 内存大小 // getRowBytes() * getHeight();
            }
        };
        //2. 本地缓存  图片缓存文件  的 根路径
        rootFile = context.getCacheDir();

        //3. 网络缓存  线程池
        //创建固定大小的线程池  : 当前池子  最多有5个子线程
        executorService = Executors.newFixedThreadPool(5);
    }

    /**
     * 展示
     * @param imageView : 控件
     * @param imageUrl : 图片url
     */
    public void display(ImageView imageView, String imageUrl) {
        //1. 内存缓存  LruCache
        // 如果获取到,展示. 如果获取不到,再从本地缓存获取.
        Bitmap cacheBitmap = mLruCache.get(imageUrl);
        if(cacheBitmap!=null){
            //展示
            imageView.setImageBitmap(cacheBitmap);
            Log.i(TAG, "display: 从内存中获取");
            return;
        }
        //2. 从本地获取  暂时存储到 cache目录
        // 确定 图片的路径  --- file
        String imageName = MD5Encoder.encode(imageUrl);// imageUrl+md5 -- http://xxxxx/xxxx/xxx.png - md5 --- asdlfkjsad234234  不加后缀
        File fileImage = new File(rootFile.getAbsolutePath(),imageName);
        //如果本地图片存在,展示 /并且存储到内存缓存中
        if(fileImage.exists()&&fileImage.length()!=0){
            //存在,有效
            //如何展示 file图片 --- bitmap
            Bitmap decodeFileBitmap = BitmapFactory.decodeFile(fileImage.getAbsolutePath());
            imageView.setImageBitmap(decodeFileBitmap);
            mLruCache.put(imageUrl,decodeFileBitmap);
            Log.i(TAG, "display: 从本地缓存中获取");
            return;
        }
        //3. 从网络获取  -- 下载图片 imageUrl,并且展示 imageView
        //开启子线程 -- 线程池
//        new Thread(){}.start();
        executorService.execute(new MyDownLoadRunnable(imageView,imageUrl));
        Log.i(TAG, "display: 从网络获取");
    }

    /**
     * 下载图片 imageUrl,并且展示 imageView
     */
    class MyDownLoadRunnable implements  Runnable{

        private  ImageView imageView;
        private  String imageUrl;

        public MyDownLoadRunnable(ImageView imageView, String imageUrl){
            this.imageView = imageView;
            this.imageUrl = imageUrl;
        }

        @Override
        public void run() {
            //请求网络 imageurl  httpurlconnection
            try {
                URL url = new URL(imageUrl);//图片url
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //请求方式
                conn.setRequestMethod("GET");
                //超时时间
                conn.setConnectTimeout(5*1000);
                //获取响应码
                int responseCode = conn.getResponseCode();
                if(responseCode==200){
                    //获取流
                    InputStream inputStream = conn.getInputStream();
                    //1.存储内存,  lruCache . bitmap
                    //inputStream -- bitmap
                    final Bitmap decodeStreamBitmap = BitmapFactory.decodeStream(inputStream);
                    mLruCache.put(imageUrl,decodeStreamBitmap);
                    // 2.存储本地中, 图片文件
                    // bitmap -- 文件
                    //compress: 压缩
                    //(1) compressFormat : 压缩格式  png/jpg/webp
                    //(2) int qulity  质量 : 0-100  0 -- 100 (压缩) / 100 -- 0(无压缩)
                    //(3) 输出流
                    String imageName = MD5Encoder.encode(imageUrl);// imageUrl+md5 -- http://xxxxx/xxxx/xxx.png - md5 --- asdlfkjsad234234  不加后缀
                    File fileImage = new File(rootFile.getAbsolutePath(),imageName);
                    OutputStream os = new FileOutputStream(fileImage);
                    decodeStreamBitmap.compress(Bitmap.CompressFormat.PNG,100,os);
                    // 3.展示. --  子线程无法更新ui
                    MainActivity mainActivity = (MainActivity) context;
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(decodeStreamBitmap);
                        }
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
