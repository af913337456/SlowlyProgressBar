package com.slowlyprogressbar;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 林冠宏 on 2016/7/11.
 *
 * 真正的仿微信网页打开的进度条
 *
 * 下面的所有属性都可以自己采用 get set 来自定义
 *
 */

public class SlowlyProgressBar {

    private static final int StartAnimation = 0x12;

    private Handler handler;
    private View view;

    /** 当前的位移距离和速度 */
    private int thisWidth = 0;
    private int thisSpeed = 0;

    private int progress = 0;  /** 当前的进度长度 */
    private int record = 0;    /** 移动单位 */
    private int width = 10;    /** 10dp each time */
    private int height = 3;    /** 3dp */

    private boolean isStart = false;

    private int phoneWidth = 0; /** 屏幕宽度 */
    private int i = 0;

    /** 每次的移动记录容器，位移对应每帧时间 */
    private List<Integer> progressQuery = new ArrayList<>();
    private List<Integer> speedQuery    = new ArrayList<>();

    public SlowlyProgressBar(View view, int phoneWidth) {
        initHandler();
        this.phoneWidth = phoneWidth;
        this.view = view;
    }

    /** 善后工作，释放引用的持有，方能 gc 生效 */
    public void destroy(){
        if(progressQuery!=null){
            progressQuery.clear();
            progressQuery = null;
        }
        if(speedQuery!=null){
            speedQuery.clear();
            speedQuery = null;
        }
        view = null;
        handler.removeCallbacksAndMessages(null);
        handler = null;
    }

    public void setProgress(int progress){
        if(progress>100 || progress <= 0){ /** 不能超过100 */
            return;
        }
        /** 每次传入的 width 应该是包含之前的数值,所以下面要减去 */
        /** 下面记得转化比例，公式 （屏幕宽度 * progress / 100） */
        this.width = (progress * phoneWidth)/100;

        /** lp.width 总是获取前一次的 大小 */
        /** 移动 100px 时的速度一次倍率 是 2 */
        int size = progressQuery.size();
        if(size != 0){
            size = progressQuery.get(size-1);
        }
        Log.d("zzzzz","width - size = "+(width - size));
        /** 计算倍率，2/100 = x/width */
        int distance = width - size;
        int speedTime;
        if(distance<=100){
            speedTime = 2;
        }else{
            speedTime = (int) ((2 * distance)/100.0);
        }
        /** 添加 */
        progressQuery.add(this.width);
        speedQuery.add(speedTime);
        /** 开始 */
        if(!isStart){
            isStart = true;
            handler.sendEmptyMessage(StartAnimation);
        }
    }

    public SlowlyProgressBar setViewHeight(int height){
        this.height = height;
        return this;
    }

    private void initHandler(){
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case StartAnimation:
                        /** 提取队列信息 */
                        if(progress >= thisWidth){ /** 如果已经跑完，那么移出 */
                            if(progressQuery.size() == i){
                                Log.d("zzzzz","break");
                                if(progress >= 100){ /** 全部走完，隐藏进度条 */
                                    view.setVisibility(View.INVISIBLE);
                                }
                                isStart = false;
                                break;
                            }
                            Log.d("zzzzz", "size is " + progressQuery.size());
                            thisWidth = progressQuery.get(i);
                            thisSpeed = speedQuery.get(i);
                            i ++;
                        }
                        move(thisSpeed,view.getLayoutParams().width);
                        Log.d("zzzzz", "send 100 "+thisSpeed);
                        /** 发信息的延时长度并不会影响速度 */
                        handler.sendEmptyMessageDelayed(StartAnimation,1);
                        break;
                }
            }
        };
    }

    /** 移动 */
    private void move(int speedTime,int lastWidth){
        if(speedTime > 9){
            speedTime = 9; /** 控制最大倍率 */
        }
        /** 乘 3 是纠正误差 */
        progress = (record * speedTime);
        /** 纠正 */
        if(progress >= lastWidth){
            view.setLayoutParams(new RelativeLayout.LayoutParams(progress,height*3));
        }else{
            Log.d("zzzzz","hit "+progress+"---"+lastWidth);
        }
        record ++;
    }
}



