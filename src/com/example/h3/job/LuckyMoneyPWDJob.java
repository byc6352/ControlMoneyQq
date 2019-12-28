/**
 * 
 */
package com.example.h3.job;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.baidu.android.common.logging.Log;
import com.byc.ControlMoneyQQ.R;
import com.example.h3.BackgroundMusic;
import com.example.h3.Config;
import com.example.h3.MainActivity;
import com.example.h3.QiangHongBaoService;
import com.example.h3.util.FloatingWindowPic;
import com.example.h3.util.SpeechUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

/**
 * @author byc
 *
 */
public class LuckyMoneyPWDJob {
	private static LuckyMoneyPWDJob current;
	public static String TAG = "byc001";	
	//public QiangHongBaoService service;
	public Context context;
	private SpeechUtil speaker ;
	//PWD面板坐标值：
	private int mCellLen=0;
	private int mCellHigh=0;
	private int mPadLeft=0;
	private int mPadTop=0;
	private int mPadBottom=0;
	//消息定义：
	private static final int  PWD_MSG=0x16;
	//消息键定义：
	private static final String KEY_CLICK_PWD="clickPWD";
	//执行成功定义：
	public boolean bSuccess=false;
	//输入密码延时：
	private FloatingWindowPic fwp;
	
	private BackgroundMusic mBackgroundMusic;
	
