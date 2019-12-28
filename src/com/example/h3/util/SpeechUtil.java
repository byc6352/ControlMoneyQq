/**
 * 
 */
package com.example.h3.util;

import android.content.Context;
import android.media.AudioManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizeBag;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.example.h3.Config;
/**
 * @author byc
 *
 */
public class SpeechUtil {
	
	private static SpeechUtil current;
	private Context context;
	private  String TAG = "byc001";//调试标识：
	private SpeechSynthesizer mSpeechSynthesizer;
	private String  mSpeaker="0";//发音模式：0--普通女声，1--普通男声，2--特别男声，3--情感男声；
	private boolean mSpeaking=true;//是否发音；
	
	private static final String MY_APP_ID = "9529675";
	private static final String MY_API_KEY = "6b3tkbm1Bv7mn1Z5UR96gL6v";
	private static final String MY_SECRET_KEY= "9IQqmTGKwUino6GUAk2kWGUYw3Yl9QgV";
	
	public SpeechUtil(Context context) {
		this.context = context;
		TAG=Config.TAG;
		this.mSpeaker=Config.speaker;
		this.mSpeaking=Config.bSpeaking;
		//initialEnv();
		initialTts();
	}
    public static synchronized SpeechUtil getSpeechUtil(Context context) {
        if(current == null) {
            current = new SpeechUtil(context);
        }
        return current;
    }
 
    /*
     * 设置发音模式：
     */
    public void setSpeaker(String speaker){
    	mSpeaker=speaker;
    	this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, mSpeaker);
    }
    /*
     * 打开或关闭语音提示；
     */
    public void setSpeaking(boolean bSpeaking){
    	this.mSpeaking=bSpeaking;
    }
    /*
     * 初始化TTS；
     */
    private void initialTts() {
        this.mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        this.mSpeechSynthesizer.setContext(context);
        // 请替换为语音开发者平台注册应用得到的apikey和secretkey (在线授权)
        this.mSpeechSynthesizer.setApiKey(MY_API_KEY, MY_SECRET_KEY);
        // 发音人（在线引擎），可用参数为0,1,2,3。。。（服务器端会动态增加，各值含义参考文档，以文档说明为准。0--普通女声，1--普通男声，2--特别男声，3--情感男声。。。）
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, mSpeaker);
        // 设置Mix模式的合成策略
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_HIGH_SPEED_NETWORK);//MIX_MODE_DEFAULT
        // 初始化tts
        mSpeechSynthesizer.initTts(TtsMode.ONLINE);
    }
    //@Override
    private void onDestroy() {
        this.mSpeechSynthesizer.release();
        //super.onDestroy();
    }

    public void speak(String text) {
    	if(!mSpeaking)return;
        //需要合成的文本text的长度不能超过1024个GBK字节。
        int result = this.mSpeechSynthesizer.speak(text);
        if (result < 0) {
            toPrint("error,please look up error code in doc or URL:http://yuyin.baidu.com/docs/tts/122 ");
        }
    }
    private void toPrint(String str) {
    	Log.w(TAG, "TTS:"+str);
    	
    }
}
