/**
 * 
 */
package com.example.h3;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.OutputStream; 
import java.io.InputStream; 
import java.io.PrintWriter;
import java.net.Socket;

import com.example.h3.Sock.SockThread;

import java.net.InetSocketAddress;

import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.os.Handler;
import android.os.Bundle;
import android.accessibilityservice.AccessibilityService;

/**
 * @author byc
 *
 */
public class Sock {
	//程序标识
	private String TAG = "byc001";
	private static Sock current;
	private Context context;
	private String host = "119.23.68.205";
	private int port = 8188;
    private Socket socket = null;

    private byte buffer[] = {0,0,0,0,0,0,0};
    //注册结构：
    //00未注册；0001试用时间；2016-01-01 12：00：00开始时间；0003试用次数；0001已用次数；
    //01已注册；8760使用时间；2016-01-01 12：00：00开始时间；0003试用次数；0001已用次数；
    //定义试用次数：TestNum="0003";使用3次；
    private String appID="ab";//定义app标识：ah排雷专家可试用
    private String host2 = "192.168.1.108";
    private String mPhoneID="";//设备ID
    private int mTestTime=0;//-- 试用0个小时； 
    //private int TestNum=0;//--试用3次 
    //通讯协议（发送）：ggDX4PL5WZFR9M123456789012；（接收）018196
    private String mSendData="";//定义通讯协议
    private String mRecvData="";//定义通讯协议018196
    private String mRegCode="";//注册码
    private MainActivity mainAct;
    //消息定义：
    private static final int MSG_SUC=0x11;//连接服务器成功，成功获得数据；
    private static final int MSG_FAIL=0x12;//连接服务器失败，未获得数据；
    //执行的操作：注册；服务器 端 验证；
    private  static final int HANDLE_REG=0x01;//执行注册操作；
    private static final int HANDLE_VERIFY=0x02;//执行验证操作；
    public int iHandle=HANDLE_REG;
    public Handler HandlerSock = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            if (msg.what == MSG_SUC) {  
                String sReg=mRecvData.substring(0,2);
                mTestTime=Integer.parseInt(mRecvData.substring(2,6));
                if(sReg.equals("01")){//授权成功
                	mainAct.showVerInfo(true);
                	getConfig().setTestTime(mTestTime);
                	getConfig().setRegCode(mRegCode);//保存注册码
                	getConfig().setCurrentStartTestTime();//this.setCurrentStartTestTime();//软件授权成功时，写入置为试用版的开始时间；
                	if(iHandle==HANDLE_REG)
                		Toast.makeText(context, "授权成功！", Toast.LENGTH_LONG).show();
                	
                }else{
                	if(iHandle==HANDLE_REG)
                		Toast.makeText(context, "授权码错误！授权失败！", Toast.LENGTH_LONG).show();
                	mainAct.showVerInfo(false);
                }
                //Log.i(TAG, "handleMessage:" + mRecvData);
            }  
            if (msg.what == MSG_FAIL) {//连接服务器失败
            	Toast.makeText(context, "连接服务器失败！", Toast.LENGTH_LONG).show();
            	mainAct.showVerInfo(false);//连接服务器失败时，置为试用版；
            }
        }  
  
    };  
	
    private Sock(Context context) {
    	this.context=context;
    	this.mainAct=(MainActivity)context;
    	//1判断是否第一次运行，如果是：
    	//在此初始化授权相关数据
    	TAG=Config.TAG;
    	host=getConfig().getHOST();
    	port=getConfig().getPORT();
    	appID=getConfig().getAppID();
    	mPhoneID=getConfig().getPhoneID();
    	mPhoneID=mPhoneID.substring(0, 12);


    }
    public static synchronized Sock getSock(Context context) {
        if(current == null) {
            current = new Sock(context);
        }
        return current;
    }
    public Config getConfig(){
    	return Config.getConfig(context);
    }


    class SockThread extends Thread { 
    	public String mSendStr;
    	 public SockThread(String str) { 
    		 mSendStr=str;
    	 }
    	 @Override  
         public void run() {  
             //定义消息  
             Message msg = new Message();  
             msg.what = MSG_SUC;
             Bundle bundle = new Bundle();
             bundle.clear(); 
             try{
                 //连接服务器 并设置连接超时为5秒  
            	 Log.d(TAG, "SOCKET Start:-------------------------->");
                 socket = new Socket();  
                 socket.connect(new InetSocketAddress(host, port), 5000); 
                 //获取输入输出流  
                 OutputStream out = socket.getOutputStream();
                 InputStream in=socket.getInputStream();

                 //发送数据
                 if (socket.isConnected()) {
                     if (!socket.isOutputShutdown()) {
                         out.write(mSendStr.getBytes("gbk"));  
                         out.flush(); 
                         //Log.d(TAG, "SOCKET Send:"+mSendStr);
                     }

                 }else{//if (socket.isConnected()) {
                     in.close();  
                     out.close();  
                     socket.close();  
                	 HandlerSock.sendEmptyMessage(MSG_FAIL);
                	 return;
                 }
                 //接收数据
                 //读取发来服务器信息  
                 Log.d(TAG, "SOCKET Recv:---------------------------------->");
                 in.read(buffer);
                 mRecvData=new String(buffer);
                 //Log.d(TAG, "SOCKET Recv:"+mRecvData);
                 bundle.putString("msg", mRecvData);  
                 msg.setData(bundle);  //
                 //发送消息 修改UI线程中的组件  
                 HandlerSock.sendMessage(msg);  
                 //关闭各种输入输出流  
                 in.close();  
                 out.close();  
                 socket.close();  
             } catch (IOException ex) {
            	 HandlerSock.sendEmptyMessage(MSG_FAIL);
                 ex.printStackTrace();
                // Log.d(TAG, "SOCKET ERROR:"+ex.getMessage());
                 //return false;
             }//try{
    	 }
    }//class SockThread extends Thread { 
    public void RegStart(String regCode){
    	mRegCode=regCode;
    	mSendData=appID+mPhoneID+regCode;
    	iHandle=HANDLE_REG;
    	new SockThread(mSendData).start();
    	return ;
    }
    public void VarifyStart(){
    	mRegCode=Config.getConfig(context).getRegCode();
    	mPhoneID=Config.getConfig(context).getPhoneIDFromHard();
    	mPhoneID=mPhoneID.substring(0, 12);
    	mSendData=appID+mPhoneID+mRegCode;
    	iHandle=HANDLE_VERIFY;
    	new SockThread(mSendData).start();
    	return ;
    }
}
