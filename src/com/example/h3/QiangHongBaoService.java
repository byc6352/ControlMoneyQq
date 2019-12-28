package com.example.h3;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityManager;
import android.util.Log;
import android.widget.Toast;
import android.annotation.TargetApi;
import android.os.Build;
import android.content.Context;
import android.content.Intent;

import java.util.List;
import java.util.Iterator;

import com.example.h3.job.WechatAccessbilityJob;
/**
 * <p>Created by byc</p>
 * <p/>
 * 抢红包外挂服务
 */
public class QiangHongBaoService extends AccessibilityService {
	//程序标识
	private static String TAG = "byc001";

	//类实例对象
	 private static QiangHongBaoService service;
	 //job对象
	 private WechatAccessbilityJob job;
	 @Override
	 public void onCreate() {
		 super.onCreate();
	     service = this;
	     TAG=Config.TAG;
	     job=WechatAccessbilityJob.getJob();
	     //job.onCreateJob(service);
	}
	 public Config getConfig() {
	        return Config.getConfig(this);
	 }

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		/*
		Log.d(TAG, "事件---->" + event);
		String ui=event.getClassName().toString();
		Log.d(TAG, "事件---->" + ui);
		if(Config.DEBUG){

			if(ui.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI")){
				
				//testUI();
			}
			if(ui.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI")){
				//UnpackRedPackage() ;
			}
		}
		*/
		job.onReceiveJob(event);
	}//public
    @Override
    public void onInterrupt() {
    	 Log.d(TAG, "qianghongbao service interrupt");
        //Toast.makeText(this, "中断埋雷服务", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        service = this;
        TAG=Config.TAG;
        job=WechatAccessbilityJob.getJob();
        job.onCreateJob(service);
        //Toast.makeText(this, "已连接埋雷服务", Toast.LENGTH_SHORT).show();
        //发送广播，已经连接上了
        Intent intent = new Intent(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT);
        sendBroadcast(intent);
    }
	@Override
	public void onDestroy() {
	    super.onDestroy();
	    Log.d(TAG, "qianghongbao service destory");
	    job.onStopJob();

	    service = null;
	    job = null;
        //发送广播，已经断开辅助服务
        Intent intent = new Intent(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);
        sendBroadcast(intent);
	}
    
    /**
     * 判断当前服务是否正在运行
     * */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean isRunning() {
        if(service == null) {
            return false;
        }
        AccessibilityManager accessibilityManager = (AccessibilityManager) service.getSystemService(Context.ACCESSIBILITY_SERVICE);
        AccessibilityServiceInfo info = service.getServiceInfo();
        if(info == null) {
            return false;
        }
        List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        Iterator<AccessibilityServiceInfo> iterator = list.iterator();

        boolean isConnect = false;
        while (iterator.hasNext()) {
            AccessibilityServiceInfo i = iterator.next();
            if(i.getId().equals(info.getId())) {
                isConnect = true;
                break;
            }
        }
        if(!isConnect) {
            return false;
        }
        return true;
    }
}