	private LuckyMoneyPWDJob(Context context) {
		this.context = context;
		speaker=SpeechUtil.getSpeechUtil(context);
		mBackgroundMusic=BackgroundMusic.getInstance(context);
		//
		fwp=FloatingWindowPic.getFloatingWindowPic(context,R.layout.float_click_delay_show);
		int w=Config.screenWidth-200;
		int h=Config.screenHeight-200;
		fwp.SetFloatViewPara(100, 200,w,h);
		//接收广播消息
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_PUT_PWD_DELAY);
        context.registerReceiver(putPWDdelayReceiver, filter);
	}
    public static synchronized LuckyMoneyPWDJob getLuckyMoneyPWDJob(Context context) {
        if(current == null) {
            current = new LuckyMoneyPWDJob(context);
        }
        current.context=context;
        return current;
        
    }
    public Handler HandlerClickPWD = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            if (msg.what == PWD_MSG) {
            	mBackgroundMusic.stopBackgroundMusic();
            	Bundle bundle=msg.getData();
            	bSuccess=bundle.getBoolean(KEY_CLICK_PWD);
               if(!bSuccess){
   				if(Config.bReg){
					Toast.makeText(context, "本机未ROOT！", Toast.LENGTH_LONG).show();
   				}	
               }//if(!bSuccess){
               //未授权：
               if(!Config.bReg){
					Toast.makeText(context, "本机未授权！", Toast.LENGTH_LONG).show();
	        		String say="您是试用版用户！";
					Toast.makeText(context, say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
               }
            }  
        }  
  
    };  
    //输入密码：
    public boolean putPWD(AccessibilityNodeInfo rootNode){

    	if(!getPWDpadInfo(rootNode))return false;//获取PWD面板长宽和单元格信息；
    	PutPWDdelay();
    	bSuccess=false;		
    	return true;
    }
    public boolean getPWDpadInfo(AccessibilityNodeInfo rootNode){
    	if(mCellLen>0)return true;
    	if(rootNode==null)return false;
    	Rect outBounds=new Rect();//
    	rootNode.getBoundsInScreen(outBounds);
    	int iFrameH=outBounds.bottom;
    	int iFrameW=outBounds.right;
    	mPadBottom=iFrameH;
    	if(iFrameH==1184&&iFrameW==720){
        	mPadLeft=0;
        	mPadTop=780;
        	mCellLen=240;
        	mCellHigh=100;
        	return true;
    	}
    	if(iFrameH==1920&&iFrameW==1080){
        	mPadLeft=0;
        	mPadTop=1310;
        	mCellLen=360;
        	mCellHigh=150;
        	return true;
    	}
    	mPadLeft=0;
    	mCellLen=iFrameW/3;
    	mCellHigh=(int)(mCellLen/2.4);
    	mPadTop=iFrameH-4*mCellHigh;
    	//Log.i(TAG, "getPWDpadInfo success------------>");
    	return true;
    }
    //计算密码面板数字坐标：
    private String getNumPosFromPad(int iNum){
    	int x=0;
    	int y=0;

    	switch(iNum){
    	case 1:
    		x=mPadLeft+mCellLen/2;
    		y=mPadBottom-3*mCellHigh-mCellHigh/2;
    		break;
    	case 2:
    		x=mPadLeft+mCellLen+mCellLen/2;
    		y=mPadBottom-3*mCellHigh-mCellHigh/2;
    		break;
    	case 3:
    		x=mPadLeft+2*mCellLen+mCellLen/2;
    		y=mPadBottom-3*mCellHigh-mCellHigh/2;
    		break;
    	case 4:
    		x=mPadLeft+mCellLen/2;
    		y=mPadBottom-2*mCellHigh-mCellHigh/2;
    		break;
    	case 5:
    		x=mPadLeft+mCellLen+mCellLen/2;
    		y=mPadBottom-2*mCellHigh-mCellHigh/2;
    		break;
    	case 6:
    		x=mPadLeft+2*mCellLen+mCellLen/2;
    		y=mPadBottom-2*mCellHigh-mCellHigh/2;
    		break;
    	case 7:
    		x=mPadLeft+mCellLen/2;
    		y=mPadBottom-mCellHigh-mCellHigh/2;
    		break;
    	case 8:
    		x=mPadLeft+mCellLen+mCellLen/2;
    		y=mPadBottom-mCellHigh-mCellHigh/2;
    		break;
    	case 9:
    		x=mPadLeft+2*mCellLen+mCellLen/2;
    		y=mPadBottom-mCellHigh-mCellHigh/2;
    		break;
    	case 0:
    		x=mPadLeft+mCellLen+mCellLen/2;
    		y=mPadBottom-mCellHigh/2;
    		break;
    	}
    	return "input tap "+String.valueOf(x)+" "+String.valueOf(y);
    }
    //点包延时：
    private void PutPWDdelay() {
    	speaker.speak("正在为您分配红包金额：");
    	mBackgroundMusic.playBackgroundMusic( "dd2.mp3", true);
		fwp.ShowFloatingWindow(); 
    	fwp.c=50;
    	fwp.mSendMsg=Config.ACTION_PUT_PWD_DELAY;
    	fwp.mShowPicType=FloatingWindowPic.SHOW_PIC_GREEN;
    	//fwp.StartSwitchPics();
    }
    //
	private BroadcastReceiver putPWDdelayReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            //Log.d(TAG, "receive-->" + action);
            if(Config.ACTION_PUT_PWD_DELAY.equals(action)) {
            	//开始输入密码：
            	clickPWDthreadStart(); 
            }
        }
    };
    /*
    public boolean getPWDpadInfo(AccessibilityNodeInfo rootNode){
    	if(mCellLen>0)return true;
    	if(rootNode==null)return false;
    	Rect outBounds=new Rect();
    	rootNode.getBoundsInScreen(outBounds);
    	int iFrameH=outBounds.bottom;
    	int iFrameW=outBounds.right;
    	AccessibilityNodeInfo tartget=AccessibilityHelper.findNodeInfosByText(rootNode, "支付安全保障中", 0);
    	if(tartget==null)return false;
    	tartget.getBoundsInScreen(outBounds);
    	int iInfo=outBounds.bottom;
    	mPadLeft=0;
    	mPadTop=iInfo+8;
    	mCellLen=iFrameW/3;
    	mCellHigh=(iFrameH-mPadTop)/4;
    	Log.i(TAG, "getPWDpadInfo success------------>");
    	return true;
    }
    

    //计算密码面板数字坐标：
    private String getNumPosFromPad(int iNum){
    	int x=0;
    	int y=0;

    	switch(iNum){
    	case 1:
    		x=mPadLeft+mCellLen/2;
    		y=mPadTop+mCellHigh/2;
    		break;
    	case 2:
    		x=mPadLeft+mCellLen+mCellLen/2;
    		y=mPadTop+mCellHigh/2;
    		break;
    	case 3:
    		x=mPadLeft+2*mCellLen+mCellLen/2;
    		y=mPadTop+mCellHigh/2;
    		break;
    	case 4:
    		x=mPadLeft+mCellLen/2;
    		y=mPadTop+mCellHigh+mCellHigh/2;
    		break;
    	case 5:
    		x=mPadLeft+mCellLen+mCellLen/2;
    		y=mPadTop+mCellHigh+mCellHigh/2;
    		break;
    	case 6:
    		x=mPadLeft+2*mCellLen+mCellLen/2;
    		y=mPadTop+mCellHigh+mCellHigh/2;
    		break;
    	case 7:
    		x=mPadLeft+mCellLen/2;
    		y=mPadTop+2*mCellHigh+mCellHigh/2;
    		break;
    	case 8:
    		x=mPadLeft+mCellLen+mCellLen/2;
    		y=mPadTop+2*mCellHigh+mCellHigh/2;
    		break;
    	case 9:
    		x=mPadLeft+2*mCellLen+mCellLen/2;
    		y=mPadTop+2*mCellHigh+mCellHigh/2;
    		break;
    	case 0:
    		x=mPadLeft+mCellLen+mCellLen/2;
    		//y=mPadTop+3*mCellHigh+mCellHigh/2;
    		y=mPadHigh-mCellHigh/2;
    		break;
    	}
    	return "input tap "+String.valueOf(x)+" "+String.valueOf(y);
    }
    */
    class clickPWDthread extends Thread { 
    	//public String mSendStr;
    	 public clickPWDthread() { 
    		 //mSendStr=str;
    	 }
    	 @Override  
         public void run() {  
             //定义消息  
             Message msg = new Message();  
             msg.what = PWD_MSG;
             Bundle bundle = new Bundle();
             bundle.clear(); 
             String sNum="";
			 String sPWD=Config.sPWD;
			 String sOrder="";
			 int iNum=0;
			 //Log.i(TAG, "Thread Start------------>");
			 int j=6;
			 if(sPWD.equals("000000")){j=10;sPWD="0000000000";}//如果 密码是6个0，点击10下。
			 try{
				 Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 for(int i=0;i<j;i++){
				 try {
					 sNum=sPWD.substring(i,i+1);
					iNum=Integer.parseInt(sNum);
					sOrder=getNumPosFromPad(iNum);
					//sOrder="input tap 120 764";
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//Log.i(TAG,sOrder);
				if(!RootShellCmd.getRootShellCmd().execShellCmd(sOrder)){
					bundle.putBoolean(KEY_CLICK_PWD, false);
					msg.setData(bundle);
					HandlerClickPWD.sendMessage(msg);
					return;
				}
			}//for(int i=0;i<6;i++){
			bundle.putBoolean(KEY_CLICK_PWD, true);
			msg.setData(bundle);
			HandlerClickPWD.sendMessage(msg);
			return;
    	 }
    }//class SockThread extends Thread { 
    public void clickPWDthreadStart(){
    	new clickPWDthread().start();
    	return ;
    }
}
