/**
 * 
 */
package com.example.h3.util;


import com.byc.ControlMoneyQQ.R;
import com.example.h3.Config;
import com.example.h3.job.FloatingWindow;

import com.example.h3.job.WechatAccessbilityJob;
import com.example.h3.util.FloatingWindowPic.PicThread;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author byc
 *
 */
public class FloatingWindowText {
	public static String TAG = "byc001";//调试标识：
	private static FloatingWindowText current;
	private Context context;
	//定义浮动窗口布局
	private LinearLayout mFloatLayout;
	private WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
	private WindowManager mWindowManager;
	private boolean bShow=false;//是否显示
	//private SpeechUtil speeker ;
	//计时器：
	private int j=0;
	//bTreadRun:终止线程变量：
	private boolean bTreadRun=true;
	//---------------------对外参数：---------------------------------------------------
	//显示时间：
	public int mShowTime=10;
	//窗口控件对象：
	public TextView tvJE;//显示金额；
	public TextView tvNum;//显示包数；
	public TextView tvThunder;//显示雷值；
	//-----------------------------对外方法------------------------------------------
	//public static synchronized FloatingWindowText getFloatingWindowText(Context context) {获取对象；
	//public void ShowFloatingWindow(){显示窗口；
	//---public void RemoveFloatingWindowText(){关闭显示；
	//---public void StartTime(){          窗口显示计时；
	//---public void StopTime(){           停止计时；
	//Config.ACTION_CALC_THUNDER_SHOW   窗口关闭广播消息；
	//-----------------------------------------------------------------------------
	private FloatingWindowText(Context context) {
		this.context = context;
		TAG=Config.TAG;
		//speeker=SpeechUtil.getSpeechUtil(context);
		createFloatView();
		tvJE = (TextView)mFloatLayout.findViewById(R.id.tvJE);
		tvNum = (TextView)mFloatLayout.findViewById(R.id.tvNum);
		tvThunder = (TextView)mFloatLayout.findViewById(R.id.tvThunder);
	}
    public static synchronized FloatingWindowText getFloatingWindowText(Context context) {
        if(current == null) {
            current = new FloatingWindowText(context);
        }
        return current;
    }
    //---------------------------------显示窗口-----------------------------------------
    public void ShowFloatingWindow(){
    	if(!bShow){
    		
    		 mWindowManager.addView(mFloatLayout, wmParams);
    		bShow=true;
    		StartTime();
    	}
    }
    private void createFloatView()
  	{
  		wmParams = new WindowManager.LayoutParams();
  		//获取WindowManagerImpl.CompatModeWrapper
  		mWindowManager = (WindowManager)context.getSystemService(context.WINDOW_SERVICE);
  		//设置window type
  		wmParams.type = LayoutParams.TYPE_TOAST; 
  		//设置图片格式，效果为背景透明
          wmParams.format = PixelFormat.RGBA_8888; 
          //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
          wmParams.flags = 
//            LayoutParams.FLAG_NOT_TOUCH_MODAL |
            LayoutParams.FLAG_NOT_FOCUSABLE
//            LayoutParams.FLAG_NOT_TOUCHABLE
            ;
          //调整悬浮窗显示的停靠位置为左侧置顶
          wmParams.gravity = Gravity.CENTER | Gravity.CENTER; 
          // 以屏幕左上角为原点，设置x、y初始值
          //wmParams.x = 0;
          //wmParams.y = 0;
          /*// 设置悬浮窗口长宽数据
          wmParams.width = 200;
          wmParams.height = 80;*/
          //设置悬浮窗口长宽数据  
          wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
          wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
          
          LayoutInflater inflater = LayoutInflater.from(context);
          //获取浮动窗口视图所在布局
          mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_calc_thunder_show, null);
          //添加mFloatLayout
          //mWindowManager.addView(mFloatLayout, wmParams)=
          //Log.i(TAG, "mFloatLayout-->left" + mFloatLayout.getLeft());
          //Log.i(TAG, "mFloatLayout-->right" + mFloatLayout.getRight());
          //Log.i(TAG, "mFloatLayout-->top" + mFloatLayout.getTop());
          //Log.i(TAG, "mFloatLayout-->bottom" + mFloatLayout.getBottom());      
          mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
  				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
  				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
         // Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredWidth()/2);
         // Log.i(TAG, "Height/2--->" + mFloatView.getMeasuredHeight()/2);

  	}
    //-----------------------------关闭显示-----------------------------------------
    private void RemoveFloatingWindow(){
		if(mFloatLayout != null)
		{
			if(bShow)mWindowManager.removeView(mFloatLayout);
			bShow=false;
		}
    }
    //计时器：
    class TimeThread extends Thread { 

    private TimeThread() { 
   	 }
   	 @Override  
        public void run() {  

            
            while(bTreadRun){
                //定义消息  
                Message msg = new Message();  
                msg.what = 0x21;
                Bundle bundle = new Bundle();
                bundle.clear(); 
           	 	bundle.putString("msg", "01");  
           	 	msg.setData(bundle);  //
           	 	//发送消息 修改UI线程中的组件  
           	 	HandlerTime.sendMessage(msg); 
           	 	try{
           	 			Thread.sleep(100);
           	 		} catch (InterruptedException e) {
           	 			e.printStackTrace();
           	 		}
           	 //Log.i(TAG, i);
            }    
   	 }
   }//class SockThread extends Thread { 
    //开始计数：
    private void StartTime(){
    	//i=0;
    	j=0;
    	bTreadRun=true;
    	new TimeThread().start();
    	return ;
    }
    //停止计数：
    private void StopTime(){
    	//i=0;
    	j=0;
    	bTreadRun=false;
    	return ;
    }
    //接收消息：
    private Handler HandlerTime = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            if (msg.what == 0x21) {  
            	//Log.i(TAG, "handleMessage----------->"+i);
            	//准备关闭窗口：
           	 	j=j+1;
           	 	if(j>=mShowTime){
           	 		StopTime();
           	 		RemoveFloatingWindow();
           	        Intent intent = new Intent(Config.ACTION_CALC_THUNDER_SHOW);
           	        context.sendBroadcast(intent);
           	 	}//if(j>=c){
            }  
        }  
  
    };  
}
