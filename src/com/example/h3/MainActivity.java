package com.example.h3;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.byc.ControlMoneyQQ.R;
import com.example.h3.util.SpeechUtil;
import com.example.h3.util.ftp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;; 

public class MainActivity extends Activity implements
SeekBar.OnSeekBarChangeListener{

	private String TAG = "byc001";
	
    // 声明SeekBar 和 TextView对象 
    private SeekBar mSeekBar;
    private TextView tvSpeed; 
    public TextView tvRegState;
    public TextView tvRegWarm;
    public TextView tvHomePage;
    public Button btReg;
    private Button btConcel;
    private Button btClose;
    private Button btStart; 
    public EditText etRegCode; 
    public TextView tvPlease;
    private SpeechUtil speaker ;
    
    //----------------------------------------------
    private EditText etMoneyValue;			//发包金额（以分为单位）
    private EditText etLuckyMoneyNum;		//包数
    private EditText etPWD;					//支付密码：
    private EditText etLuckyMoneySay;		//红包上出现的文字：

    private RadioGroup rgThunderPos;		//埋雷位置
    private RadioButton rbFen;
    private RadioButton rbJiao;
    private RadioButton rbYuan;
    private FlowRadioGroup rgThunderNum;		//请设置出现的雷数：
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private RadioButton rb5;
    private RadioButton rb6;
    private RadioButton rb7;
    private RadioButton rb8;
    private RadioButton rb9;
    private RadioButton rb10;
    private RadioGroup rgMoneySay;
    private RadioButton rbHead;
    private RadioButton rbTail;
    private RadioGroup rgCalcThunder;
    private RadioButton rbAutoThunder;
    private RadioButton rbHandThunder;
    //单雷双雷三雷
    private RadioGroup rgSayThunderNum;
    private RadioButton rbSingleThunder;
    private RadioButton rbDoubleThunder;
    private RadioButton rbThreeThunder;
    private RadioButton rbFourThunder;
    private RadioButton rbFiveThunder;
    //碰碰群玩法
    private RadioGroup rgSelPutThunderPlaying;
    private RadioButton rbCommonPlaying;
    private RadioButton rbBumpPlaying;
    
    public TextView tvResolution;
    //发音模式：
    private RadioGroup rgSelSoundMode; 
    private RadioButton rbFemaleSound;
    private RadioButton rbMaleSound;
    private RadioButton rbSpecialMaleSound;
    private RadioButton rbMotionMaleSound;
    private RadioButton rbChildrenSound;
    private RadioButton rbCloseSound;
    
    
    private BackgroundMusic mBackgroundMusic;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//my codes
		//测试：

		//0.初始化
	    mSeekBar=(SeekBar) findViewById(R.id.seekBar1);
	    tvSpeed = (TextView) findViewById(R.id.tvSpeed); 
	    tvRegState=(TextView) findViewById(R.id.tvRegState);
	    tvRegWarm=(TextView) findViewById(R.id.tvRegWarm);
	    tvHomePage=(TextView) findViewById(R.id.tvHomePage);
	    btReg=(Button)findViewById(R.id.btReg);
	    btConcel=(Button)findViewById(R.id.btConcel);
	    btClose=(Button)findViewById(R.id.btClose);
	    btStart=(Button) findViewById(R.id.btStart); 
	    etRegCode=(EditText) findViewById(R.id.etRegCode); 
	    tvPlease=(TextView) findViewById(R.id.tvPlease); 

	    //------------------------------------------------------------------------
	    etMoneyValue=(EditText)findViewById(R.id.etMoneyValue);
	    etLuckyMoneyNum=(EditText)findViewById(R.id.etLuckyMoneyNum);
	    etPWD=(EditText)findViewById(R.id.etPWD);
	    etLuckyMoneySay=(EditText)findViewById(R.id.etLuckyMoneySay);
	    
	    rgThunderPos = (RadioGroup)this.findViewById(R.id.rgThunderPos);
	    rbFen=(RadioButton)findViewById(R.id.rbFen);
	    rbJiao=(RadioButton)findViewById(R.id.rbJiao);
	    rbYuan=(RadioButton)findViewById(R.id.rbYuan);
	    rgThunderNum = (FlowRadioGroup)this.findViewById(R.id.rgThunderNum);
	    rb1=(RadioButton)findViewById(R.id.rb1);
	    rb2=(RadioButton)findViewById(R.id.rb2);
	    rb3=(RadioButton)findViewById(R.id.rb3);
	    rb4=(RadioButton)findViewById(R.id.rb4);
	    rb5=(RadioButton)findViewById(R.id.rb5);
	    rb6=(RadioButton)findViewById(R.id.rb6);
	    rb7=(RadioButton)findViewById(R.id.rb7);
	    rb8=(RadioButton)findViewById(R.id.rb8);
	    rb9=(RadioButton)findViewById(R.id.rb9);
	    rb10=(RadioButton)findViewById(R.id.rb10);
	    rgMoneySay = (RadioGroup)this.findViewById(R.id.rgMoneySay);
	    rbHead=(RadioButton)findViewById(R.id.rbHead);
	    rbTail=(RadioButton)findViewById(R.id.rbTail);
	    tvResolution=(TextView) findViewById(R.id.tvResolution);
	    
	    rgCalcThunder = (RadioGroup)this.findViewById(R.id.rgCalcThunder);
	    rbAutoThunder=(RadioButton)findViewById(R.id.rbAutoThunder);
	    rbHandThunder=(RadioButton)findViewById(R.id.rbHandThunder);
	    //单雷双雷三雷
	    rgSayThunderNum = (RadioGroup)this.findViewById(R.id.rgSayThunderNum);
	    rbSingleThunder=(RadioButton)findViewById(R.id.rbSingleThunder);
	    rbDoubleThunder=(RadioButton)findViewById(R.id.rbDoubleThunder);
	    rbThreeThunder=(RadioButton)findViewById(R.id.rbThreeThunder);
	    rbFourThunder=(RadioButton)findViewById(R.id.rbFourThunder);
	    rbFiveThunder=(RadioButton)findViewById(R.id.rbFiveThunder);
	    //碰碰群玩法
	    rgSelPutThunderPlaying = (RadioGroup)this.findViewById(R.id.rgSelPutThunderPlaying);
	    rbCommonPlaying=(RadioButton)findViewById(R.id.rbCommonPlaying);
	    rbBumpPlaying=(RadioButton)findViewById(R.id.rbBumpPlaying);
	    //发音模式：
	    rgSelSoundMode = (RadioGroup)this.findViewById(R.id.rgSelSoundMode);
	    rbFemaleSound=(RadioButton)findViewById(R.id.rbFemaleSound);
	    rbMaleSound=(RadioButton)findViewById(R.id.rbMaleSound);
	    rbSpecialMaleSound=(RadioButton)findViewById(R.id.rbSpecialMaleSound);
	    rbMotionMaleSound=(RadioButton)findViewById(R.id.rbMotionMaleSound);
	    rbChildrenSound=(RadioButton)findViewById(R.id.rbChildrenSound);
	    rbCloseSound=(RadioButton)findViewById(R.id.rbCloseSound);
	    Log.d(TAG, "事件---->打开TTS");
	    Config.getConfig(MainActivity.this);//初 始化配置类；
	    speaker=SpeechUtil.getSpeechUtil(MainActivity.this);
	    //获取分辨率:
	    getResolution2();
	    String sResolution="本机分辨率：（"+Config.screenWidth+"*"+Config.screenHeight+"）";
	    tvResolution.setText(sResolution);


		//1。启动微信按钮
		TAG=Config.TAG;
		Log.d(TAG, "事件---->MainActivity onCreate");
		//btConcel=(Button)this.findViewById(R.id.btConcel);
		btConcel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//setTitle("aa");
				//setAppToTestVersion();
				mBackgroundMusic.stopBackgroundMusic();
				if(!Config.bReg){
					String say="必须先授权后才能开启控制金额服务。";
					Toast.makeText(MainActivity.this,say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
					return;
				}
				if(GetParams()){
					//Config.bSendLuckyMoney=true;
					Log.d(TAG, "事件---->打开QQ");
					OpenWechat();
					speaker.speak("启动QQ。开始发包。");
				}
			}
		});//btn.setOnClickListener(
		btClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				System.exit(0);
			}
		});//btn.setOnClickListener(
		//2。打开辅助服务按钮
		btStart.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(!Config.bReg){
					String say="必须先授权后才能开启控制发包金额服务。";
					Toast.makeText(MainActivity.this,say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
					return;
				}
				if(!GetParams()){return;}
				//1判断服务是否打开：
				mBackgroundMusic.stopBackgroundMusic();
				if(!QiangHongBaoService.isRunning()) {
					//打开系统设置中辅助功能
					Log.d(TAG, "事件---->打开系统设置中辅助功能");
					Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS); 
					startActivity(intent);
					Toast.makeText(MainActivity.this, "请找到QQ控制发包金额服务，然后开启QQ控制发包金额服务。", Toast.LENGTH_LONG).show();
					speaker.speak("请找到QQ控制发包金额服务，然后开启QQ控制发包金额服务。");
				}else{
					Toast.makeText(MainActivity.this, "QQ控制发包金额服务已开启！如需重新开启，请重启软件。", Toast.LENGTH_LONG).show();
					speaker.speak("QQ控制发包金额服务已开启！如需重新开启，请重启软件。");
				}
				//2保存延时参数
			}
		});//startBtn.setOnClickListener(
		//3。SeekBar处理
        mSeekBar.setOnSeekBarChangeListener(this); 
        //4.是否注册处理（显示版本信息(包括标题)）
		Config.bReg=getConfig().getREG();
		showVerInfo(Config.bReg);
		if(Config.bReg)//开始服务器验证：
			Sock.getSock(MainActivity.this).VarifyStart();
        //5。注册流程：
		btReg.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//setTitle("aa");
				mBackgroundMusic.stopBackgroundMusic();
				String regCode=etRegCode.getText().toString();
				if(regCode.length()!=12){
					Toast.makeText(MainActivity.this, "授权码输入错误！", Toast.LENGTH_LONG).show();
					speaker.speak("授权码输入错误！");
					return;
				}
				getSock().RegStart(regCode);
				Log.d(TAG, "事件---->测试");
				//System.exit(0);
			}
		});//btReg.setOnClickListener(
		//6。接收广播消息
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT);
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);
        registerReceiver(qhbConnectReceiver, filter);
        //7.播放背景音乐：
        mBackgroundMusic=BackgroundMusic.getInstance(this);
        mBackgroundMusic.playBackgroundMusic( "bg_music.mp3", true);
        //8.设置参数
        SetParams();
        //9.置为试用版；
        setAppToTestVersion();
	}
	private BroadcastReceiver qhbConnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.d(TAG, "receive-->" + action);
            String say="";
            if(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT.equals(action)) {
            	say="已连接QQ控制发包金额服务！";
            } else if(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT.equals(action)) {
            	say="已中断QQ控制发包金额服务！";
            }
            Toast.makeText(MainActivity.this,say, Toast.LENGTH_SHORT).show();
            speaker.speak(say);
        }
    };

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent=new Intent();
			//Intent intent =new Intent(Intent.ACTION_VIEW,uri);
			intent.setAction("android.intent.action.VIEW");
			Uri content_url=Uri.parse(Config.homepage);
			intent.setData(content_url);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//SeekBar接口：
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, 
            boolean fromUser) {// 在拖动中--即值在改变 
        // progress为当前数值的大小 
    	tvSpeed.setText("请设置发包速度:当前发包延迟：" + progress); 
    	
    } 
    
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {// 在拖动中会调用此方法 
    	//mSpeed.setText("xh正在调节"); 
    } 
    
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {// 停止拖动 
    	//mSpeed.setText("xh停止调节"); 
    	//保存当前值
    	Config.getConfig(this).SetWechatOpenDelayTime(seekBar.getProgress());
    	Config.iDelayTime=seekBar.getProgress();
    	speaker.speak("当前发包延迟：" + seekBar.getProgress());
    } 
    public Config getConfig(){
    	return Config.getConfig(this);
    }
    public Sock getSock(){
    	return Sock.getSock(this);
    }
    public boolean OpenWechat(){
    	Intent intent = new Intent(); 
    	PackageManager packageManager = this.getPackageManager(); 
    	intent = packageManager.getLaunchIntentForPackage(Config.WECHAT_PACKAGENAME); 
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP) ; 
    	this.startActivity(intent);
    	return true;
    }

    //取得参数：
    private boolean GetParams(){
    	boolean ret=false;
    	String sSay="";
    	//取发包金额参数：
    	//int iMoneyValue=0;
    	Config.sMoneyValue=etMoneyValue.getText().toString();
    	if(Config.sMoneyValue.equals("")){
    		sSay="请设置发包金额！";
        	Toast.makeText(MainActivity.this, sSay, Toast.LENGTH_LONG).show();
        	speaker.speak(sSay );
        	return ret;
    	}
    	getConfig().setMoneyValue(Config.sMoneyValue);//保存发包金额参数；
    	//取发包包数：
    	//int iLuckyMoneyNum=0;
    	Config.sLuckyMoneyNum=etLuckyMoneyNum.getText().toString();
    	if(Config.sLuckyMoneyNum.equals("")){
    		sSay="请设置发包包数！";
        	Toast.makeText(MainActivity.this, sSay, Toast.LENGTH_LONG).show();
        	speaker.speak(sSay );
        	return ret;
    	}
    	getConfig().setLuckyMoneyNum(Config.sLuckyMoneyNum);//保存发包包数参数；
    	//取支付密码：
    	//int iPWD=0;
    	Config.sPWD=etPWD.getText().toString();
    	if(Config.sPWD.equals("")){
    		sSay="请设置支付密码！";
        	Toast.makeText(MainActivity.this, sSay, Toast.LENGTH_LONG).show();
        	speaker.speak(sSay );
        	return ret;
    	}
    	getConfig().setPayPWD(Config.sPWD);//保存支付密码参数；
    	//取红包上的文字 ：
    	if(Config.bAutoThunder){//自动雷值：
    		Config.sLuckyMoneySay=Config.KEY_LUCKY_MONEY_SAY;//置空；
    	}else{
    		Config.sLuckyMoneySay=etLuckyMoneySay.getText().toString();
    		if(Config.sLuckyMoneySay.equals("")){
    			sSay="请设置红包上的文字！";
    			Toast.makeText(MainActivity.this, sSay, Toast.LENGTH_LONG).show();
    			speaker.speak(sSay );
    			return ret;
    		}
    		getConfig().setLuckyMoneySay(Config.sLuckyMoneySay);//保存红包上的文字参数；
    	}//if(Config.bAutoThunder){//自动雷值：
    	
    	ret=true;
    	return ret;
    }
    //配置参数：
    private void SetParams(){
    	//参数控件初始化
    	//参数控件初始化
    	//发包金额；
    	Config.sMoneyValue=getConfig().getMoneyValue();//发包金额；
    	etMoneyValue.setText(Config.sMoneyValue);
    	//发包包数；
    	Config.sLuckyMoneyNum=getConfig().getLuckyMoneyNum();//发包包数；
    	etLuckyMoneyNum.setText(Config.sLuckyMoneyNum);
    	//支付密码；
    	Config.sPWD=getConfig().getPayPWD();//支付密码；
    	etPWD.setText(Config.sPWD);
    	//埋雷位置；
    	Config.iMoneyThunderPos=getConfig().getMoneyValuePos();//雷在红包金额中的位置；
    	if(Config.iMoneyThunderPos==Config.KEY_THUNDER_FEN)rbFen.setChecked(true);
    	if(Config.iMoneyThunderPos==Config.KEY_THUNDER_JIAO)rbJiao.setChecked(true);
    	if(Config.iMoneyThunderPos==Config.KEY_THUNDER_YUAN)rbYuan.setChecked(true);
    	//出现的雷数；
    	Config.iThunder=getConfig().getThunderNum();//出现的雷数；
    	if(Config.iThunder==1)rb1.setChecked(true);
    	if(Config.iThunder==2)rb2.setChecked(true);
    	if(Config.iThunder==3)rb3.setChecked(true);
    	if(Config.iThunder==4)rb4.setChecked(true);
    	if(Config.iThunder==5)rb5.setChecked(true);
    	if(Config.iThunder==6)rb6.setChecked(true);
    	if(Config.iThunder==7)rb7.setChecked(true);
    	if(Config.iThunder==8)rb8.setChecked(true);
    	if(Config.iThunder==9)rb9.setChecked(true);
    	if(Config.iThunder==10)rb10.setChecked(true);
    	//红包上出现的文字；
    	Config.sLuckyMoneySay=getConfig().getLuckyMoneySay();//红包上出现的文字；
    	etLuckyMoneySay.setText(Config.sLuckyMoneySay);
    	//雷的类型：
    	Config.iMoneySayPos=getConfig().getMoneySayPos();//雷在红包语中的位置；
    	if(Config.iMoneySayPos==Config.KEY_THUNDER_TAIL)rbTail.setChecked(true);
    	if(Config.iMoneySayPos==Config.KEY_THUNDER_HEAD)rbHead.setChecked(true);
    	//是否计算最佳雷值
    	Config.bAutoThunder=getConfig().getAutoThunder();//是否计算最佳雷值
    	if(Config.bAutoThunder==Config.KEY_AUTO_THUNDER){
    		rbAutoThunder.setChecked(true);
    		etLuckyMoneySay.setText(Config.KEY_LUCKY_MONEY_SAY_AUTO);
    	}
    	if(Config.bAutoThunder==Config.KEY_NO_AUTO_THUNDER){
    		rbHandThunder.setChecked(true);
    	}
    	//单雷双雷三雷
    	Config.iMoneySayThunderNum=getConfig().getMoneySayThunderNum();//
    	if(Config.iMoneySayThunderNum==Config.KEY_MONEY_SAY_SINGLE_THUNDER)rbSingleThunder.setChecked(true);
    	if(Config.iMoneySayThunderNum==Config.KEY_MONEY_SAY_DOUBLE_THUNDER)rbDoubleThunder.setChecked(true);
    	if(Config.iMoneySayThunderNum==Config.KEY_MONEY_SAY_THREE_THUNDER)rbThreeThunder.setChecked(true);
    	if(Config.iMoneySayThunderNum==Config.KEY_MONEY_SAY_FOUR_THUNDER)rbFourThunder.setChecked(true);
    	if(Config.iMoneySayThunderNum==Config.KEY_MONEY_SAY_FIVE_THUNDER)rbFiveThunder.setChecked(true);
    	//埋雷玩法
    	Config.iPlaying=getConfig().getPutThunderPlaying();//
    	if(Config.iPlaying==Config.KEY_PUT_THUNDER_COMMON_PLAYING)rbCommonPlaying.setChecked(true);
    	if(Config.iPlaying==Config.KEY_PUT_THUNDER_BUMP_PLAYING)rbBumpPlaying.setChecked(true);
    	//埋雷延迟；
    	Config.iDelayTime=getConfig().getWechatOpenDelayTime();//埋雷延迟；
    	mSeekBar.setProgress(Config.iDelayTime);
    	//发音模式：
    	if(Config.bSpeaking==Config.KEY_NOT_SPEAKING){
    		rbCloseSound.setChecked(true);//自动返回
    	}else if(Config.speaker.equals(Config.KEY_SPEAKER_FEMALE)){
    		rbFemaleSound.setChecked(true);
    	}else if(Config.speaker.equals(Config.KEY_SPEAKER_MALE)){
    		rbMaleSound.setChecked(true);
    	}else if(Config.speaker.equals(Config.KEY_SPEAKER_SPECIAL_MALE)){
    		rbSpecialMaleSound.setChecked(true);
    	}else if(Config.speaker.equals(Config.KEY_SPEAKER_EMOTION_MALE)){
    		rbMotionMaleSound.setChecked(true);
    	}else if(Config.speaker.equals(Config.KEY_SPEAKER_CHILDREN)){
    		rbChildrenSound.setChecked(true);
    	}
    	speaker.setSpeaker(Config.speaker);
    	speaker.setSpeaking(Config.bSpeaking);
    	 //绑定排雷位置
    	rgThunderPos.setOnCheckedChangeListener(new OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(RadioGroup arg0, int arg1) {
                 // TODO Auto-generated method stub
                 //获取变更后的选中项的ID
                int radioButtonId = arg0.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                 //更新文本内容，以符合选中项
                String sChecked=rb.getText().toString();
                 //tv.setText("您的性别是：" + rb.getText());
                if(sChecked.equals("分")){
                	getConfig().setMoneyValuePos(Config.KEY_THUNDER_FEN);
                	speaker.speak("当前设置：分为雷。");
                	Toast.makeText(MainActivity.this, "分为雷", Toast.LENGTH_LONG).show();
                }
                if(sChecked.equals("角")){
                	getConfig().setMoneyValuePos(Config.KEY_THUNDER_JIAO);
                	speaker.speak("当前设置：角为雷。");
                	Toast.makeText(MainActivity.this, "角为雷", Toast.LENGTH_LONG).show();
                }
                if(sChecked.equals("元")){
                	getConfig().setMoneyValuePos(Config.KEY_THUNDER_YUAN);
                	speaker.speak("当前设置：元为雷。");
                	Toast.makeText(MainActivity.this, "元为雷", Toast.LENGTH_LONG).show();
                }
                	
            }
         });
      	 //绑定出现的雷数
       	rgThunderNum.setOnCheckedChangeListener(new OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(RadioGroup arg0, int arg1) {
                   // TODO Auto-generated method stub
                   //获取变更后的选中项的ID
                  int radioButtonId = arg0.getCheckedRadioButtonId();
                  //根据ID获取RadioButton的实例
                   RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                   //更新文本内容，以符合选中项
                   String sChecked=rb.getText().toString();
                   int iNum=Integer.parseInt(sChecked);
                   String sSay="当前埋雷数为："+sChecked+"个雷。";
                   Config.iThunder=iNum;
                   getConfig().setThunderNum(iNum);
                   Toast.makeText(MainActivity.this,sSay, Toast.LENGTH_LONG).show();
                   speaker.speak(sSay);
                   
              }
           });

     	 //绑定红包语排雷位置
      	rgMoneySay.setOnCheckedChangeListener(new OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(RadioGroup arg0, int arg1) {
                   // TODO Auto-generated method stub
                   //获取变更后的选中项的ID
                  int radioButtonId = arg0.getCheckedRadioButtonId();
                  //根据ID获取RadioButton的实例
                   RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                   //更新文本内容，以符合选中项
                   String sChecked=rb.getText().toString();
                   //tv.setText("您的性别是：" + rb.getText());
                   String say="";
                  if(sChecked.equals("文字尾为雷")){
               	   Config.iMoneySayPos=Config.KEY_THUNDER_TAIL;
               	   say="当前设置：红包语中最后一个数字为雷。";

                  }
                  if(sChecked.equals("文字首为雷")){
               	   Config.iMoneySayPos=Config.KEY_THUNDER_HEAD;
               	   say="当前设置：红包语中第一个数字为雷。";
                  }
                 	getConfig().setMoneySayPos(Config.iMoneySayPos);
                 	speaker.speak(say);
                 	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
              }
           });
       	//设置发包金额：
       	etMoneyValue.addTextChangedListener(new TextWatcher(){
       		@Override
       		public void afterTextChanged(Editable s) {
       			String sShow="当前发包金额："+s+"元";
       			speaker.speak(sShow);
       			Toast.makeText(MainActivity.this, sShow, Toast.LENGTH_LONG).show();
       		}
       		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
       			
       		}
       		public void onTextChanged(CharSequence s, int start, int before,int count) {
       			
       		}
       		
       	});
       	//设置发包包数：
       	etLuckyMoneyNum.addTextChangedListener(new TextWatcher(){
       		@Override
       		public void afterTextChanged(Editable s) {
       			String sShow="当前发包包数："+s+"包";
       			speaker.speak(sShow);
       			Toast.makeText(MainActivity.this, sShow, Toast.LENGTH_LONG).show();
       		}
       		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
       			
       		}
       		public void onTextChanged(CharSequence s, int start, int before,int count) {
       			
       		}
       		
       	});
       	//设置红包语：
       	etLuckyMoneySay.addTextChangedListener(new TextWatcher(){
       		@Override
       		public void afterTextChanged(Editable s) {
       			String sShow="当前红包上的文字为："+s+"。";
       			speaker.speak(sShow);
       			Toast.makeText(MainActivity.this, sShow, Toast.LENGTH_LONG).show();
       		}
       		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
       			
       		}
       		public void onTextChanged(CharSequence s, int start, int before,int count) {
       			
       		}
       		
       	});
    	 //设置是否自动推荐最佳雷值：
       	rgCalcThunder.setOnCheckedChangeListener(new OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(RadioGroup arg0, int arg1) {
                   // TODO Auto-generated method stub
                   //获取变更后的选中项的ID
                  int radioButtonId = arg0.getCheckedRadioButtonId();
                  //根据ID获取RadioButton的实例
                   RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                   //更新文本内容，以符合选中项
                   String sChecked=rb.getText().toString();
                   String say="";
                  if(sChecked.equals("自动计算雷值")){
                	  say="当前设置：自动计算最佳雷值。";
                  	  Config.bAutoThunder=Config.KEY_AUTO_THUNDER;
                  	  etLuckyMoneySay.setText(Config.KEY_LUCKY_MONEY_SAY_AUTO);
                  }
                  if(sChecked.equals("手动填写雷值")){
                	  say="当前设置：手动填写雷值。";
                  	  Config.bAutoThunder=Config.KEY_NO_AUTO_THUNDER;
                  	etLuckyMoneySay.setText(Config.KEY_LUCKY_MONEY_SAY);
                  	etLuckyMoneySay.setFocusable(true);
                  }
                	getConfig().setAutoThunder(Config.bAutoThunder);
                	speaker.speak(say);
                	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
              }
           });
   	 //设置单雷三雷双雷
       	rgSayThunderNum.setOnCheckedChangeListener(new OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(RadioGroup arg0, int arg1) {
                   // TODO Auto-generated method stub
                   //获取变更后的选中项的ID
                  int radioButtonId = arg0.getCheckedRadioButtonId();
                  //根据ID获取RadioButton的实例
                   RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                   //更新文本内容，以符合选中项
                   String sChecked=rb.getText().toString();
                   String say="";
                   //tv.setText("您的性别是：" + rb.getText());
                  if(sChecked.equals("单雷")){
                	  Config.iMoneySayThunderNum=Config.KEY_MONEY_SAY_SINGLE_THUNDER;
                	  say="当前设置：单雷。";
                  }
                  if(sChecked.equals("双雷")){
                	  Config.iMoneySayThunderNum=Config.KEY_MONEY_SAY_DOUBLE_THUNDER;
                	  say="当前设置：双雷。";
                  }
                  if(sChecked.equals("三雷")){
                	  Config.iMoneySayThunderNum=Config.KEY_MONEY_SAY_THREE_THUNDER;
                	  say="当前设置：三雷。";
                  }
                  if(sChecked.equals("四雷")){
                	  Config.iMoneySayThunderNum=Config.KEY_MONEY_SAY_FOUR_THUNDER;
                	  say="当前设置：四雷。";
                  }
                  if(sChecked.equals("五雷")){
                	  Config.iMoneySayThunderNum=Config.KEY_MONEY_SAY_FIVE_THUNDER;
                	  say="当前设置：五雷。";
                  }
              	getConfig().setMoneySayThunderNum(Config.iMoneySayThunderNum);
              	speaker.speak(say);
              	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
              }
           });
        //选择埋雷玩法
       	rgSelPutThunderPlaying.setOnCheckedChangeListener(new OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(RadioGroup arg0, int arg1) {
                   // TODO Auto-generated method stub
                   //获取变更后的选中项的ID
                  int radioButtonId = arg0.getCheckedRadioButtonId();
                  //根据ID获取RadioButton的实例
                   RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                   //更新文本内容，以符合选中项
                   String sChecked=rb.getText().toString();
                   String say="";
                   //tv.setText("您的性别是：" + rb.getText());
                  if(sChecked.equals("普通群玩法")){
                	  Config.iPlaying=Config.KEY_PUT_THUNDER_COMMON_PLAYING;
                	  say="当前选择：普通群玩法。";
                	  //etLuckyMoneyNum.setEnabled(true);
                  }
                  if(sChecked.equals("碰碰群玩法")){
                	  Config.iPlaying=Config.KEY_PUT_THUNDER_BUMP_PLAYING;
                	  say="当前选择：碰碰群玩法。";
                	  etLuckyMoneyNum.setText("5");
                	  rbSingleThunder.setChecked(true);
                	  //etLuckyMoneyNum.setEnabled(false);
                  }

              	getConfig().setPutThunderPlaying(Config.iPlaying);
              	speaker.speak(say);
              	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
              }
           });
        //发音 模式
    	rgSelSoundMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                //获取变更后的选中项的ID
               int radioButtonId = arg0.getCheckedRadioButtonId();
               //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                //更新文本内容，以符合选中项
                String sChecked=rb.getText().toString();
                String say="";
               if(sChecked.equals("关闭语音提示")){
            	   Config.bSpeaking=Config.KEY_NOT_SPEAKING;
               		say="当前设置：关闭语音提示。";
               }
               if(sChecked.equals("女声")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_FEMALE;
               		say="当前设置：女声提示。";
               }
               if(sChecked.equals("男声")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_MALE;
               		say="当前设置：男声提示。";
               }
               if(sChecked.equals("特别男声")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_SPECIAL_MALE;
               		say="当前设置：特别男声提示。";
               }
               if(sChecked.equals("情感男声")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_EMOTION_MALE;
               		say="当前设置：情感男声提示。";
               }
               if(sChecked.equals("情感儿童声")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_CHILDREN;
               		say="当前设置：情感儿童声提示。";
               }
        	   speaker.setSpeaking(Config.bSpeaking);
        	   speaker.setSpeaker(Config.speaker);
          		getConfig().setWhetherSpeaking(Config.bSpeaking);
          		getConfig().setSpeaker(Config.speaker);
              	speaker.speak(say);
              	Toast.makeText(MainActivity.this,say, Toast.LENGTH_LONG).show();
           }
        });
    }
    @SuppressWarnings("deprecation")
	private void getResolution2(){
        WindowManager windowManager = getWindowManager();    
        Display display = windowManager.getDefaultDisplay();    
        Config.screenWidth= display.getWidth();    
        Config.screenHeight= display.getHeight();  
        Config.currentapiVersion=android.os.Build.VERSION.SDK_INT;
    }
    private void getResolution(){
    	  DisplayMetrics dm = new DisplayMetrics();  
          getWindowManager().getDefaultDisplay().getMetrics(dm);   
          Config.screenWidth=(int)(dm.widthPixels*dm.density/2);   
          Config.screenHeight=(int)(dm.heightPixels*dm.density/2);  
          Config.currentapiVersion=android.os.Build.VERSION.SDK_INT;
    }
    public void setMyTitle(){
        if(Config.version.equals("")){
      	  try {
      		  PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
      		  Config.version =info.versionName;
      	  } catch (PackageManager.NameNotFoundException e) {
      		  e.printStackTrace();
            
      	  }
        }
        if(Config.bReg){
      	  setTitle(getString(R.string.app_name) + " v" + Config.version+"（正式版）");
        }else{
      	  setTitle(getString(R.string.app_name) + " v" + Config.version+"（试用版）");
        }
    }
    /**显示版本信息*/
    public void showVerInfo(boolean bReg){
        if(bReg){
        	Config.bReg=true;
        	getConfig().setREG(true);
        	tvRegState.setText("授权状态：已授权");
        	tvRegWarm.setText("升级技术售后联系"+Config.contact);
        	etRegCode.setVisibility(View.INVISIBLE);
        	tvPlease.setVisibility(View.INVISIBLE);
        	btReg.setVisibility(View.INVISIBLE);
        	speaker.speak("欢迎使用QQ控制发包金额！您是正版用户！" );
        	
        }else{
        	Config.bReg=false;
        	getConfig().setREG(false);
        	tvRegState.setText("授权状态：未授权");
        	tvRegWarm.setText(Config.warning+"技术及授权联系"+Config.contact);
        	etRegCode.setVisibility(View.VISIBLE);
        	tvPlease.setVisibility(View.VISIBLE);
        	btReg.setVisibility(View.VISIBLE);
        	speaker.speak("欢迎使用QQ控制发包金额！您是试用版用户！" );
        	
        }
        String html = "<font color=\"blue\">官方网站下载地址(点击链接打开)：</font><br>";
        html+= "<a target=\"_blank\" href=\""+Config.homepage+"\"><font color=\"#FF0000\"><big><b>"+Config.homepage+"</b></big></font></a>";
        //html+= "<a target=\"_blank\" href=\"http://119.23.68.205/android/android.htm\"><font color=\"#0000FF\"><big><i>http://119.23.68.205/android/android.htm</i></big></font></a>";
        tvHomePage.setTextColor(Color.BLUE);
        tvHomePage.setBackgroundColor(Color.WHITE);//
        //tvHomePage.setTextSize(20);
        tvHomePage.setText(Html.fromHtml(html));
        tvHomePage.setMovementMethod(LinkMovementMethod.getInstance());
        setMyTitle();
        updateMeWarning(Config.version,Config.new_version);//软件更新提醒
    }
    /**  软件更新提醒*/
    private void updateMeWarning(String version,String new_version){
 	   try{
 		   float f1=Float.parseFloat(version);
 		   float f2=Float.parseFloat(new_version);
 	   if(f2>f1){
 		   showUpdateDialog();
 	   }
 	   } catch (Exception e) {  
            e.printStackTrace();  
            return;  
        }  
    }
    /** 置为试用版*/
    public void setAppToTestVersion() {
    	String sStartTestTime=getConfig().getStartTestTime();//取自动置为试用版的开始时间；
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);//yyyy-MM-dd_HH-mm-ss
    	String currentDate =sdf.format(new Date());//取当前时间；
    	int timeInterval=getConfig().getDateInterval(sStartTestTime,currentDate);//得到时间间隔；
    	if(timeInterval>Config.TestTimeInterval){//7天后置为试用版：
    		showVerInfo(false);
    		//ftp.getFtp().DownloadStart();//下载服务器信息;
    	}
    }
    private   void   showUpdateDialog(){ 
        /* @setIcon 设置对话框图标 
         * @setTitle 设置对话框标题 
         * @setMessage 设置对话框消息提示 
         * setXXX方法返回Dialog对象，因此可以链式设置属性 
         */ 
        final AlertDialog.Builder normalDialog=new  AlertDialog.Builder(MainActivity.this); 
        normalDialog.setIcon(R.drawable.ic_launcher); 
        normalDialog.setTitle(  "升级提醒"  );
        normalDialog.setMessage("有新版软件，是否现在升级？"); 
        normalDialog.setPositiveButton("确定",new DialogInterface.OnClickListener(){
            @Override 
            public void onClick(DialogInterface dialog,int which){ 
                //...To-do
     		   Uri uri = Uri.parse(Config.download);    
     		   Intent it = new Intent(Intent.ACTION_VIEW, uri);    
     		   startActivity(it);  
            }
        }); 
        normalDialog.setNegativeButton("关闭",new DialogInterface.OnClickListener(){ 
            @Override 
            public void onClick(DialogInterface dialog,   int   which){ 
            //...To-do 
            } 
        }); 
        // 显示 
        normalDialog.show(); 
    } 
}
