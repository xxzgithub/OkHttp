package bwie.com.shopcardetails;

/**
 * 文 件 名: MyApplication
 * 创 建 人: 谢兴张
 * 创建日期: 2017/10/20
 * 邮   箱:
 * 博   客:
 * 修改时间：
 * 修改备注：
 */

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 仿淘宝上拉进入商品详情的控件，这里继承RelativeLayout，尝试继承LinearLayout，上拉没效果，
 * 可能是因为LinearLayout没有层次的原因
 */
public class ScrollViewContainer extends RelativeLayout {


    /**
     * 自动上滑
     */
    public static final int AUTO_UP = 0;
    /**
     * 自动下滑
     */
    public static final int AUTO_DOWN = 1;
    /**
     * 动画完成
     */
    public static final int DONE = 2;
    /**
     * 动画速度
     */
    public static final float SPEED = 10.0f;

    //判断onMeasured()方法是否是第一次调用
    private boolean isMeasured = false;

    /**
     * 用于计算手滑动的速度
     */
    private VelocityTracker vt;

    //控件的高度
    private int mViewHeight;
    //控件的宽度
    private int mViewWidth;

    //第一个页面
    private View topView;
    //上拉后出现的第二个页面
    private View bottomView;

    //可以下拉标记
    private boolean canPullDown;
    //可以上拉标记
    private boolean canPullUp;
    //状态码，可以是DONE、AUTO_UP、AUTO_DOWN
    private int state = DONE;

    /**
     * 记录当前展示的是哪个view，0是topView，1是bottomView
     */
    private int mCurrentViewIndex = 0;
    /**
     * 手滑动距离，这个是控制布局的主要变量
     */
    private float mMoveLen;
    //计时器对象
    private MyTimer mTimer;
    //上一次的Y轴方向的坐标
    private float mLastY;
    /**
     * 用于控制是否变动布局的另一个条件，mEvents==0时布局可以拖拽了，mEvents==-1时可以舍弃将要到来的第一个move事件，
     * 这点是去除多点拖动剧变的关键
     */
    private int mEvents;


    public ScrollViewContainer(Context context) {
        super(context);
        init();
    }

