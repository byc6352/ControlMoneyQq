/**
 * 
 */
package com.example.h3.job;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.byc.ControlMoneyQQ.R;
import com.example.h3.Config;
import com.example.h3.MainActivity;
import com.example.h3.QiangHongBaoService;
import com.example.h3.util.SpeechUtil;
import com.example.h3.util.VolumeChangeListen;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;
/**
 * @author byc
 *
 */
public class WechatAccessbilityJob extends BaseAccessbilityJob  {
	
	private static WechatAccessbilityJob current;
	//---------------------------------------------------------------------------
	private SpeechUtil speaker ;
	private FloatingWindow fw;//��ʾ��������
	//private boolean bComp=false;
	private String mCurrentUI="";
	private LuckyMoneyPWDJob mPWDJob;
	private LuckyMoneyLauncherJob mLauncherJob;
	private LuckyMoneyPrepareJob mPrepareJob;
	private VolumeChangeListen mVolumeChange;
	private boolean bDel=false;//ɾ�������
	private AccessibilityNodeInfo mRootNode; 

	//----------------------------------------------------------------------------------------
    @Override
    public void onCreateJob(QiangHongBaoService service) {
        super.onCreateJob(service);
        //ע��context�ı仯��
        //mReceiveJob=LuckyMoneyReceiveJob.getLuckyMoneyReceiveJob(context);
        //mDetailJob=LuckyMoneyDetailJob.getLuckyMoneyDetailJob(context);
        context=getContext();
        mPWDJob=LuckyMoneyPWDJob.getLuckyMoneyPWDJob(context);
        mLauncherJob=LuckyMoneyLauncherJob.getLuckyMoneyLauncherJob(context);
        mPrepareJob=LuckyMoneyPrepareJob.getLuckyMoneyPrepareJob(context);
        speaker=SpeechUtil.getSpeechUtil(context);
        fw=FloatingWindow.getFloatingWindow(this);
        mVolumeChange=VolumeChangeListen.getVolumeChangeListen(this);
        //mVolumeChange.onVolumeChangeListener();
    }
    @Override
    public void onStopJob() {
    	mVolumeChange.onDestroy();
    	//mVolumeChange.
    }
    public static synchronized WechatAccessbilityJob getJob() {
        if(current == null) {
            current = new WechatAccessbilityJob();
        }
        return current;
    }
    
