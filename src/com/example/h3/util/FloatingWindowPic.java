/**
 * 
 */
package com.example.h3.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.byc.ControlMoneyQQ.R;
import com.example.h3.Config;
import com.example.h3.MainActivity;
import com.example.h3.job.LuckyMoneyReceiveJob;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @author byc
 *
 */
public class FloatingWindowPic {
	public static String TAG = "byc001";//调试标识：
	private Context context;
	//-------------------------------------------------------------------------
	//定义浮动窗口布局
	private LinearLayout mFloatLayout;
	private WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
	private WindowManager mWindowManager;
	
	private boolean bShow=false;//是否显示
	//计数器：
	private int i=0;
	//计时器：
	private int j=0;
	//显示时间：
	public int c=10;
	//资源集合：
	int[] resids=new int[]{
			R.drawable.p0,R.drawable.p1,R.drawable.p2,R.drawable.p3,R.drawable.p4,
			R.drawable.p5,R.drawable.p6,R.drawable.p7,R.drawable.p8,R.drawable.p9
	};
	//红色图片资源集合：
	int[] residsRed=new int[]{
			R.drawable.m0,R.drawable.m1,R.drawable.m2,R.drawable.m3,R.drawable.m4,
			R.drawable.m5,R.drawable.m6,R.drawable.m7,R.drawable.m8,R.drawable.m9
	};
	//++++++++++++++++++++++++++++++++++对外参数+++++++++++++++++++++++++++++++++++++++++++
	//显示资源类别：
	public int mShowPicType=0;//默认显示绿色图片集；
	public static final int SHOW_PIC_GREEN=0;//绿色图片集；
	public static final int SHOW_PIC_RED=1;//红色图片集；
	//发送消息变量：
	public String mSendMsg=Config.ACTION_PUT_PWD_DELAY;
	//------------------------------------对外方法-------------------------------------------
	//--public FloatingWindowPic(Context context,int resource) {
	//public static synchronized FloatingWindowPic getFloatingWindowPic(Context context,int resource) {
	//public void ShowFloatingWindow(){          显示窗口
	//---public void RemoveFloatingWindowPic(){     隐藏窗口
	//public void SetFloatViewPara(int x,int y,int w,int h){   设置窗口位置大小
	//---public void StartSwitchPics(){
	//---public void StopSwitchPics(){
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//bTreadRun:终止线程变量：
	private boolean bTreadRun=true;
	private static FloatingWindowPic current;
	
	private FloatingWindowPic(Context context,int resource) {
		this.context = context;
		TAG=Config.TAG;

		createFloatViewPic(resource);
	}
    public static synchronized FloatingWindowPic getFloatingWindowPic(Context context,int resource) {
        if(current == null) {
            current = new FloatingWindowPic(context,resource);
        }
        return current;
        
    }
    public void ShowFloatingWindow(){
    	if(!bShow){
    		
    		try{
       		 	mWindowManager.addView(mFloatLayout, wmParams);
       		 	bShow=true;
       		 	StartSwitchPics();
       		}catch (Exception e) {
       			e.printStackTrace();
       		}
    	}
    }
    
    private void RemoveFloatingWindowPic(){
		if(mFloatLayout != null)
		{
			if(bShow)mWindowManager.removeView(mFloatLayout);
			bShow=false;
		}
    }
    public void SetFloatViewPara(int x,int y,int w,int h){
        // 以屏幕左上角为原点，设置x、y初始值
    	if(wmParams==null)return;
        wmParams.x = x;
        wmParams.y = y;
        // 设置悬浮窗口长宽数据
        wmParams.width = w;
        wmParams.height =h;
        //设置悬浮窗口长宽数据  
        //wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }
    private void createFloatViewPic(int resource)
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
          // LayoutParams.FLAG_NOT_TOUCH_MODAL |
           LayoutParams.FLAG_NOT_FOCUSABLE	
         //  LayoutParams.FLAG_NOT_TOUCHABLE
           ;
         
         //调整悬浮窗显示的停靠位置为左侧置顶
         wmParams.gravity = Gravity.LEFT | Gravity.TOP; 
         
         // 以屏幕左上角为原点，设置x、y初始值
         wmParams.x = 0;
         wmParams.y = 0;

         // 设置悬浮窗口长宽数据
         //wmParams.width = w;
         //wmParams.height =h;
         
         //设置悬浮窗口长宽数据  
         wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
         wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
         
         LayoutInflater inflater = LayoutInflater.from(context);
         //获取浮动窗口视图所在布局
         //mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_bigpic, null);
         mFloatLayout = (LinearLayout) inflater.inflate(resource, null);
         //添加mFloatLayout
         //mWindowManager.addView(mFloatLayout, wmParams);
    
         mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
 				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
 				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
 	}

    //切换图片：
    private void switchPic(int i){

    	ImageView iv=(ImageView)mFloatLayout.findViewById(R.id.ImageView01);
    	switch(mShowPicType){
    	case SHOW_PIC_GREEN:
    		iv.setImageResource(resids[i]);
    		break;
    	case SHOW_PIC_RED:
    		iv.setImageResource(residsRed[i]);
    		break;
    	}
    }


    class PicThread extends Thread { 

    	 public PicThread() { 

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
            	 HandlerPic.sendMessage(msg); 
            	 try{
            	 Thread.sleep(100);
                 } catch (InterruptedException e) {
            		 e.printStackTrace();
            	 }

            	 //Log.i(TAG, i);
             }

             
    	 }
    }//class SockThread extends Thread { 
    private void StartSwitchPics(){
    	i=0;
    	j=0;
    	bTreadRun=true;
    	new PicThread().start();
    	return ;
    }
    private void StopSwitchPics(){
    	i=0;
    	j=0;
    	bTreadRun=false;
    	return ;
    }
    //接收消息：
    private Handler HandlerPic = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            if (msg.what == 0x21) {  
            	//Log.i(TAG, "handleMessage----------->"+i);
            	switchPic(i);
            	i=i+1;
            	if(i>9)i=0;
            	//准备关闭窗口：
           	 	j=j+1;
           	 	if(j>=c){
           	 		StopSwitchPics();
           	 		RemoveFloatingWindowPic();
           	        //Intent intent = new Intent(Config.ACTION_PUT_PWD_DELAY);
           	 		Intent intent = new Intent(mSendMsg);
           	        context.sendBroadcast(intent);
           	 	}//if(j>=c){
            }  
        }  
  
    };  
	
}