    public ScrollViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScrollViewContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //处理手指离开屏幕后的自动滚动
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (mMoveLen != 0) {
                if (state == AUTO_UP) {
                    mMoveLen -= SPEED;
                    if (mMoveLen <= -mViewHeight) {
                        mMoveLen = -mViewHeight;
                        state = DONE;
                        mCurrentViewIndex = 1;
                    }
                } else if (state == AUTO_DOWN) {
                    mMoveLen += SPEED;
                    if (mMoveLen >= 0) {
                        mMoveLen = 0;
                        state = DONE;
                        mCurrentViewIndex = 0;
                    }
                } else {
                    mTimer.cancel();
                }
            }
            //当view确定自身已经不再适合现有的区域时，
            // 该view本身调用这个方法要求
            // parent view重新调用他的onMeasure onLayout来对重新设置自己位置。
            requestLayout();
        }

    };

    private void init() {
        mTimer = new MyTimer(handler);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (vt == null)
                    vt = VelocityTracker.obtain();
                else
                    vt.clear();
                mLastY = ev.getY();
                vt.addMovement(ev);
                mEvents = 0;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                // 多一只手指按下或抬起时舍弃将要到来的第一个事件move，防止多点拖拽的bug
                mEvents = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                vt.addMovement(ev);
                if (canPullUp && mCurrentViewIndex == 0 && mEvents == 0) {
                    mMoveLen += (ev.getY() - mLastY);
                    // 防止上下越界
                    if (mMoveLen > 0) {
                        //此时是向下拖动，故无效
                        mMoveLen = 0;
                        mCurrentViewIndex = 0;
                    } else if (mMoveLen < -mViewHeight) {
                        //这种情况，直接切换到下一个view了
                        mMoveLen = -mViewHeight;
                        mCurrentViewIndex = 1;

                    }
                    if (mMoveLen < -8) {
                        // 防止事件冲突
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                    }
                } else if (canPullDown && mCurrentViewIndex == 1 && mEvents == 0) {
                    mMoveLen += (ev.getY() - mLastY);
                    // 防止上下越界
                    if (mMoveLen < -mViewHeight) {
                        //正在向上拖动，无效 (mMoveLen在第二页时是一个负值)
                        mMoveLen = -mViewHeight;
                        mCurrentViewIndex = 1;
                    } else if (mMoveLen > 0) {
                        //这种情况直接拉到上一个view了
                        mMoveLen = 0;
                        mCurrentViewIndex = 0;
                    }
                    if (mMoveLen > 8 - mViewHeight) {
                        // 防止事件冲突
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                    }
                } else
                    mEvents++;
                mLastY = ev.getY();
                requestLayout();
                break;
            case MotionEvent.ACTION_UP:
                mLastY = ev.getY();
                vt.addMovement(ev);
                vt.computeCurrentVelocity(700);
                // 获取Y方向的速度
                float mYV = vt.getYVelocity();
                if (mMoveLen == 0 || mMoveLen == -mViewHeight) {
                    break;
                }
                if (Math.abs(mYV) < 500) {
                    // 速度小于一定值的时候当作静止释放，这时候两个View往哪移动取决于滑动的距离
                    if (mMoveLen <= -mViewHeight / 2) {
                        state = AUTO_UP;
                    } else if (mMoveLen > -mViewHeight / 2) {
                        state = AUTO_DOWN;
                    }
                } else {
                    // 抬起手指时速度方向决定两个View往哪移动
                    if (mYV < 0)
                        state = AUTO_UP;
                    else
                        state = AUTO_DOWN;
                }
                mTimer.schedule(2);
                break;

            case MotionEvent.ACTION_CANCEL:
                try {
                    vt.recycle();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
        super.dispatchTouchEvent(ev);
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        topView.layout(0, (int) mMoveLen, mViewWidth,
                topView.getMeasuredHeight() + (int) mMoveLen);

        bottomView.layout(0, topView.getMeasuredHeight() + (int) mMoveLen,
                mViewWidth, topView.getMeasuredHeight() + (int) mMoveLen
                        + bottomView.getMeasuredHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isMeasured) {
            isMeasured = true;

            topView = getChildAt(0);
            bottomView = getChildAt(1);

            mViewHeight = getMeasuredHeight();
            mViewWidth = getMeasuredWidth();

            topView.setOnTouchListener(topViewTouchListener);
            //获得第二个scrollview
            ((ViewGroup) bottomView).getChildAt(2).setOnTouchListener(bottomViewChildTouchLister);
        }
    }

    private OnTouchListener topViewTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ScrollView sv = (ScrollView) v;

            //这个是为了保证控件高度一定等于scrollView的高度，避免某些手机可能可以把底部导航栏隐藏时，
            // 使得布局高度发生变化时出现的bug
            if (mViewHeight != sv.getMeasuredHeight())
                mViewHeight = sv.getMeasuredHeight();
            /**
             * sv.getChildAt(0).getMeasuredHeight() 获得了弟0个子view（即LinerLayout）的实际高度
             * sv.getMeasuredHeight() 获得了scrollview控件的高度
             *判断scrollView是否已经滑到底，是则可以上拉进入详情页面，否则不行
             */
            canPullUp = sv.getScrollY() >= (sv.getChildAt(0).getMeasuredHeight() - sv
                    .getMeasuredHeight()) && mCurrentViewIndex == 0;

            return false;
        }
    };


    private OnTouchListener bottomViewChildTouchLister = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //第二个scrollView是在滑到顶的时候可以下拉回第一个页面
            canPullDown = v.getScrollY() == 0;
            return false;
        }
    };


    /**
     * 用于实现动画的计时器类，其实就是每隔若干毫秒向handler发送信息进行刷新页面布局
     */
    class MyTimer {
        private Handler handler;
        private Timer timer;
        private MyTask mTask;

        public MyTimer(Handler handler) {
            this.handler = handler;
            timer = new Timer();
        }

        public void schedule(long period) {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
            mTask = new MyTask(handler);
            timer.schedule(mTask, 0, period);
        }

        public void cancel() {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
        }

        class MyTask extends TimerTask {
            private Handler handler;

            public MyTask(Handler handler) {
                this.handler = handler;
            }

            @Override
            public void run() {
                handler.obtainMessage().sendToTarget();
            }

        }
    }
/**
 1、由于这里为两个ScrollView设置了OnTouchListener，所以在其他地方不能再设置了，否则就白搭了。

 2、两个ScrollView的layout参数统一由mMoveLen决定。

 3、变量mEvents有两个作用：一、是防止手动滑到底部或顶部时继续滑动而改变布局，必须再次按下才能继续滑动；
 二、是在新的pointer down或up时把mEvents设置成-1可以舍弃将要到来的第一个move事件，防止mMoveLen出现剧变。
 为什么会出现剧变呢？因为假设一开始只有一只手指在滑动，记录的坐标值是这个pointer的事件坐标点，
 这时候另一只手指按下了导致事件又多了一个pointer，这时候到来的move事件的坐标可能就变成了新的pointer的坐标，
 这时计算与上一次坐标的差值就会出现剧变，变化的距离就是两个pointer间的距离。所以要把这个move事件舍弃掉，
 让mLastY值记录这个pointer的坐标再开始计算mMoveLen。pointer up的时候也一样。*/
 }