package bwie.com.shopcardetails;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;

import static bwie.com.shopcardetails.R.id.vp_FindFragment_pager;

public class MainActivity extends FragmentActivity implements BaseFragment.OnFragmentInteractionListener {

    @BindView(R.id.banner)
    Banner mBanner;
    @BindView(R.id.tab_FindFragment_title)
    TabLayout mTabFindFragmentTitle;
    @BindView(vp_FindFragment_pager)
    ViewPager mVpFindFragmentPager;
    /*@BindView(R.id.tabs)
    PagerSlidingTabStrip mTabs;*/
    private String path = "http://c.3g.163.com/recommend/getChanListNews?channel=T1456112189138&size=20&passport=&devId=1uuFYbybIU2oqSRGyFrjCw%3D%3D&lat=%2F%2FOm%2B%2F8ScD%2B9fX1D8bxYWg%3D%3D&lon=LY2l8sFCNzaGzqWEPPgmUw%3D%3D&version=9.0&net=wifi&ts=1464769308&sign=bOVsnQQ6gJamli6%2BfINh6fC%2Fi9ydsM5XXPKOGRto5G948ErR02zJ6%2FKXOnxX046I&encryption=1&canal=meizu_store2014_news&mac=sSduRYcChdp%2BBL1a9Xa%2F9TC0ruPUyXM4Jwce4E9oM30%3D\n";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_layout);
        ButterKnife.bind(this);
        initData();
        //初始化Fragment
        CommentFragment commentFragment = new CommentFragment();
        DesFragment desFragment = new DesFragment();

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(commentFragment);
        fragments.add(desFragment);

        ArrayList<String> title = new ArrayList<>();
        title.add("评论");
        title.add("详情");

        mTabFindFragmentTitle.setTabMode(TabLayout.MODE_FIXED);
        //加載tablayout的文字
        mTabFindFragmentTitle.addTab(mTabFindFragmentTitle.newTab().setText(title.get(0)));
        mTabFindFragmentTitle.addTab(mTabFindFragmentTitle.newTab().setText(title.get(1)));

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, title);
        ////viewpager加载adapter
        mVpFindFragmentPager.setAdapter(viewPagerAdapter);
        //tablayout和Viewpager绑定
        mTabFindFragmentTitle.setupWithViewPager(mVpFindFragmentPager, true);
    }

    private void initData() {
        OkHttp.getAsync(path, new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {

            }

            @Override
            public void requestSuccess(String result) throws Exception {
                if (result != null) {
                    Gson gson = new Gson();
                    final Beans beans = gson.fromJson(result, Beans.class);
                    //切换主线程
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //imglist
                            List imgList = new ArrayList<String>();
                            //titleList
                            List titleList = new ArrayList<String>();
                            //拿图片集合
                            List<Beans.美女Bean> beanList = beans.美女;
                            for (Beans.美女Bean bean : beanList) {
                                imgList.add(bean.imgsrc);
                                titleList.add(bean.title);
                            }

                            //设置banner样式
                            mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
                            //设置图片加载器
                            mBanner.setImageLoader(new GlideImageLoader());
                            //设置图片集合
                            mBanner.setImages(imgList);
                            //设置banner动画效果
                            mBanner.setBannerAnimation(Transformer.DepthPage);
                            //设置标题集合（当banner样式有显示title时）
                            mBanner.setBannerTitles(imgList);
                            //设置自动轮播，默认为true
                            mBanner.isAutoPlay(true);
                            //设置轮播时间
                            mBanner.setDelayTime(1500);
                            //设置指示器位置（当banner模式中有指示器时）
                            mBanner.setIndicatorGravity(BannerConfig.CENTER);
                            //banner设置方法全部调用完毕时最后调用
                            mBanner.start();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onFragmentInteraction(Bundle bundle) {

    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */
//            eg：

            //Glide 加载图片简单用法
            Glide.with(context).load(path).into(imageView);

            //Picasso 加载图片简单用法
//            Picasso.with(context).load(path).into(imageView);

            //用fresco加载图片简单用法，记得要写下面的createImageView方法
//            Uri uri = Uri.parse((String) path);
//            imageView.setImageURI(uri);
        }

        /*//提供createImageView 方法，如果不用可以不重写这个方法，主要是方便自定义ImageView的创建
        @Override
        public ImageView createImageView(Context context) {
            //使用fresco，需要创建它提供的ImageView，当然你也可以用自己自定义的具有图片加载功能的ImageView
            SimpleDraweeView simpleDraweeView=new SimpleDraweeView(context);
            return simpleDraweeView;
        }*/

    }

}
