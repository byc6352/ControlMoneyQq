/**
 * 
 */
package com.example.h3.util;

import com.byc.ControlMoneyQQ.R;
import com.example.h3.Config;
import com.example.h3.job.FloatingWindow;
import com.example.h3.job.LuckyMoneyDetailJob;

import com.example.h3.job.WechatAccessbilityJob;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * @author byc
 *
 */
public class VolumeChangeListen {
	private static VolumeChangeListen current;
	private Context context;
	private static String TAG="byc001";
	private WechatAccessbilityJob job;
	private SpeechUtil speeker ;
	 /**
     * 当前音量
     */
    private int currentVolume;
    /**
     * 控制音量的对象
     */
    public AudioManager mAudioManager;
    /**
     * 系统最大音量
     */
    private int maxVolume;
    /**
     * 确保关闭程序后，停止线程
     */
    private boolean isDestroy=false;
    /**
     * 消息定义：
     */
    Message msgPutThunder;
    
	  public static synchronized VolumeChangeListen getVolumeChangeListen(WechatAccessbilityJob job) {
	    	
	        if(current == null) {
	            current = new VolumeChangeListen(job);
	        }
	        return current;
	    }
	    private VolumeChangeListen(WechatAccessbilityJob job){
	    	this.job=job;
	    	this.context = job.context;
	    	TAG=Config.TAG;
	    	speeker=SpeechUtil.getSpeechUtil(context);
	    	 // 获得AudioManager对象
	        mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);//音乐音量,如果要监听铃声音量变化，则改为AudioManager.STREAM_RING
	        //maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);

	    }
	    public void onDestroy()
	    {
	        isDestroy = true;
	    }
	    /**
	     * 监听音量按键的线程
	     */
	    private Thread volumeChangeThread;
	    /**
	     * 持续监听音量变化 说明： 当前音量改变时，将音量值重置为最大值减2
	     */
	    public void onVolumeChangeListener()
	    {
	 
	        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
	        volumeChangeThread = new Thread()
	        {
	            public void run()
	            {
	                while (!isDestroy)
	                    {
	                        int count = 0;
	                        boolean isDerease = false;
	                        // 监听的时间间隔
	                        try
	                            {
	                                Thread.sleep(20);
	                            } catch (InterruptedException e)
	                            {
	                                System.out.println("error in onVolumeChangeListener Thread.sleep(20) " + e.getMessage());
	                            }
	 
	                        if (currentVolume < mAudioManager.getStreamVolume(AudioManager.STREAM_RING))
	                            {
	                                count++;
	                                currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
	                                // 设置音量等于 maxVolume-2的原因是：当音量值是最大值和最小值时，按音量加或减没有改变，所以每次都设置为固定的值。
	                                mAudioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume - 2,
	                                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
	                            }
	                        if (currentVolume > mAudioManager.getStreamVolume(AudioManager.STREAM_RING))
	                            {
	                                count++;
	                                currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
	                                mAudioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume - 2,
	                                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
	                                if (count == 1)
	                                    {
	                                        isDerease = true;
	                                    }
	 
	                            }
	 
	                        if (count == 2)
	                            {
	                        		count=0;
	                    	        //消息初始化：
	                                msgPutThunder = Message.obtain();
	                                msgPutThunder.what = Config.MSG_PUT_THUNDER;
	                        		handlerPutThunder.sendMessage(msgPutThunder);
	                                System.out.println("按下了音量+");

	 
	                            } else if (isDerease)
	                            {
	                    	        //消息初始化：
	                                msgPutThunder = Message.obtain();
	                                msgPutThunder.what = Config.MSG_PUT_THUNDER;
	                            	handlerPutThunder.sendMessage(msgPutThunder);
	                                System.out.println("按下了音量-");
	                            }
	 
	                    }
	            };
	        };
	        volumeChangeThread.start();
	    }
	    //埋雷消息处理：
	    private final Handler handlerPutThunder=new Handler(){
	        public void handleMessage(Message msg) {
	        	super.handleMessage(msg);
	            if(msg.what==Config.MSG_PUT_THUNDER) {
	            	//显示确认对话框：
	            	//VolumeChangeListen.this.showNormalDialog();
	            	//继续监听音量键：
	            	//VolumeChangeListen.this.onVolumeChangeListener();
	            	/*
					String sShow="";
					if(!Config.bReg){
						sShow="您是试用版用户！请购买正版！才能开始埋雷。";
						Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
						speeker.speak(sShow);
						return;
					}
					
	            	//开始埋雷：
	            	String sShow="";
					boolean bWindow=VolumeChangeListen.this.job.distributeClickJiaJob();
					if(!bWindow){
						sShow="请进入要埋雷的红包群！才能开始埋雷。";
						Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
						speeker.speak(sShow);
						
					}else{
						Config.JobState=Config.STATE_SENDING_LUCKYMONEY;
					}
					*/
	            	VolumeChangeListen.this.job.distributePutThunder();
	           }
	        }
	    };
	    //显示埋雷确认对话框：
	    private void showNormalDialog(){
	        /* @setIcon 设置对话框图标
	         * @setTitle 设置对话框标题
	         * @setMessage 设置对话框消息提示
	         * setXXX方法返回Dialog对象，因此可以链式设置属性
	         */
	        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(context);
	        normalDialog.setIcon(R.drawable.icon_dialog);
	        normalDialog.setTitle("排雷专家");
	        normalDialog.setMessage("开始埋雷：");
	        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                //开始埋雷：
	            	VolumeChangeListen.this.job.distributeClickJiaJob();
	            }
	        });
	        normalDialog.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                //...To-do
	            }
	        });
	        Dialog dialog=normalDialog.create();
	        // 显示
	        //normalDialog.show();
	        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			dialog.show();
	    }
	    
	    
	    public void onVolumeChangeListener_Music()
	    {
	 
	        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	        volumeChangeThread = new Thread()
	        {
	            public void run()
	            {
	                while (!isDestroy)
	                    {
	                        int count = 0;
	                        boolean isDerease = false;
	                        // 监听的时间间隔
	                        try
	                            {
	                                Thread.sleep(20);
	                            } catch (InterruptedException e)
	                            {
	                                System.out.println("error in onVolumeChangeListener Thread.sleep(20) " + e.getMessage());
	                            }
	 
	                        if (currentVolume < mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC))
	                            {
	                                count++;
	                                currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	                                // 设置音量等于 maxVolume-2的原因是：当音量值是最大值和最小值时，按音量加或减没有改变，所以每次都设置为固定的值。
	                                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume - 2,
	                                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
	                            }
	                        if (currentVolume > mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC))
	                            {
	                                count++;
	                                currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	                                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume - 2,
	                                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
	                                if (count == 1)
	                                    {
	                                        isDerease = true;
	                                    }
	 
	                            }
	 
	                        if (count == 2)
	                            {
	                        		handlerPutThunder.sendMessage(msgPutThunder);
	                                System.out.println("按下了音量+");

	 
	                            } else if (isDerease)
	                            {
	                            	handlerPutThunder.sendMessage(msgPutThunder);
	                                System.out.println("按下了音量-");
	                            }
	 
	                    }
	            };
	        };
	        volumeChangeThread.start();
	    }
}