    //----------------------------------------------------------------------------------------
    @Override
    public void onReceiveJob(AccessibilityEvent event) {
    	
    	final int eventType = event.getEventType();
    	String sShow="";

		if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
			//��ȡ�������ƣ�
			mCurrentUI=event.getClassName().toString();
			//Log.i(TAG, "��ǰ����----------------��"+mCurrentUI);
			//Toast.makeText(context, mCurrentUI, Toast.LENGTH_LONG).show();
			//mRootNode=event.getSource();
			//if(mRootNode==null)return;
			//AccessibilityHelper.recycle(mRootNode);
			//-------------------------ɾ����Ϣ�Ի���------------------------------------------------------
			if(bDel){
			if(mCurrentUI.equals("android.app.Dialog")){
				bDel=false;
				mRootNode=event.getSource();
				if(mRootNode==null)return;
				AccessibilityNodeInfo delNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "ɾ���󽫲�������������Ϣ��¼��",-1);
				if(delNode==null)return;
				delNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "ɾ��",-1);
				if(delNode==null)return;
				AccessibilityHelper.performClick(delNode);
				bDel=false;
			}
			}
			//1��++++++++++++++++++++++++++++++++++++�Ƿ������壺++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
				//������ɣ�
				if(Config.JobState==Config.STATE_INPUT_CLOSING){
					if(mPWDJob.bSuccess){
						sShow="�����ɹ���ɣ�";
						mPWDJob.bSuccess=false;
					}else{
						sShow="������ɣ�";
					}
					Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
					speaker.speak(sShow);
					Config.JobState=Config.STATE_NONE;
				}
				//2����ʾ�������壨����Ӻţ���
				fw.ShowFloatingWindow();
				//3����������ť��
				//if(!ClickLuckyMoney(event)){Config.bSendLuckyMoney=false;return;}
			}else  {
				//4���ر�������ť��
				fw.DestroyFloatingWindow();
				//5�����Ƿ���״̬���˳���
				if(Config.JobState==Config.STATE_NONE)return;
			}
			//6���Ƿ���״̬��
			//+++++++++++++++++++++++++++++++++�����ı���++++++++++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_PREPARE_UI)){
				//2.3�����ı���
				//2.3�����ı���
				if(Config.JobState!=Config.STATE_INPUT_TEXT)return;
		        AccessibilityNodeInfo rootNode = event.getSource();
		        if (rootNode == null) {return; }
				mPrepareJob.inputText2(rootNode);
				return;
				/*
				if(Config.JobState!=Config.STATE_INPUT_TEXT)return;
				boolean bResult=mPrepareJob.inputText(event);
	        	if(bResult==false&&Config.bReg==false){
	        		String say="�������ð��û������ֶ���ɺ������׹��������ð��û����׻�����0~5��֮�䡣";
					Toast.makeText(context, say, Toast.LENGTH_LONG).show();
					speeker.speak(say);
	        	}
	        	if(bResult)
	        		Config.JobState=Config.STATE_INPUT_PWD;
	        	else
	        		Config.JobState=Config.STATE_INPUT_CLOSING;
				return;
				*/
				
			}
			//+++++++++++++++++++++++++++++��������++++++++++++++++++++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_WALLETPAY_UI)){
				//2.4�������룺
				if(Config.JobState!=Config.STATE_INPUT_PWD)return;
		        AccessibilityNodeInfo rootNode = event.getSource();
		        if (rootNode == null) {return; }
				//AccessibilityHelper.recycle(rootNode);
				mPWDJob.putPWD(rootNode);
				Config.JobState=Config.STATE_INPUT_CLOSING;
				return;
			}
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 
		//+++++++++++++++++++++++++++++++++���ݸı�+++++++++++++++++++++++++++++++++++++++++++++++
		if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {

			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
				mRootNode=event.getSource();
				if(mRootNode==null)return;
				mRootNode=AccessibilityHelper.getRootNode(mRootNode);
				//---------------------------------�Ƿ��ǵ���ʽ����----------------------------------------
				if(bDel){
				if(mLauncherJob.isPopmenuUi(mRootNode)){
					AccessibilityNodeInfo adPop=AccessibilityHelper.findNodeInfosByText(mRootNode, "ɾ��",-1);
					if(adPop!=null){
						AccessibilityHelper.performClick(adPop);
						//adPop.
					}
				}
				}
				if(mLauncherJob.isMemberChatUi(mRootNode)){
					//ʵ�ֳ���ָ�� ���ݣ�-----------------------���� ɾ���˵�-------------------------------------------------------
					AccessibilityNodeInfo adNode=AccessibilityHelper.findNodeInfosByText(mRootNode,mLauncherJob.mStrAD ,-1);
					if(adNode!=null){
						if(AccessibilityHelper.performLongClick(adNode))bDel=true;
						//if(adNode.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK))bDel=true;
					}
					//----------------------------------------------------------------------------------
					mLauncherJob.mIntAD=mLauncherJob.mIntAD+1;
					if(mLauncherJob.mIntAD>mLauncherJob.MAX_NO_REG_AD)mLauncherJob.SendAD(mRootNode);//���ð淢����棻
					//_____________________________________����Ǻ��Ⱥ���򲻷������____________________________________________	
					if(mLauncherJob.getLastLuckyMoneyNoded(mRootNode)!=null)mLauncherJob.mIntAD=0;

				}
				//if(Config.getConfig(context).bAutoClearThunder)clickLuckyMoney();
			}//if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 
    }
    public boolean distributePutThunder(){
		boolean bWindow=distributeClickJiaJob();
		if(!bWindow){
			String sShow="�����Ҫ�����ĺ��Ⱥ�����ܿ�ʼ������";
			Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
			speaker.speak(sShow);
			fw.DestroyFloatingWindow();
			return false;
		}else{
			Config.JobState=Config.STATE_INPUT_TEXT;//���������ı�״̬��
			RootShellCmd.getRootShellCmd().initShellCmd();
			return true;
		}
    }
   
    //����Ӻţ�
    public boolean distributeClickJiaJob() {
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        if (rootNode == null) {return false;}
        if(!mLauncherJob.isMemberChatUi(rootNode))return false;
		//mLauncherJob.mIntAD=mLauncherJob.mIntAD+1;
		//if(mLauncherJob.mIntAD>mLauncherJob.MAX_NO_REG_AD)mLauncherJob.SendAD(rootNode);//���ð淢����棻
        if(!mLauncherJob.ClickJia(rootNode))return false;
        return mLauncherJob.ClickLuckyMoney(rootNode);
    }


}